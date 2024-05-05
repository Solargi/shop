<template>
    <div class="min-h-screen flex flex-col justify-center items-center">
      <h1 class="text-4xl font-bold text-center">Sign up IN NOOOOW</h1>
      <form class="mt-5 w-full max-w-lg mx-auto flex flex-col">
        <input
          class="p-3.5 rounded-t border-b border-gray-300 text-gray-900 outline-none"
          type="text"
          placeholder="Username"
          required
        />
        <input
          class="p-3.5 rounded-t border-b border-gray-300 text-gray-900 outline-none"
          type="text"
          placeholder="Name"
          required
        />
        <input
          class="p-3.5 rounded-t border-b border-gray-300 text-gray-900 outline-none"
          type="text"
          placeholder="Surname"
          required
        />
        <input
          class="p-3.5 rounded-t border-b border-gray-300 text-gray-900 outline-none"
          type="date"
          placeholder=""
          required
        />
        <input
          class="p-3.5 rounded-t border-b border-gray-300 text-gray-900 outline-none"
          type="email"
          placeholder="Email"
          required
        />
        <input
          class="p-3.5 rounded-t border-b border-gray-300 text-gray-900 outline-none"
          type="address"
          placeholder="Address"
          required
        />
        <input
          class="p-3.5 rounded-t border-b border-gray-300 text-gray-900 outline-none"
          type="text"
          placeholder="Role"
          required
        />
        <input
          class="p-3.5 rounded-b text-gray-900 outline-none"
          type="password"
          placeholder="Password"
          required
        />
        <input
          class="p-3.5 rounded-b text-gray-900 outline-none"
          type="password"
          placeholder="Password again"
          required
        />

      </form>
      <button class=" w-full max-w-lg  bg-blue-500 hover:bg-blue-600 mt-2.5 py-2.5 rounded">
          Register
        </button>
    </div>
  </template>

<script setup>

</script>