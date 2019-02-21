<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">{{ $t('table.add') }}</el-button>
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
      <el-table-column :label="$t('table.dataBaseName')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.jdbcUrl')" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.jdbcUrl }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="270" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleSeleceTable(scope.row)">{{ $t('table.selectTable') }}</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('table.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogStatus" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item :label="$t('table.dataBaseName')" prop="name">
          <el-input v-model="temp.name"/>
        </el-form-item>
        <el-form-item :label="$t('table.jdbcUrl')" prop="jdbcUrl">
          <el-input v-model="temp.jdbcUrl"/>
        </el-form-item>
        <el-form-item :label="$t('table.userName')" prop="userName">
          <el-input v-model="temp.userName"/>
        </el-form-item>
        <el-form-item :label="$t('table.password')" prop="password">
          <el-input v-model="temp.password"/>
        </el-form-item>
        <el-form-item :label="$t('table.remark')">
          <el-input :autosize="{ minRows: 2, maxRows: 4}" v-model="temp.comment" type="textarea" placeholder="请输入"/>
        </el-form-item>
      </el-form>
      <div v-if="dialogStatus==='新增'" slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="createData()">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { doGetDataSourceList, doRegisterDataSource, doRemoveDataSource } from '@/api/code/code'

export default {
  data() {
    return {
      tableKey: 0,
      list: null,
      listLoading: true,
      temp: {
        name: undefined,
        jdbcUrl: undefined,
        userName: undefined,
        password: undefined,
        comment: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      rules: {
        name: [{ required: true, message: 'dataBaseName is required', trigger: 'blur' }],
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
          item.name = item.dataBaseName
        }
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    resetTemp() {
      this.temp = {
        name: undefined,
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
        console.log(this.temp)
        if (valid) {
          doRegisterDataSource(this.temp).then(response => {
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.dialogFormVisible = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      doRemoveDataSource(row).then(response => {
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
        this.getList()
      })
    },
    handleSeleceTable(row) {
      const dataSourceName = row.dataBaseName
      this.$router.push('/code/Tables?dataSource=' + dataSourceName)
    }
  }
}
</script>
