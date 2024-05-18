<template>
    <p>{{data}}</p>
    <p>{{authStore}}</p>
    <p>{{authStore.getLocalStorage}} </p>
    <button @click="authStore.getAuth()" class="bg-blue-500 hover:bg-blue-600 mt-2.5 py-2.5 rounded"> getter test</button>
    <button @click="authStore.logout()" class="bg-blue-500 hover:bg-blue-600 mt-2.5 py-2.5 rounded"> Log Out</button>

</template>
  
<script setup>
import { ref } from 'vue';
import axios from '../axios-config'
import { useAuthStore } from '@/stores/AuthStore';


const authStore = useAuthStore();
const data = ref("");


axios.get("/users/1")
.then(response=>{
    data.value = response.data;
}) .catch(error => {
    
      console.error('Login failed:', error);
      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        console.error('Status:', error.response.status);
        console.error('Data:', error.response.data);
        errorMessage.value = 'Login failed. Please check your credentials.';
      } else if (error.request) {
        // The request was made but no response was received
        console.error('No response received:', error.request);
        errorMessage.value = 'No response from server. Please try again later.';
      } else {
        // Something happened in setting up the request that triggered an Error
        console.error('Error:', error.message);
        errorMessage.value = 'An unexpected error occurred. Please try again later.';
      }
    });


</script>