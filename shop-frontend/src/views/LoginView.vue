
<template>
  <div class="min-h-screen flex flex-col justify-center items-center">
    <h1 class="text-4xl font-bold text-center">LOG IN NOOOOW</h1>
    <form @submit.prevent="login"class="mt-5 w-full max-w-lg mx-auto flex flex-col">
      <Field
      label="Username"
      required
      :error-message="errorMessage"
      hide-err-message>
      <FormInput
      v-model:model-value="username"
      placeholder="username"
      type="text">
      </FormInput>
      </Field>

      <Field
      label="Password"
      required
      :error-message="errorMessage"
      description="you can choose whatever as a password"
      >
      <FormInput
      v-model:model-value="password"
      type="password"
      placeholder ="Password"
      ></FormInput>
    </Field>

      <button  type="submit" class="bg-blue-500 hover:bg-blue-600 mt-2.5 py-2.5 rounded">
        Log in
      </button>
    </form>
    <RouterLink class=" text-center w-full max-w-lg justify:center allign-center bg-blue-500 hover:bg-blue-600 mt-2.5 py-2.5 rounded" :to="{name: 'SignUp'}">
       <button>Register</button>
      </RouterLink>
  </div>
  <p>{{ password }} {{ username }}</p>
  <p v-if="authStore.auth === null">store is null</p>
  <p v-if="authStore.auth !== null">store not null</p>
  <p>{{authStore.auth}}</p>
  <p v-if="authStore.user">{{authStore.user.id}}</p>
  <button @click="authStore.logout()">{{authStore}}</button>
</template>

<script setup>
import FormLabel from "@/components/FormLabel.vue"
import FormInput from "@/components/FormInput.vue"
import ErrorMessage from "@/components/ErrorMessage.vue";
import InputDescription from "@/components/InputDescription.vue";
import Field from "@/components/Field.vue";
import axios from 'axios';
import { useRouter } from 'vue-router';
import { ref } from 'vue';
import { useAuthStore } from "../stores/AuthStore"
let username = ref('');
let password = ref('');
const router = useRouter();
const errorMessage = ref('');
const baseUrl = import.meta.env.VITE_API_URL;
const authStore = useAuthStore();

function login(){
  console.log("datassssss: "+password.value)
 // Encode username and password in Base64 format
 const basicAuth = "Basic " + btoa(username.value+ ":" + password.value);

  console.log(baseUrl + "/users/login");
  axios.post(baseUrl + "/users/login",null, {
      headers: {
        'Authorization': basicAuth,
        "Content-Type": "application/json",
          Accept: "application/json",
      },
       withCredentials : true
    },)
    .then(response => {
      console.log(response.data);
      //set auth value in pinia store
      authStore.login(response.data.userInfo)
      //redirict to restricted requested page if exist else to home page
      const nextUrl = localStorage.getItem('requestedUrl')
      if(nextUrl){
        router.push(nextUrl);
      } else{
        router.push({name:'home'})
      }
    })
    .catch(error => {
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
}




</script>
