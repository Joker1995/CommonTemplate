<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :key="tableKey"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;">
      <el-table-column :label="table.number" align="center" width="65">
        <template slot-scope="scope">
          <span>{{ scope.row.number }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="table.jdbcName" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.jdbcName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="table.comment" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.comment }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="table.actions" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleGenerateCode(scope.row)">{{ table.generateCode }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogStatus" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="110px" style="width: 400px; margin-left:50px;">
        <el-form-item :label="table.packageName" prop="packageName">
          <el-input v-model="temp.packageName"/>
        </el-form-item>
        <el-form-item :label="table.projectPackageName" prop="projectPackageName">
          <el-input v-model="temp.projectPackageName"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ table.cancel }}</el-button>
        <el-button type="primary" @click="generateCode()">{{ table.confirm }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { doGetDataSourceTableList, doGetGenerateCode } from '@/api/code/code'

export default {
  data() {
    return {
      tableKey: 0,
      list: null,
      listLoading: true,
      temp: {
        packageName: undefined,
        projectPackageName: undefined,
        config: {
          name: this.$route.query.dataSource
        },
        table: {}
      },
      dialogFormVisible: false,
      dialogStatus: '生成简易代码',
      rules: {
        packageName: [{ required: true, message: 'packageName is required', trigger: 'blur' }],
        projectPackageName: [{ required: true, message: 'projectPackageName is required', trigger: 'blur' }]
      },
      table: {
        number: '序号',
        cancel: '取消',
        confirm: '确认',
        actions: '操作',
        generateCode: '生成简易代码',
        packageName: '生成包前缀',
        projectPackageName: '项目包名',
        jdbcName: '数据表名称',
        comment: '备注'
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      const data = {}
      data.name = this.$route.query.dataSource
      doGetDataSourceTableList(data).then(response => {
        this.list = response.data
        let i = 0
        for (const item of this.list) {
          item.number = Number(++i)
        }
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    resetTemp() {
      this.temp = {
        packageName: undefined,
        projectPackageName: undefined,
        config: {
          name: this.$route.query.dataSource
        },
        table: {}
      }
    },
    handleGenerateCode(row) {
      this.resetTemp()
      this.temp.table.jdbcName = row.jdbcName
      this.dialogFormVisible = true
    },
    generateCode() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const fileName = '生成代码.zip'
          doGetGenerateCode(this.temp, fileName).then(() => {
            this.dialogFormVisible = false
          })
        }
      })
    }
  }
}
</script>
