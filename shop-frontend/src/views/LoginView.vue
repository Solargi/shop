<template>
  <div class="min-h-screen flex flex-col justify-center items-center">
    <h1 class="text-4xl font-bold text-center">Todo App</h1>
    <form class="mt-5 w-full max-w-lg mx-auto flex flex-col">
      <input
        class="p-3.5 rounded-t border-b border-gray-300 text-gray-900 outline-none"
        type="text"
        placeholder="Username"
        required
      />
      <input
        class="p-3.5 rounded-b text-gray-900 outline-none"
        type="password"
        placeholder="Password"
        required
      />
      <button class="bg-blue-500 hover:bg-blue-600 mt-2.5 py-2.5 rounded">
        Log in
      </button>
    </form>
  </div>
</template>

<script setup></script>
