<script setup>
    import { RouterLink } from 'vue-router';
    import searchbar from './SearchBar.vue';
    import cart from './Cart.vue';
    import darkMode from './DarkMode.vue';
    
    
</script>

<template>
  <header class="sticky .top-0 bg-black shadow-lg items-center width:100%">
    <nav class="flex flex-col sm:flex-row items-center text-white py-6 px-6 justify-center gap-3" >
        <RouterLink :to="{name: 'home'}">
            <div class="flex items-center flex-1">
                <p class="text-2xl">Home</p>
            </div>
        </RouterLink>
        
        <searchbar class="w-1/2"/> 
        
        <RouterLink :to="{name: 'login'}">
            <div class="flex items-center">
                <p class="text-2xl">Login/Signup then profile</p>
            </div>
        </RouterLink>
        
        <RouterLink :to="{name: 'login'}">
            <div class="flex items-center">
                <cart/>
            </div>
        </RouterLink>

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
