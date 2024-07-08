<script setup>
    import { RouterLink } from 'vue-router';
    import searchbar from './SearchBar.vue';
    import cart from './Cart.vue';
    import darkMode from './DarkMode.vue';
    import { useAuthStore } from '@/stores/AuthStore';
    import DropDown from './DropDown.vue';
import { useCartStore } from '@/stores/CartStore';
    // import useAPI from '@/composables/useAPI'
    // const api = useAPI();
    const authStore = useAuthStore();
    const cartStore = useCartStore();
    //     if (localStorage.userID && localStorage.auth){
    //         api.get("/cartItems/" + authStore.userId)
    //         console.log("HERE")
    //         console.log(api.data)}
    localStorage.cartLegnth = 0;
    //cartStore.updateCart();
   
    
    
    
</script>

<template>
    <!-- <p>{{ api.data }}</p> -->
  <header class="sticky .top-0 bg-black shadow-lg items-center width:100%">
    <nav class="flex flex-col sm:flex-row items-center text-white py-6 px-6 justify-center gap-3" >
        <RouterLink :to="{name: 'home'}">
            <div class="flex items-center flex-1">
                <p class="text-2xl">Home</p>
            </div>
        </RouterLink>
        
        <searchbar class="w-1/2"/> 
        
        <RouterLink v-if="authStore.auth" :to="{name: 'profile'}">
            <div class="flex items-center">
                <p class="text-2xl"> profile</p>
            </div>
        </RouterLink>
        
        <RouterLink v-else :to="{name: 'login'}">
            <div class="flex items-center">
                <p class="text-2xl">Login/Signup</p>
            </div>
        </RouterLink>

        
        <RouterLink :to="{name: 'cart'}">
            <div class="flex items-center">
                <cart v-model="cartStore.cartLegnth"/>
            </div>
        </RouterLink>

        <DropDown></DropDown>

        <div class="items-center justify-center" bg-white>
            <darkMode/>
        </div>
        
    </nav>
    <!-- <div class= " flex flex-col items-center">
        <searchbar/> 
    </div> -->
  </header>
  
</template>

<style scoped>

</style>
