<script setup>
import ItemCard from '@/components/ItemCard.vue';
const baseUrl = import.meta.env.VITE_API_URL;


// import TheWelcome from '../components/TheWelcome.vue'
let image = "https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60";
</script>

<template>
  <main>
    <!-- <TheWelcome /> -->
    <!-- cards container -->
  <div class="flex flex-wrap felex-col sm:flex-row items-center" >
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
  </main>
</template>
