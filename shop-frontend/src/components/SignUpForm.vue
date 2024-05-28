<template>
    <div class="min-h-screen flex flex-col justify-center items-center">
      <h1 class="text-4xl font-bold text-center">Sign up IN NOOOOW</h1>
      <form class="mt-5 w-full max-w-lg mx-auto flex flex-col">
        <Field
      label="Username"
      required
      :error-message="UsernameErrorMessage">
      <FormInput
      v-model:model-value="user.username"
      placeholder="username"
      type="text">
      </FormInput>
      </Field>
      <Field
      label="Name"
      required
      :error-message="NameErrorMessage"
      >
      <FormInput
      v-model:model-value="user.name"
      placeholder="Name"
      type="text">
      </FormInput>
      </Field>
      <Field
      label="Surname"
      required
      :error-message="SurnameErrorMessage"
      >

      <FormInput
      v-model:model-value="user.surname"
      placeholder="Surname"
      type="text">
      </FormInput>
      </Field>

      <Field
      label="BirthDate"
      required
      :error-message="BirthDateErrorMessage"
      >
      <FormInput
      v-model:model-value="user.birthDate"
      type="date">
      </FormInput>
      </Field>

      <Field
      label="Email"
      required
      :error-message="EmailErrorMessage"
      >
      <FormInput
      v-model:model-value="user.email"
      placeholder="email@something.com"
      type="email">
      </FormInput>
      </Field>

      <Field
      label="Address"
      :error-message="AddressErrorMessage"
      >
      <FormInput
      v-model:model-value="user.address"
      placeholder="address"
      type="text">
      </FormInput>
      </Field>

      <Field
      label="Role"
      required
      :error-message="RoleErrorMessage"
      >
      <FormInput
      v-model:model-value="user.roles"
      placeholder="either USER or ADMIN"
      type="text">
      </FormInput>
      </Field>

      <Field
      label="Password"
      required
      :error-message="PasswordErrorMessage"
      description="you can choose whatever as a password"
      >
      <FormInput
      v-model:model-value="user.password"
      type="password"
      placeholder ="Password"
      ></FormInput>
    </Field>

    <Field
      label="Repeat Password"
      required
      :error-message="RepeatPasswordErrorMessage"
      >
      <FormInput
      v-model:model-value="repeatPassword"
      type="password"
      placeholder ="Password"
      ></FormInput>
    </Field>

      </form>
      <button @click="signUp()" class=" w-full max-w-lg  bg-blue-500 hover:bg-blue-600 mt-2.5 py-2.5 rounded">
          Register
        </button>
      <ErrorMessage v-if="errorMessage"> {{ errorMessage }}</ErrorMessage>
        <p>{{ UsernameErrorMessage }}</p>
    </div>
  </template>

<script setup>
const baseUrl = import.meta.env.VITE_API_URL;
import Field from "@/components/Field.vue"
import FormInput from "./FormInput.vue";
import ErrorMessage from "./ErrorMessage.vue";
import axios from "axios";
import { useRouter } from 'vue-router';
import { ref, reactive, computed, watch} from "vue";
const router = useRouter();
const user = reactive({
  username: "",
  name: "",
  surname: "",
  address: "",
  email:"",
  password:"",
  birthDate:"",
  roles:'',
});
const repeatPassword = ref('');
const UsernameErrorMessage = ref("");
const NameErrorMessage = ref("");
const SurnameErrorMessage = ref("");
const BirthDateErrorMessage = ref("");
const EmailErrorMessage = ref("");
const AddressErrorMessage = ref("");
const RoleErrorMessage = ref("");
const PasswordErrorMessage = ref("");

const RepeatPasswordErrorMessage = computed(() => {
  return user.password === repeatPassword.value ? null : "Passwords do not match";
});



function signUp(){
  axios.post(baseUrl + "/users",user, {
      headers: {
        "Content-Type": "application/json",
          Accept: "application/json",
      },
    },)
    .then(response => {
      console.log(response.data);
      //redirict to restricted requested page if exist else to home page
      const nextUrl = localStorage.getItem('requestedUrl')
      if(nextUrl && nextUrl !== "/profile"){
        router.push(nextUrl);
      } else{
        router.push({name:'home'})
      }
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
        RoleErrorMessage.value = data.roles ? `Role ${data.roles}` : '';
        PasswordErrorMessage.value = data.password ? `Password ${data.password}` : '';
        console.error('Status:', error.response.status);
        console.error('Data:', error.response.data);

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