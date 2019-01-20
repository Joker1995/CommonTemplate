<template>
  <div class="app-container">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="110px" style="width: 400px; margin-left:50px;">
      <el-form-item :label="form.projectPackageName" prop="projectPackageName">
        <el-input v-model="temp.projectPackageName"/>
      </el-form-item>
      <el-button type="primary" @click="generateProject()">{{ generateProjectText }}</el-button>
    </el-form>
  </div>
</template>
<script>
import { doGetGenerateProject } from '@/api/code/code'

export default {
  data() {
    return {
      tableKey: 0,
      list: null,
      listLoading: true,
      temp: {
        projectPackageName: undefined
      },
      form: {
        projectPackageName: '项目包名称'
      },
      rules: {
        projectPackageName: [{ required: true, message: 'projectPackageName is required', trigger: 'blur' }]
      },
      generateProjectText: '生成项目代码'
    }
  },
  methods: {
    resetTemp() {
      this.temp = {
        projectPackageName: undefined
      }
    },
    generateProject() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          doGetGenerateProject(this.temp, '项目代码.zip').then(() => {
            this.resetTemp()
          }, (error) => {
            console.log(error)
          })
        }
      })
    }
  }
}
</script>
