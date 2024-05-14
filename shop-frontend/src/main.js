import './assets/tailwind.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
// import instance from './axios-config'

const app = createApp(App)
//make axios available globalli without import at this.$axios
// loses autocompile and suggestions from vs code
// app.config.globalProperties.axios = instance

app.use(router)

app.mount('#app')
