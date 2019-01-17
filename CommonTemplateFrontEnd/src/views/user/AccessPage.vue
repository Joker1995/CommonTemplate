<template>
  <div class="app-container">
    <el-container>
      <el-aside>
        <el-tree
          :data="permissionOptions"
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
          <el-table-column :label="table.number" align="center" width="65">
            <template slot-scope="scope">
              <span>{{ scope.row.number }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="table.name" min-width="150px">
            <template slot-scope="scope">
              <span>{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="table.url" min-width="150px">
            <template slot-scope="scope">
              <span>{{ scope.row.url }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="table.actions" align="center" width="230" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">{{ table.edit }}</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ table.delete }}</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

        <el-dialog :title="dialogStatus" :visible.sync="dialogFormVisible">
          <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="90px" style="width: 400px; margin-left:50px;">
            <el-form-item :label="table.name" prop="name">
              <el-input v-model="temp.name"/>
            </el-form-item>
            <el-form-item :label="table.url" prop="url">
              <el-input v-model="temp.url"/>
            </el-form-item>
            <el-form-item :label="table.remark">
              <el-input :autosize="{ minRows: 2, maxRows: 4}" v-model="temp.memo" type="textarea" placeholder="请输入"/>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">{{ table.cancel }}</el-button>
            <el-button type="primary" @click="dialogStatus==='新增'?createData():updateData()">{{ table.confirm }}</el-button>
          </div>
        </el-dialog>
      </el-main>
    </el-container>
  </div>
</template>
<script>
import { Container, Tree } from 'element-ui'
import Pagination from '@/components/Pagination'
import { doGetAccessPageList, doGetAccessPageChildNodes, doCreateAccessPage, doUpdateAccessPage,
  doDeleteAccessPage } from '@/api/user/accessPage'
import { generateTreeData } from '@/utils'

export default {
  components: { Pagination, Tree, Container },
  data() {
    return {
      permissionOptions: [],
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined,
        url: undefined
      },
      currentNodeKey: '',
      temp: {
        id: undefined,
        name: undefined,
        url: undefined,
        parentId: undefined,
        memo: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      rules: {
        name: [{ required: true, message: 'name is required', trigger: 'blur' }],
        url: [{ required: true, message: 'url is required', trigger: 'blur' }]
      },
      table: {
        name: '名称',
        url: '对应链接',
        status: '状态',
        search: '搜索',
        add: '添加',
        number: '序号',
        cancel: '取消',
        remark: '备注',
        confirm: '确认',
        actions: '操作',
        edit: '修改',
        delete: '删除'
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      doGetAccessPageList(this.listQuery).then(response => {
        const data = response.data.list
        this.permissionOptions = generateTreeData(null, data)
        const firstNodeId = this.permissionOptions.length > 0 ? this.permissionOptions[0].id : null
        if (this.currentNodeKey === '') {
          this.currentNodeKey = firstNodeId
          this.initNodeChildList(firstNodeId)
        } else {
          this.initNodeChildList(this.currentNodeKey)
        }
      })
    },
    initNodeChildList(parentId) {
      doGetAccessPageChildNodes(parentId, this.listQuery).then(response => {
        this.list = []
        const dataList = response.data.list
        this.total = response.data.total
        let i = 0
        for (const item of dataList) {
          const obj = {}
          obj.id = item.id
          obj.name = item.name
          obj.url = item.url
          obj.number = Number(++i)
          this.list.push(obj)
        }
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    append(data, event) {
      this.resetTemp()
      this.temp.parentId = data.id
      this.dialogStatus = '新增'
      this.dialogFormVisible = true
      this.$nextTick(() => { this.$refs['dataForm'].clearValidate() })
      event.stopPropagation()
    },
    remove(node, data, event) {
      this.resetTemp()
      this.temp.id = data.id
      this.temp.parentId = data.id
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
      return (
        <span class='custom-tree-node'>
          <span>{node.label}</span>
          <span>
            <el-button size='mini' type='text' on-click={ (event) => this.append(data, event) }>新增</el-button>
            <el-button size='mini' type='text' on-click={ (event) => this.remove(node, data, event) }>删除</el-button>
          </span>
        </span>
      )
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: undefined,
        url: undefined,
        parentId: undefined
      }
    },
    queryChildList(data, node, component) {
      this.currentNodeKey = data.id
      this.initNodeChildList(data.id)
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          /* 新增权限操作 TODO */
          doCreateAccessPage(this.temp).then(response => {
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.dialogFormVisible = false
            this.getList()
            this.initNodeChildList(this.temp.id)
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
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
          doUpdateAccessPage(this.temp).then(response => {
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.dialogFormVisible = false
            this.getList()
            this.initNodeChildList(this.temp.id)
          })
        }
      })
    },
    handleDelete(data) {
      this.resetTemp()
      this.temp.id = data.id
      doDeleteAccessPage(this.temp).then(response => {
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
