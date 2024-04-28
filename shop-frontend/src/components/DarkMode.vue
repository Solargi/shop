<script setup>
import {useDark, useToggle} from '@vueuse/core'
const isDark = useDark();
const toggleDark = useToggle(isDark);
</script>

<template>
    <p> isDark:{{ isDark }}</p>
    <button @click="toggleDark()" class="bg-green-500 rounded">
        Toggle Dark Mode
    </button>
</template>