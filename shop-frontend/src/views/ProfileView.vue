<template class="bg-black">
  <!-- <p>{{ user }}</p>
  <p>{{ authStore }}</p>
  <p>{{ authStore.getLocalStorage }}</p>
  <div class="flex justify-center">
    <div class="flex max-w-96">
      <div class="items-center grid grid-rows-6 grid-cols-5 gap-4">
        <div class="row-span-6 col-span-2">
          <a
            @click="gotoItem(id)"
            class="relative mx-3 flex overflow-hidden justify-center rounded-xl cursor-pointer"
          >
            <img
              class="object-cover rounded-xl border-black"
              src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
              alt="product image"
          /></a>
        </div>
        <div class="col-span-2"></div>
        <div>111</div>
        <div class="col-span-2">02</div>
        <div>111</div>
        <div class="col-span-2">02</div>
        <div>111</div>
        <div class="col-span-2">02</div>
        <div>111</div>
        <div class="col-span-2">02</div>
        <div>111</div>
        <div class="col-span-2">02</div>
        <div>111</div>
        <div class="col-span-2">02</div>
        <div>111</div>
        <div class="col-span-2">02</div>
        <div>111</div>
      </div>
    </div>
  </div> -->

  <div class="flex justify-center mt-10">
    <form class="flex flex-wrap justify-center">
      <div class="flex flex-col">
      <a
        @click="gotoItem(id)"
        class="relative mx-3 h-40 w-40 flex overflow-hidden justify-center rounded-xl cursor-pointer"
      >
        <img
          class="object-cover rounded-xl border-black"
          src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
          alt="product image"/>
        </a>
        <button
    @click="authStore.logout()"
    class="bg-blue-500 hover:bg-blue-600 mx-3 mt-2.5 py-2.5 rounded"
  >
    Change Password
  </button>
  <button
    @click="authStore.logout()"
    class="bg-blue-500 hover:bg-blue-600 mx-3 mt-2.5 py-2.5 rounded"
  > Logout</button>
  <button
    @click=""
    class="bg-red-500 hover:bg-blue-600 mx-3 mt-2.5 py-2.5 rounded"
  >
    Delete Profile
  </button>
    </div>
      <div class="flex flex-col">
        <p>Username: {{ data.username }}</p>
        <Field
          :label="'Username: ' + user.username"
          required
          :error-message="UsernameErrorMessage"
        >
          <FormInput
            v-model:model-value="user.username"
            placeholder="username"
            type="text"
          >
          </FormInput>
        </Field>
      
      <Field label="Name" required :error-message="NameErrorMessage">
        <FormInput
          v-model:model-value="user.name"
          placeholder="Name"
          type="text"
        >
        </FormInput>
      </Field>
      <Field label="Surname" required :error-message="SurnameErrorMessage">
        <FormInput
          v-model:model-value="user.surname"
          placeholder="Surname"
          type="text"
        >
        </FormInput>
      </Field>

      <Field label="BirthDate" required :error-message="BirthDateErrorMessage">
        <FormInput v-model:model-value="user.birthDate" type="date">
        </FormInput>
      </Field>

      <Field label="Email" required :error-message="EmailErrorMessage">
        <FormInput
          v-model:model-value="user.email"
          placeholder="email@something.com"
          type="email"
        >
        </FormInput>
      </Field>

      <Field label="Address" :error-message="AddressErrorMessage">
        <FormInput
          v-model:model-value="user.address"
          placeholder="address"
          type="text"
        >
        </FormInput>
      </Field>
    </div>
    </form>
    
  </div>
  <div class="flex justify-center">
    <button @click="modifyUser()" class=" w-full max-w-xl mx-3 mb-2 bg-blue-500 hover:bg-blue-600 mt-2.5 py-2.5 rounded">
          Save
        </button>
  </div>
  


  
</template>

<script setup>
import { ref, reactive, toRaw } from "vue";
import axios from "../axios-config";
import Field from "@/components/Field.vue";
import FormInput from "@/components/FormInput.vue";
import { useAuthStore } from "@/stores/AuthStore";
// let user = reactive({
//   username: "",
//   name: "",
//   surname: "",
//   address: "",
//   email:"",
//   password:"",
//   birthDate:"",
//   roles:'',
// });
let user = ref("");
const UsernameErrorMessage = ref("");
const NameErrorMessage = ref("");
const SurnameErrorMessage = ref("");
const BirthDateErrorMessage = ref("");
const EmailErrorMessage = ref("");
const AddressErrorMessage = ref("");

const authStore = useAuthStore();
const data = ref("");

axios
  .get("/users/" + authStore.userId)
  .then((response) => {
    data.value = response.data;
    user.value = structuredClone(response.data);
  })
  .catch((error) => {
    console.error("Login failed:", error);
    if (error.response) {
      // The request was made and the server responded with a status code
      // that falls out of the range of 2xx
      console.error("Status:", error.response.status);
      console.error("Data:", error.response.data);
      errorMessage.value = "Login failed. Please check your credentials.";
    } else if (error.request) {
      // The request was made but no response was received
      console.error("No response received:", error.request);
      //errorMessage.value = "No response from server. Please try again later.";
    } else {
      // Something happened in setting up the request that triggered an Error
      console.error("Error:", error.message);
      //errorMessage.value =
        //"An unexpected error occurred. Please try again later.";
    }
  });

  function modifyUser(){
    console.log(user)
  axios.put("/users/" + authStore.userId,user.value, {
      headers: {
        "Content-Type": "application/json",
          Accept: "application/json",
      },
    },)
    .then(response => {
      console.log(response.data);
      //redirict to restricted requested page if exist else to home page
      // const nextUrl = localStorage.getItem('requestedUrl')
      // if(nextUrl && nextUrl !== "/profile"){
      //   router.push(nextUrl);
      // } else{
      //   router.push({name:'home'})
      // }
    })
    .catch(error => {
      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        let data = error.response.data
        //TODO FIX THIS IN BACKEND AND FIX THIS HERE NO IFS
        if (error.response.status === 500 && data.includes('duplicate key value violates unique constraint "users_username_key"')) {
          UsernameErrorMessage.value = "Username already exists.";
        } else {
          UsernameErrorMessage.value = data.username ? `Username ${data.username}` : '';
        }
        NameErrorMessage.value = data.name ? `Name ${data.name}` : '';
        SurnameErrorMessage.value = data.surname ? `Surname ${data.surname}` : '';
        BirthDateErrorMessage.value = data.birthDate ? `Birth Date ${data.birthDate}` : '';
        EmailErrorMessage.value = data.email ? `Email ${data.email}` : '';
        AddressErrorMessage.value = data.address ? `Address ${data.address}` : '';
        console.error('Status:', error.response.status);
        console.error('Data:', error.response.data);

      } else if (error.request) {
        // The request was made but no response was received
        console.error('No response received:', error.request);
        //errorMessage.value = 'No response from server. Please try again later.';
      } else {
        // Something happened in setting up the request that triggered an Error
        console.error('Error:', error.message);
        //errorMessage.value = 'An unexpected error occurred. Please try again later.';
      }
    });
}
</script>
