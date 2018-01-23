// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import Cons from './components/Cons'
import router from './router'
import VueResource from 'vue-resource'
/** bootstrap-vue **/
import BootstrapVue from 'bootstrap-vue/dist/bootstrap-vue.esm'
Vue.use(VueResource)
// cors
Vue.http.interceptors.push((request, next) => {
  request.credentials = true
  next()
})

// Globally register components
Vue.use(BootstrapVue)

/** vue-i18n **/
import VueI18n from 'vue-i18n'
Vue.use(VueI18n)
const i18n = new VueI18n({})

// Vue.directive('t', {})
Vue.component('t', {
  render: function (createElement) {
    return this.$slots.default[0]
  }
})
Vue.directive('t', {
  bind (el, binding, vnode) {
    el.originText = el.innerText.trim()
    // console.log(vnode.context)
    if (vnode.context.$root.lang) {
      el.innerHTML = vnode.context.$t(el.originText)
    }
  },
  componentUpdated (el, binding, vnode) {
    if (vnode.context.$root.lang) {
      el.innerHTML = vnode.context.$t(el.originText)
    }
  }
})
Vue.component('t', {
  render: function () {
    // console.log(this)
    if (!this.$root.lang) {
      return this.$slots.default[0]
    }
    return this.$root._v(this.$t(this.$slots.default[0].text))
  }
})
Vue.config.productionTip = false

window.bus = new Vue()
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  i18n,
  data () {
    return {
      lang: undefined,
      languages: []
    }
  },
  template: '<App/>',
  mounted () {
    this.switchLang(localStorage.lang || 'zh_CN')

    this.$http.get(Cons.apiHost + 'api/resource/1_i18n/locale').then(
      response => {
        let json = response.body
        if (json.success) {
          this.languages = json.list
        }
      }
    )
  },
  methods: {
    switchLang (lang) {
      window.bus.$emit('loading')
      this.$http.get(Cons.apiHost + 'openapi/i18n/i18n/' + lang + '.json').then(res => {
        i18n.locale = lang
        i18n.setLocaleMessage(lang, res.body)

        if (!this.lang) {
          // this.$options.components.t = new VueComponent({
          //   render: function () {
          //     // console.log(this)
          //     if (!this.$root.lang) {
          //       return this.$slots.default[0]
          //     }
          //     return this.$root._v(this.$t(this.$slots.default[0].text))
          //   }
          // })
          // vm.directive('t', {
          //   bind (el, binding, vnode) {
          //     el.originText = el.innerHTML
          //     el.innerHTML = vnode.context.$t(el.originText)
          //   },
          //   componentUpdated (el, binding, vnode) {
          //     el.innerHTML = vnode.context.$t(el.originText)
          //   }
          // })
        }
        this.lang = localStorage.lang = lang
        window.bus.$emit('loaded')
      }, res => {
        alert('服务访问出错')
        window.bus.$emit('loaded')
      })
    }
  },
  components: {App}
})
