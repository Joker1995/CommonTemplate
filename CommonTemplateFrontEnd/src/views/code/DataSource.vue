<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">{{ table.add }}</el-button>
    </div>

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
      <el-table-column :label="table.dataBaseName" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.dataBaseName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="table.jdbcUrl" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.jdbcUrl }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="table.actions" align="center" width="270" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleSeleceTable(scope.row)">{{ table.selectTable }}</el-button>
          <el-button type="primary" size="mini" @click="handleQueryDetail(scope.row)">{{ table.detail }}</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ table.delete }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogStatus" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item :label="table.dataBaseName" prop="dataBaseName">
          <el-input v-model="temp.dataBaseName"/>
        </el-form-item>
        <el-form-item :label="table.jdbcUrl" prop="jdbcUrl">
          <el-input v-model="temp.jdbcUrl"/>
        </el-form-item>
        <el-form-item :label="table.userName" prop="userName">
          <el-input v-model="temp.userName"/>
        </el-form-item>
        <el-form-item :label="table.password" prop="password">
          <el-input v-model="temp.password"/>
        </el-form-item>
        <el-form-item :label="table.remark">
          <el-input :autosize="{ minRows: 2, maxRows: 4}" v-model="temp.comment" type="textarea" placeholder="请输入"/>
        </el-form-item>
      </el-form>
      <div v-if="dialogStatus==='新增'" slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ table.cancel }}</el-button>
        <el-button type="primary" @click="createData()">{{ table.confirm }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { doGetDataSourceList } from '@/api/code/code'

export default {
  data() {
    return {
      tableKey: 0,
      list: null,
      listLoading: true,
      temp: {
        dataBaseName: undefined,
        jdbcUrl: undefined,
        userName: undefined,
        password: undefined,
        comment: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      rules: {
        dataBaseName: [{ required: true, message: 'dataBaseName is required', trigger: 'blur' }],
        jdbcUrl: [{ required: true, message: 'jdbcUrl is required', trigger: 'blur' }],
        userName: [{ required: true, message: 'userName is required', trigger: 'blur' }],
        password: [{ required: true, message: 'password is required', trigger: 'blur' }]
      },
      table: {
        dataBaseName: '名称',
        jdbcUrl: '链接',
        userName: '用户名',
        password: '密码',
        search: '搜索',
        add: '添加',
        number: '序号',
        cancel: '取消',
        remark: '备注',
        confirm: '确认',
        actions: '操作',
        detail: '详情',
        delete: '删除',
        selectTable: '选择数据库'
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true

      doGetDataSourceList().then(response => {
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
        dataBaseName: undefined,
        jdbcUrl: undefined,
        userName: undefined,
        password: undefined,
        comment: undefined
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
          /* 新增数据源操作 TODO */
          this.$notify({
            title: '成功',
            message: '创建成功',
            type: 'success',
            duration: 2000
          })
          this.dialogFormVisible = false
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
    handleSeleceTable(row) {
      const dataSourceName = row.dataBaseName
      this.$router.push('/code/Tables?dataSource=' + dataSourceName)
    }
  }
}
</script>
