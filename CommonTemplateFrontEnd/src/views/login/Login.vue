<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="temp" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
      <h3 class="title">{{ projectName }}</h3>
      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input v-model="temp.username" name="username" type="text" auto-complete="on" placeholder="username" />
      </el-form-item>
      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :type="pwdType"
          v-model="temp.password"
          name="password"
          auto-complete="on"
          placeholder="password"
          @keyup.enter.native="handleLogin" />
        <span class="show-pwd" @click="showPwd">
          <svg-icon icon-class="eye" />
        </span>
      </el-form-item>
      <el-form-item prop="captcha">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input v-model="temp.captcha" name="captcha" type="text" auto-complete="on" placeholder="capatcha"/>
        <img id="captchaImage" ref="captchaImage" @click.prevent="refreshCaptcha">
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
          登录
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="success" style="width:100%;" @click.native.prevent="handleRegister">
          注册
        </el-button>
      </el-form-item>
      <div class="tips"/>
    </el-form>
  </div>
</template>

<script>
import { validatePwd, validateCaptcha } from '@/utils/validate'
import { doGetCaptcha } from '@/api/user/user'

export default {
  name: 'Login',
  data() {
    const validatePass = (rule, value, callback) => {
      if (!validatePwd(value)) {
        callback(new Error('密码必须是8-16位数字、大、小写字母、特殊符号中的三种及三种以上的组合'))
      } else {
        callback()
      }
    }
    const validateCapt = (rule, value, callback) => {
      if (!validateCaptcha(value)) {
        callback(new Error('验证码为4位'))
      } else {
        callback()
      }
    }
    return {
      temp: {
        username: undefined,
        password: undefined,
        captcha: undefined,
        captchaToken: undefined
      },
      captcha: {
        token: undefined,
        image: undefined
      },
      loginRules: {
        password: [{ required: true, trigger: 'blur', validator: validatePass }],
        captcha: [{ required: true, trigger: 'blur', validator: validateCapt }]
      },
      loading: false,
      pwdType: 'password',
      redirect: undefined,
      projectName: '爬虫后台'
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  mounted: function() {
    this.refreshCaptcha()
  },
  methods: {
    showPwd() {
      if (this.pwdType === 'password') {
        this.pwdType = ''
      } else {
        this.pwdType = 'password'
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.temp.captchaToken = this.captcha.token
          this.$store.dispatch('Login', this.temp).then(() => {
            this.loading = false
            this.$router.push({ path: this.redirect || '/' })
          }, () => {
            this.loading = false
          })
        }
      })
    },
    handleRegister() {
      this.$router.push('/register')
    },
    refreshCaptcha() {
      doGetCaptcha().then(async response => {
        const data = response.data
        this.captcha.token = data.token
        this.captcha.image = data.image
        this.$refs.captchaImage.src = 'data:image/png;base64,' + this.captcha.image
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
$bg:#2d3a4b;
$light_gray:#eee;

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      &:-webkit-autofill {
        -webkit-box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: #fff !important;
      }
    }
  }
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}

</style>

<style rel="stylesheet/scss" lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;
.login-container {
  position: fixed;
  height: 100%;
  width: 100%;
  background-color: $bg;
  .login-form {
    position: absolute;
    left: 0;
    right: 0;
    width: 520px;
    max-width: 100%;
    padding: 35px 35px 15px 35px;
    margin: 120px auto;
  }
  .el-form-item{
    #captchaImage{
      position: absolute;
      right: 0;
    }
  }
  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;
    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }
  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }
  .title {
    font-size: 26px;
    font-weight: 400;
    color: $light_gray;
    margin: 0px auto 40px auto;
    text-align: center;
    font-weight: bold;
  }
  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
