<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="table.name" v-model="listQuery.name" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ table.search }}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">{{ table.add }}</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :key="tableKey"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange">
      <el-table-column :label="table.number" align="center" width="65">
        <template slot-scope="scope">
          <span>{{ scope.row.number }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="table.name" prop="name" sortable="custom" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="table.label" prop="label" sortable="custom" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.label }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="table.actions" align="center" width="350" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">{{ table.edit }}</el-button>
          <el-button type="primary" size="mini" @click="handleAccessPageUpdate(scope.row)">{{ table.accessPage }}</el-button>
          <el-button type="primary" size="mini" @click="handleResourceUpdate(scope.row)">{{ table.resource }}</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ table.delete }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="dialogStatus" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item :label="table.name" prop="name">
          <el-input v-model="temp.name"/>
        </el-form-item>
        <el-form-item :label="table.label" prop="label">
          <el-input v-model="temp.label"/>
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

    <el-dialog :title="resourceDialogTitle" :visible.sync="resourceFormVisible">
      <el-tree ref="resourceTree" :data="resourceOptions" :props="defaultProps" :default-checked-keys="temp.resourceIds" show-checkbox node-key="id"/>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resourceFormVisible = false">{{ table.cancel }}</el-button>
        <el-button type="primary" @click="updateResource()">{{ table.confirm }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="accessPageDialogTitle" :visible.sync="accessPageFormVisible">
      <el-tree ref="accessPageTree" :data="accessPageOptions" :props="defaultProps" :default-checked-keys="temp.accessPageIds" show-checkbox node-key="id"/>
      <div slot="footer" class="dialog-footer">
        <el-button @click="accessPageFormVisible = false">{{ table.cancel }}</el-button>
        <el-button type="primary" @click="updateAccessPage()">{{ table.confirm }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { doGetRoleList, doDeleteRole, doCreateRole, doUpdateRole, doUpdateAccessPageList, doUpdateResourceList } from '@/api/user/role'
import { doGetAccessPageList } from '@/api/user/accessPage'
import { doGetResourceList } from '@/api/user/resource'
import { Tree } from 'element-ui'
import Pagination from '@/components/Pagination'
import { generateTreeData, filterSelectNodeId } from '@/utils'
import request from '@/utils/request'

export default {
  components: { Pagination, Tree },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined,
        label: undefined,
        sort: '+id'
      },
      resourceOptions: [],
      accessPageOptions: [],
      temp: {
        id: undefined,
        name: undefined,
        label: undefined,
        accessPageIds: [],
        resourceIds: []
      },
      dialogFormVisible: false,
      dialogStatus: '',
      rules: {
        name: [{ required: true, message: 'name is required', trigger: 'blur' }],
        label: [{ required: true, message: 'nickName is required', trigger: 'blur' }]
      },
      downloadLoading: false,
      resourceDialogTitle: '权限修改',
      resourceFormVisible: false,
      accessPageDialogTitle: '页面修改',
      accessPageFormVisible: false,
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      table: {
        name: '名称',
        label: '昵称',
        status: '状态',
        search: '搜索',
        add: '添加',
        export: '导出',
        number: '序号',
        cancel: '取消',
        remark: '备注',
        confirm: '确认',
        actions: '操作',
        edit: '修改',
        delete: '删除',
        accessPage: '页面权限',
        resource: '接口权限'
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      request.all([doGetAccessPageList({ page: -1, limit: -1 }), doGetResourceList({ page: -1, limit: -1 }), doGetRoleList(this.listQuery)])
        .then(response => {
          const accessPageData = generateTreeData(null, response[0].data.list)
          this.accessPageOptions = accessPageData
          const resourceData = generateTreeData(null, response[1].data.list)
          this.resourceOptions = resourceData
          this.list = response[2].data.list
          this.total = response[2].data.total
          let i = 0
          for (const item of this.list) {
            item.number = Number(++i)
          }
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        })
    },
    refreshRoleList() {
      this.listLoading = true
      doGetRoleList(this.listQuery).then(response => {
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
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    sortChange(data) {
      console.log(data)
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: undefined,
        label: undefined,
        mobilePhone: undefined,
        organization: undefined,
        status: undefined,
        accessPageIds: [],
        resourceIds: []
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
          doCreateRole(this.temp).then(response => {
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.refreshRoleList()
            this.dialogFormVisible = false
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
          doUpdateRole(this.temp).then(response => {
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.refreshRoleList()
            this.dialogFormVisible = false
          })
        }
      })
    },
    handleDelete(data) {
      this.resetTemp()
      this.temp.id = data.id
      doDeleteRole(this.temp).then(response => {
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      })
    },
    handleAccessPageUpdate(data) {
      this.resetTemp()
      this.accessPageFormVisible = true
      const ids = []
      for (const item of data.accessPageList) {
        ids.push(item.id)
      }
      this.temp.id = data.id
      this.temp.accessPageIds = filterSelectNodeId(this.accessPageOptions, ids)
    },
    handleResourceUpdate(data) {
      this.resetTemp()
      this.resourceFormVisible = true
      const ids = []
      for (const item of data.resourceList) {
        ids.push(item.id)
      }
      this.temp.id = data.id
      this.temp.resourceIds = filterSelectNodeId(this.resourceOptions, ids)
    },
    updateAccessPage() {
      this.temp.accessPageIds = this.$refs['accessPageTree'].getCheckedKeys()
      doUpdateAccessPageList(this.temp).then(response => {
        this.$notify({
          title: '成功',
          message: '权限修改成功',
          type: 'success',
          duration: 2000
        })
        this.accessPageFormVisible = false
      })
    },
    updateResource() {
      this.temp.resourceIds = this.$refs['resourceTree'].getCheckedKeys()
      doUpdateResourceList(this.temp).then(response => {
        this.$notify({
          title: '成功',
          message: '权限修改成功',
          type: 'success',
          duration: 2000
        })
        this.resourceFormVisible = false
      })
    }
  }
}
</script>
