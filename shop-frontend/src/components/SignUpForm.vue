<template>
    <div class="min-h-screen flex flex-col justify-center items-center">
      <h1 class="text-4xl font-bold text-center">Sign up IN NOOOOW</h1>
      <form class="mt-5 w-full max-w-lg mx-auto flex flex-col">
        <Field
      label="Username"
      required
      :error-message="UsernameErrorMessage"
      hide-err-message>
      <FormInput
      v-model:model-value="username"
      placeholder="username"
      type="text">
      </FormInput>
      </Field>
      <Field
      label="Name"
      required
      :error-message="NameErrorMessage"
      hide-err-message>
      <FormInput
      v-model:model-value="name"
      placeholder="Name"
      type="text">
      </FormInput>
      </Field>
      <Field
      label="Surname"
      required
      :error-message="SurnameErrorMessage"
      hide-err-message>

      <FormInput
      v-model:model-value="surname"
      placeholder="Surname"
      type="text">
      </FormInput>
      </Field>

      <Field
      label="BirthDate"
      required
      :error-message="BirthDateErrorMessage"
      hide-err-message>
      <FormInput
      v-model:model-value="birthDate"
      type="date">
      </FormInput>
      </Field>

      <Field
      label="Email"
      required
      :error-message="EmailErrorMessage"
      hide-err-message>
      <FormInput
      v-model:model-value="email"
      placeholder="email@something.com"
      type="email">
      </FormInput>
      </Field>

      <Field
      label="Address"
      required
      :error-message="AddressErrorMessage"
      hide-err-message>
      <FormInput
      v-model:model-value="address"
      placeholder="address"
      type="text">
      </FormInput>
      </Field>

      <Field
      label="Role"
      required
      :error-message="RoleErrorMessage"
      hide-err-message>
      <FormInput
      v-model:model-value="role"
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
      v-model:model-value="password"
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
      <button class=" w-full max-w-lg  bg-blue-500 hover:bg-blue-600 mt-2.5 py-2.5 rounded">
          Register
        </button>
    </div>
  </template>

<script setup>
import Field from "@/components/Field.vue"
import FormInput from "./FormInput.vue";

</script>