<template>
  <div>
    <el-menu class="navbar" mode="horizontal">
      <hamburger :toggle-click="toggleSideBar" :is-active="sidebar.opened" class="hamburger-container"/>
      <breadcrumb />
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar+'?imageView2/1/w/80/h/80'" class="user-avatar">
          <i class="el-icon-caret-bottom"/>
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <router-link class="inlineBlock" to="/">
            <el-dropdown-item>
              {{ $t('table.home') }}
            </el-dropdown-item>
          </router-link>
          <el-dropdown-item divided>
            <span style="display:block;" @click="dialogVisible=true">{{ $t('table.updateSelfPassword') }}</span>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <span style="display:block;" @click="logout">{{ $t('table.logOut') }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-menu>

    <el-dialog :title="$t('table.updateSelfPassword')" :visible.sync="dialogVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 200px; margin-left:50px;">
        <el-form-item :label="$t('table.password')" prop="password">
          <el-input v-model="temp.password"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="handleUpdateSelfPwd">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { doUpdateSelfPwd } from '@/api/user/user'
import { validatePwd } from '@/utils/validate'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import md5 from 'js-md5'
import { getEncyptSalt } from '@/utils'
import { setToken } from '@/utils/cookie'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  data() {
    const validatePass = (rule, value, callback) => {
      if (!validatePwd(value)) {
        callback(new Error('密码必须是8-16位数字、大、小写字母、特殊符号中的三种及三种以上的组合'))
      } else {
        callback()
      }
    }
    return {
      rules: {
        password: [{ required: true, trigger: 'blur', validator: validatePass }]
      },
      dialogVisible: false,
      temp: {
        password: undefined
      }
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar'
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar')
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    },
    resetTemp() {
      this.temp = {
        password: undefined
      }
    },
    handleUpdateSelfPwd() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const data = {}
          data.password = md5(md5(this.temp.password + getEncyptSalt()))
          doUpdateSelfPwd(data).then(response => {
            this.resetTemp()
            const data = response.data
            if (data) {
              this.dialogVisible = false
              const notify = this.$notify({
                title: '成功',
                message: '修改密码成功,即将退出登录......',
                type: 'success',
                duration: 2000
              })
              notify.onClose = () => {
                setToken('')
                this.$router.push({ path: '/login?redirect=' + this.$route.path })
              }
            } else {
              this.$notify({
                title: '失败',
                message: '修改密码失败',
                type: 'error',
                duration: 2000
              })
            }
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .screenfull {
    position: absolute;
    right: 90px;
    top: 16px;
    color: red;
  }
  .avatar-container {
    height: 50px;
    display: inline-block;
    position: absolute;
    right: 35px;
    .avatar-wrapper {
      cursor: pointer;
      margin-top: 5px;
      position: relative;
      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }
      .el-icon-caret-bottom {
        position: absolute;
        right: -20px;
        top: 25px;
        font-size: 12px;
      }
    }
  }
}
</style>
