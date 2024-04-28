<script setup>
import navbar from './components/NavBar.vue'
import searchbar from './components/SearchBar.vue'
import ItemCard from './components/ItemCard.vue';
</script>

<template>
  <!-- <RouterView/> -->
    <navbar/>
    <!-- <navbar class="sticky top-0 z-40"/> -->
  
  <!-- cards container -->
  <div class="flex flex-wrap flex-col sm:flex-row items-center" >
    <ItemCard/>
    <ItemCard/>
    <ItemCard/>
    <ItemCard/>
    <ItemCard/>
    <ItemCard/>
    <ItemCard/>
    <ItemCard/>
    <ItemCard/>
    <ItemCard/>
    <ItemCard/>
    <ItemCard/>
  </div>
  
  
  <!-- <header>
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="You did it!" />

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
      </nav>
    </div>
  </header>


<script setup>
</script>


  <RouterView /> -->
  <RouterView/>
  
</template>

