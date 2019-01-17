<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="table.name" v-model="listQuery.data.name" style="width: 140px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-input :placeholder="table.label" v-model="listQuery.data.label" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-input :placeholder="table.mobilePhone" v-model="listQuery.data.mobilePhone" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-select v-model="listQuery.data.organization" :placeholder="table.organization" clearable style="width: 200px" class="filter-item">
        <el-option v-for="item in organizationOptions" :key="item.id" :label="item.name" :value="item.id"/>
      </el-select>
      <el-select v-model="listQuery.data.status" :placeholder="table.status" clearable style="width: 90px" class="filter-item">
        <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value"/>
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ table.search }}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">{{ table.add }}</el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">{{ table.export }}</el-button>
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
      <el-table-column :label="table.organization" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.organization }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="table.role" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.role }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="table.actions" align="center" width="420" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">{{ table.edit }}</el-button>
          <el-button type="primary" size="mini" @click="handleRoleUpdate(scope.row)">{{ table.role }}</el-button>
          <el-button type="primary" size="mini" @click="handleResourceUpdate(scope.row)">{{ table.resource }}</el-button>
          <el-button type="primary" size="mini" @click="handleAccessPageUpdate(scope.row)">{{ table.accessPage }}</el-button>
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
        <el-form-item :label="table.organization" prop="organization">
          <el-select v-model="temp.organizationId" class="filter-item" placeholder="请选择">
            <el-option v-for="item in organizationOptions" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="table.status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择" prop="status">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
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

    <el-dialog :title="roleDialogTitle" :visible.sync="roleFormVisible">
      <el-checkbox-group v-model="roleLabelList" @change="selectRole">
        <el-checkbox v-for="item in roleOptions" :label="item.label" :value="item.id" :key="item.label"/>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button @click="roleFormVisible = false">{{ table.cancel }}</el-button>
        <el-button type="primary" @click="updateRole()">{{ table.confirm }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="resourceDialogTitle" :visible.sync="resourceFormVisible">
      <el-tree ref="resourceTree" :data="resourceOptions" :props="defaultProps" show-checkbox node-key="id" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="resourceFormVisible = false">{{ table.cancel }}</el-button>
        <el-button type="primary" @click="updateResource()">{{ table.confirm }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="accessPageDialogTitle" :visible.sync="accessPageFormVisible">
      <el-tree ref="accessPageTree" :data="accessPageOptions" :props="defaultProps" show-checkbox node-key="id" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="accessPageFormVisible = false">{{ table.cancel }}</el-button>
        <el-button type="primary" @click="updateAccessPage()">{{ table.confirm }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { doGetUserList, doGetUserResourcesList, doGetUserRolesList, doGetUserAccessPagesList,
  doUpdateUserRoleList, doCreateUser, doUpdateUserResourceList, doDownloadUserList,
  doUpdateUserAccessPageList, doUpdateUser } from '@/api/user/user'
import { doGetOrganizationList } from '@/api/user/organization'
import { doGetRoleList } from '@/api/user/role'
import { doGetResourceList } from '@/api/user/resource'
import { doGetAccessPageList } from '@/api/user/accessPage'
import { Tree } from 'element-ui'
import Pagination from '@/components/Pagination'
import { generateTreeData, filterSelectNode } from '@/utils'
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
        data: {
          name: undefined,
          label: undefined,
          mobilePhone: undefined,
          organization: undefined,
          status: undefined
        },
        sort: '+id'
      },
      organizationOptions: [],
      resourceOptions: [],
      accessPageOptions: [],
      roleOptions: [],
      roleLabelList: [],
      roleIdList: [],
      statusOptions: [{ value: 'S0A', label: '正常' }, { value: 'SXA', label: '禁用' }],
      temp: {
        id: undefined,
        name: undefined,
        label: undefined,
        mobilePhone: undefined,
        organization: undefined,
        status: undefined,
        roleIds: [],
        accessPageIds: [],
        resourceIds: []
      },
      dialogFormVisible: false,
      dialogStatus: '',
      rules: {
        name: [{ required: true, message: 'name is required', trigger: 'blur' }],
        label: [{ required: true, message: 'nickName is required', trigger: 'blur' }],
        organization: [{ required: true, message: 'organization is required', trigger: 'change' }],
        status: [{ required: true, message: 'status is required', trigger: 'change' }]
      },
      downloadLoading: false,
      resourceDialogTitle: '接口权限修改',
      accessPageDialogTitle: '前端界面权限修改',
      roleDialogTitle: '角色修改',
      resourceFormVisible: false,
      accessPageFormVisible: false,
      roleFormVisible: false,
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      table: {
        name: '名称',
        label: '昵称',
        mobilePhone: '手机号码',
        organization: '部门',
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
        role: '角色',
        resource: '接口权限',
        accessPage: '界面权限'
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      request.all([doGetOrganizationList({ page: -1, limit: -1 }), doGetRoleList({ page: -1, limit: -1 }), doGetResourceList({ page: -1, limit: -1 }),
        doGetAccessPageList({ page: -1, limit: -1 }), doGetUserList(this.listQuery)]).then(response => {
        this.organizationOptions = response[0].data.list
        this.roleOptions = response[1].data.list
        this.resourceOptions = generateTreeData(null, response[2].data.list)
        this.accessPageOptions = generateTreeData(null, response[3].data.list)
        this.list = response[4].data.list
        this.total = response[4].data.total
        let i = 0
        for (const item of this.list) {
          item.number = Number(++i)
          for (const organization of this.organizationOptions) {
            if (organization.id === item.organizationId) {
              item.organization = organization.name
            }
          }
          let roleStr = ''
          for (const role of this.roleOptions) {
            if (item.roleIds.includes(role.id)) {
              roleStr += role.label + ','
            }
          }
          item.role = roleStr.substring(0, roleStr.length - 1)
        }
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.refreshUserList()
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
        status: undefined
      }
    },
    refreshUserList() {
      doGetUserList(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        let i = 0
        for (const item of this.list) {
          item.number = Number(++i)
          for (const organization of this.organizationOptions) {
            if (organization.id === item.organizationId) {
              item.organization = organization.name
            }
          }
          let roleStr = ''
          for (const role of this.roleOptions) {
            if (item.roleIds.includes(role.id)) {
              roleStr += role.label + ','
            }
          }
          item.role = roleStr.substring(0, roleStr.length - 1)
        }
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
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
          doCreateUser(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
          })
          this.dialogFormVisible = false
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy 简单Object
      this.dialogStatus = '修改'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          doUpdateUser(tempData).then(() => {
            this.refreshUserList()
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row) {
      this.$notify({
        title: '成功',
        message: '删除成功',
        type: 'success',
        duration: 2000
      })
    },
    handleDownload() {
      this.downloadLoading = true
      doDownloadUserList(this.listQuery, '用户列表.xls')
      setTimeout(() => {
        this.downloadLoading = false
      }, 2000)
    },
    handleResourceUpdate(row) {
      this.resourceFormVisible = true
      const userId = row.id
      this.temp = Object.assign({}, row) // copy obj
      doGetUserResourcesList(userId).then(response => {
        const data = response.data
        const userResourceIds = []
        for (const item of data) {
          userResourceIds.push(item.id)
        }
        const treeSelectedNodes = filterSelectNode(this.resourceOptions, userResourceIds)
        this.$refs['resourceTree'].setCheckedNodes(treeSelectedNodes)
      })
    },
    handleRoleUpdate(row) {
      this.roleFormVisible = true
      this.roleLabelList = []
      this.temp = Object.assign({}, row)
      const userId = this.temp.id
      doGetUserRolesList(userId).then(response => {
        const data = response.data
        for (const item of data) {
          this.roleLabelList.push(item.label)
        }
      })
    },
    handleAccessPageUpdate(row) {
      this.accessPageFormVisible = true
      const userId = row.id
      doGetUserAccessPagesList(userId).then(response => {
        const data = response.data
        const userAccessPageIds = []
        for (const item of data) {
          userAccessPageIds.push(item.id)
        }
        const treeSelectedNodes = filterSelectNode(this.accessPageOptions, userAccessPageIds)
        this.$refs['accessPageTree'].setCheckedNodes(treeSelectedNodes)
      })
    },
    updateResource() {
      const userId = this.temp.id
      const checkedNodes = this.$refs['resourceTree'].getCheckedNodes()
      const resouceIdList = []
      for (const node of checkedNodes) {
        resouceIdList.push(node.id)
      }
      doUpdateUserResourceList(userId, resouceIdList).then(response => {
        this.$notify({
          title: '成功',
          message: '权限修改成功',
          type: 'success',
          duration: 2000
        })
        this.resourceFormVisible = false
      })
    },
    updateRole() {
      const userId = this.temp.id
      doUpdateUserRoleList(userId, this.roleIdList).then(response => {
        this.refreshUserList()
        this.$notify({
          title: '成功',
          message: '角色修改成功',
          type: 'success',
          duration: 2000
        })
        this.roleFormVisible = false
      })
    },
    updateAccessPage() {
      const userId = this.temp.id
      const checkedNodes = this.$refs['resourceTree'].getCheckedNodes()
      const accessPageIdList = []
      for (const node of checkedNodes) {
        accessPageIdList.push(node.id)
      }
      doUpdateUserAccessPageList(userId, accessPageIdList).then(response => {
        this.$notify({
          title: '成功',
          message: '角色修改成功',
          type: 'success',
          duration: 2000
        })
        this.accessPageFormVisible = false
      })
    },
    selectRole(selectedRoleLabels) {
      this.roleIdList = []
      for (const roleOption of this.roleOptions) {
        for (const selectRole of selectedRoleLabels) {
          if (roleOption.label === selectRole) {
            this.roleIdList.push(roleOption.id)
          }
        }
      }
    }
  }
}
</script>
