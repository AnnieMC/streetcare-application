import { createApp } from 'vue' //Import the createApp function from Vue
import { createPinia } from 'pinia' //

import App from './App.vue'
import router from './router'

const app = createApp(App) //Create a new Vue application instance

app.use(createPinia())
app.use(router) //Use the router in the application

app.mount('#app')
