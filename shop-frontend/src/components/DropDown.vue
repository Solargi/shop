<template>
    <div class="flex flex-col items-center relative">
        <button @click="toggleDropDown" class="flex flex-row block text-2xl">
            <i class="fa-solid fa-user" style="color: #ffffff;"></i>
            <p>ADMINTOOOLS</p>
            <svg class="-mr-1 h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
        <path fill-rule="evenodd" d="M5.23 7.21a.75.75 0 011.06.02L10 11.168l3.71-3.938a.75.75 0 111.08 1.04l-4.25 4.5a.75.75 0 01-1.08 0l-4.25-4.5a.75.75 0 01.02-1.06z" clip-rule="evenodd" />
      </svg>
        </button>
        <div v-if="isOpen" class="bg-white rounded-lg text-black flex flex-col max-w-20 absolute mt-20 py-2 px-2 shadow-sm">
            <a href="#" class="block hover:bg-green-500">test1</a>
            <a href="#">testsss adasd as </a>
            <a href="#">test3</a>
        </div>
    </div>
</template>

<script setup>
    import { ref } from "vue";

    let isOpen = ref(false);
    
    function toggleDropDown(){
        isOpen.value = !isOpen.value;
        console.log(isOpen.value)
    }
</script>