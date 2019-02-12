import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Cookies from 'js-cookie'
import elementEnLocale from 'element-ui/lib/locale/lang/en' // 英文字体
import elementZhLocale from 'element-ui/lib/locale/lang/zh-CN'// 中文字体
import enLocale from './en'
import zhLocale from './zh'

Vue.use(VueI18n)

const messages = {
  en: {
    ...enLocale,
    ...elementEnLocale
  },
  zh: {
    ...zhLocale,
    ...elementZhLocale
  }
}

const i18n = new VueI18n({
  // set locale
  // options: en | zh
  locale: Cookies.get('language') || 'zh',
  // set locale messages
  messages
})

export default i18n
