<template>
    <div v-if="item">
      <h1>{{ item.name }}</h1>
      <p>{{ item.description }}</p>
      <!-- other item details -->
    </div>
    <div v-else>
      <p>Loading...</p>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, watch } from 'vue';
  import { useRoute } from 'vue-router';
  import axios from 'axios';
  
  const route = useRoute();
  const item = ref(null);
  
  const fetchItem = async (id) => {
    try {
      const response = await axios.get(`/items/${id}`);
      item.value = response.data;
    } catch (error) {
      console.error('Error fetching item:', error);
    }
  };
  
  onMounted(() => {
    fetchItem(route.params.id);
  });
  
  watch(() => route.params.id, (newId) => {
    fetchItem(newId);
  });
  </script>
  