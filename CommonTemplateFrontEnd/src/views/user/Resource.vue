<template>
  <div class="app-container">
    <el-container>
      <el-aside>
        <el-tree
          :data="resourceOptions"
          :expand-on-click-node="false"
          :render-content="renderContent"
          :current-node-key="currentNodeKey"
          :highlight-current="true"
          node-key="id"
          default-expand-all
          @node-click="queryChildList"/>
      </el-aside>
      <el-main>
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
          <el-table-column :label="$t('table.name')" min-width="150px">
            <template slot-scope="scope">
              <span>{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('table.url')" min-width="150px">
            <template slot-scope="scope">
              <span>{{ scope.row.url }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('table.actions')" align="center" width="230" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">{{ $t('table.edit') }}</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('table.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="initNodeChildList(currentNodeKey)" />

        <el-dialog :title="dialogStatus" :visible.sync="dialogFormVisible">
          <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="90px" style="width: 400px; margin-left:50px;">
            <el-form-item :label="$t('table.name')" prop="name">
              <el-input v-model="temp.name"/>
            </el-form-item>
            <el-form-item :label="$t('table.url')" prop="url">
              <el-input v-model="temp.url"/>
            </el-form-item>
            <el-form-item :label="$t('table.remark')">
              <el-input :autosize="{ minRows: 2, maxRows: 4}" v-model="temp.memo" type="textarea" placeholder="请输入"/>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>
            <el-button type="primary" @click="dialogStatus==='新增'?createData():updateData()">{{ $t('table.confirm') }}</el-button>
          </div>
        </el-dialog>
      </el-main>
    </el-container>
  </div>
</template>
<script>
import { Container, Tree } from 'element-ui'
import Pagination from '@/components/Pagination'
import { doGetResourceChildNodes, doGetResourceList, doCreateResource, doUpdateResource,
  doDeleteResource } from '@/api/user/resource'
import { generateTreeData } from '@/utils'
import deepcopy from 'deepcopy'

export default {
  components: { Pagination, Tree, Container },
  data() {
    return {
      currentNodeKey: '',
      resourceOptions: [],
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: -1,
        limit: -1,
        name: undefined,
        url: undefined,
        parentId: undefined
      },
      temp: {
        id: undefined,
        name: undefined,
        url: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      rules: {
        name: [{ required: true, message: 'name is required', trigger: 'blur' }],
        url: [{ required: true, message: 'url is required', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      doGetResourceList(this.listQuery).then(response => {
        const data = response.data.list
        const root = [{ id: 'root', label: '根节点', children: undefined }]
        root[0].children = generateTreeData(null, data)
        this.resourceOptions = root
        const firstNodeId = root[0].id
        if (this.currentNodeKey === '') {
          this.currentNodeKey = firstNodeId
          this.initNodeChildList(firstNodeId)
        } else {
          this.initNodeChildList(this.currentNodeKey)
        }
      })
    },
    queryChildList(data, node, component) {
      this.currentNodeKey = data.id
      if (this.currentNodeKey !== 'root' && this.listQuery.limit === -1) {
        this.listQuery.limit = 10
        this.listQuery.page = 1
      } else if (this.currentNodeKey === 'root') {
        this.listQuery.limit = -1
        this.listQuery.page = -1
      }
      this.initNodeChildList(data.id)
    },
    initNodeChildList(id) {
      doGetResourceChildNodes(id, this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        let i = 0
        for (const item of this.list) {
          item.number = Number(++i)
        }
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    append(data, event) {
      this.resetTemp()
      if (data.id === 'root') {
        this.temp.parentId = null
      } else {
        this.temp.parentId = data.id
      }
      this.dialogStatus = '新增'
      this.dialogFormVisible = true
      this.$nextTick(() => { this.$refs['dataForm'].clearValidate() })
      event.stopPropagation()
    },
    remove(node, data, event) {
      this.resetTemp()
      if (data.id === 'root') {
        this.temp.id = null
      } else {
        this.temp.id = data.id
      }
      this.$confirm('此操作将永久删除该节点及其下属节点, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.handleDelete(data)
      })
      event.stopPropagation()
    },
    renderContent(h, { node, data, store }) {
      /* 生成树状结构 */
      if (data.id === 'root') {
        return (
          <span class='custom-tree-node'>
            <span>{node.label}</span>
            <span>
              <el-button size='mini' type='text' on-click={ (event) => this.append(data, event) }>新增</el-button>
            </span>
          </span>
        )
      } else {
        return (
          <span class='custom-tree-node'>
            <span>{node.label}</span>
            <span>
              <el-button size='mini' type='text' on-click={ (event) => this.append(data, event) }>新增</el-button>
              <el-button size='mini' type='text' on-click={ (event) => this.remove(node, data, event) }>删除</el-button>
            </span>
          </span>
        )
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: undefined,
        url: undefined
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = '新增'
      this.dialogFormVisible = true
      this.$nextTick(() => { this.$refs['dataForm'].clearValidate() })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          doCreateResource(this.temp).then(response => {
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
            this.dialogFormVisible = false
            this.initNodeChildList(this.temp.id)
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = deepcopy(row) // copy obj
      this.dialogStatus = '修改'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          /* 修改权限操作 TODO */
          doUpdateResource(this.temp).then(response => {
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
            this.dialogFormVisible = false
            this.initNodeChildList(this.temp.id)
          })
        }
      })
    },
    handleDelete(data) {
      this.resetTemp()
      this.temp.id = data.id
      doDeleteResource(this.temp).then(response => {
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
        this.getList()
        this.initNodeChildList(data.id)
      })
    }
  }
}
</script>
