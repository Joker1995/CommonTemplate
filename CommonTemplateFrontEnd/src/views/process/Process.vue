<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('table.name')" v-model="listQuery.name" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleUpload">上传流程资源</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :key="tableKey"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;">
      <el-table-column :label="$t('table.number')" align="center" width="65">
        <template slot-scope="scope">
          <span>{{ scope.row.number }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程资源名称" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.processName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程资源路径" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.processResourcePath }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程版本" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.version }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="320" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handlePreview(scope.row.processName)">流程图预览</el-button>
          <el-button type="primary" size="mini">流程实例</el-button>
          <el-button type="danger" size="mini" @click="handleUndeploy(scope.row.deployId)" >撤销部署</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogFormVisible" title="流程资源部署">
      <el-upload ref="upload" :http-request="handleUploadProcessResource" :limit="1" :auto-upload="false" :action="uploadUrl" drag accept=".zip">
        <i class="el-icon-upload"/>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">只能上传zip文件，且不超过2MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button @click="handleSumbitProcessFile">上传</el-button>
        <el-button @click="handleDeploy">部署</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogPreviewVisible" :before-close="resetTemp" title="流程资源预览">
      <div class="block">
        <img :src="temp.imageSrc">
      </div>
    </el-dialog>
  </div>
</template>
<script>
import request from '@/utils/request'
import { processUploadUrl, doGetProcessList, doDeployProcess, doUnDeployProcess, doPreviewProcessImg } from '@/api/process/process'

export default {
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 0,
        limit: 20
      },
      uploadUrl: processUploadUrl,
      temp: {
        imageSrc: undefined,
        processName: undefined,
        processResourcePath: undefined
      },
      dialogFormVisible: false,
      dialogPreviewVisible: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      doGetProcessList(this.listQuery).then(resp => {
        this.list = resp.data
        let i = 1
        for (const item of this.list) {
          item.number = Number(i++)
        }
      })
      setTimeout(() => {
        this.listLoading = false
      }, 1.5 * 1000)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp(done) {
      this.temp = {
        imageSrc: undefined,
        processName: undefined
      }
      if (done) {
        done(true)
      }
    },
    handleUpload() {
      this.dialogFormVisible = true
    },
    handleUploadProcessResource(param) {
      const fileObj = param.file
      const form = new FormData()
      form.append('file', fileObj)
      request({
        url: '/process/resources',
        method: 'post',
        data: form,
        config: { headers: { 'Content-Type': 'multipart/form-data' }}
      }).then((res) => {
        if (res.data.length > 0) {
          this.temp.processResourcePath = res.data
          this.$notify({
            title: '成功',
            message: '流程文件上传成功',
            type: 'warning',
            duration: 2000
          })
          console.log(param)
        }
      })
    },
    handlePreview(processName) {
      const requestData = { 'processName': processName }
      doPreviewProcessImg(requestData).then(resp => {
        if (resp.data.length > 0) {
          this.temp.imageSrc = 'data:image/png;base64,' + resp.data
          this.temp.processName = processName
          this.dialogPreviewVisible = true
        } else {
          this.$notify({
            title: '查询成功',
            message: '未找到相关流程图片文件',
            type: 'warning',
            duration: 2000
          })
        }
      })
    },
    handleUndeploy(deployId) {
      const requestData = { 'deploymentId': deployId }
      doUnDeployProcess(requestData).then(resp => {
        this.$notify({
          title: '成功',
          message: '取消部署成功',
          type: 'success',
          duration: 2000
        })
        this.getList()
      })
    },
    handleDeploy() {
      const requestData = { 'processResourcePath': this.temp.processResourcePath }
      doDeployProcess(requestData).then(resp => {
        this.$notify({
          title: '成功',
          message: '部署成功',
          type: 'success',
          duration: 2000
        })
        this.dialogFormVisible = false
        this.resetTemp()
        this.getList()
      })
    },
    handleSumbitProcessFile() {
      this.$refs.upload.submit()
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .block{
    img{
      width: 80%;
    }
  }
</style>
