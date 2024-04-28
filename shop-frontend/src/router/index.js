import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import SignUpView from '@/views/SignUpView.vue'
import OrdersView from '@/views/OrdersView.vue'
import ProfileView from '@/views/ProfileView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path:'/login',
      name:'login',
      component:LoginView
    },
    {
      path:'/SingUp',
      name:'SignUp',
      component:SignUpView
    },
    {
      path:'/myOrders',
      name:'orders',
      component:OrdersView
    },
    {
      path:'/profile',
      name:'profile',
      component:ProfileView
    },
  ]
})

export default router
