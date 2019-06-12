<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input :placeholder="$t('table.name')" v-model="listQuery.name" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
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
      <el-table-column label="申请用户" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请时间" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.applyTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到期时间" min-width="150px">
        <template slot-scope="scope">
          <span>{{ scope.row.expireTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleApproval(scope.row)">审批</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogFormVisible" title="用户注册审批">
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="申请用户">
          <span>{{ temp.userName }}</span>
        </el-form-item>
        <el-form-item label="申请时间">
          <span>{{ temp.applyTime }}</span>
        </el-form-item>
        <el-form-item label="到期时间">
          <span>{{ temp.expireTime }}</span>
        </el-form-item>
        <el-form-item label="操作">
          <el-radio v-model="temp.approvalResult" label="0">同意</el-radio>
          <el-radio v-model="temp.approvalResult" label="1">驳回</el-radio>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" rows="4" placeholder="请输入内容">{{ temp.memo }}</el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button @click="doApproval">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { doGetRegisterTask, doApprovalRegisterTask } from '@/api/user/user'

export default {
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20
      },
      temp: {
        userName: undefined,
        applyTime: undefined,
        expireTime: undefined,
        approvalResult: undefined
      },
      dialogFormVisible: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      doGetRegisterTask().then(resp => {
        let index = 1
        this.list = resp.data
        for (const item of this.list) {
          item.number = Number(index++)
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
    resetTemp() {
      this.temp = {
        userName: undefined,
        applyTime: undefined,
        expireTime: undefined,
        approvalResult: undefined
      }
    },
    handleApproval(row) {
      this.dialogFormVisible = true
      this.temp = row
    },
    doApproval() {
      doApprovalRegisterTask(this.temp).then(resp => {
        this.dialogFormVisible = false
        this.resetTemp()
        this.getList()
      })
    }
  }
}
</script>
