<template>
    <p :class="[' text-text-light dark:text-text-dark-50 font-roboto',{ 'font-bold':bold, 'underline':underline, }]">
    <slot/></p>
</template>

<script setup>
    const props = defineProps({
        bold: Boolean,
        underline:Boolean,
    })

</script>