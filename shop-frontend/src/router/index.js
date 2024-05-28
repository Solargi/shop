import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import SignUpView from '@/views/SignUpView.vue'
import OrdersView from '@/views/OrdersView.vue'
import ProfileView from '@/views/ProfileView.vue'
import CartView from '@/views/CartView.vue'
import { useAuthStore } from '@/stores/AuthStore'



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
      component:OrdersView,
      meta : {
        requiresAuth: true
      }
    },
    {
      path:'/cart',
      name:'cart',
      component:CartView
    },
    {
      path:'/profile',
      name:'profile',
      component:ProfileView,
      meta: {
        requiresAuth: true //lets router know that this route requires authentication
      }
    },
  ]
})

//navigation guard to redirict to unauthorized requests to login a
//and then back to desired page if login successfull

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const isAuthenticated = authStore.auth; // TODO implement this when logic for login check ready ->pinia state

  //if route requires authentication and the user is not authenticated redirict -> pinia srtore
  console.log(to.meta.requiresAuth && isAuthenticated)
  if(to.meta.requiresAuth && !isAuthenticated){
    //store the page the user is trying to reach pinia or loaclstorage ->use pinia
    localStorage.setItem('requestedUrl', to.fullPath);
    //redirict to login
    next({name : 'login'})
  } else {
    // if authenticated go on
    next();
  }
})

export default router
