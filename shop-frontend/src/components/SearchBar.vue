<template>
        <div class="pt-4 mb-8 relative text-black dark:text-white  items-center">
            <input
                type="text"
                v-model="searchQuery"
                placeholder="Search for items"
                class="py-2 px-1 w-full bg-transparent border-b
                focus:green focus:outline-none focus:shadow-[0px_1px_0_0_#004E71]"
            />
        </div>
</template>

<script setup>
import { ref } from 'vue';
    const searchQuery = ref("");
</script>