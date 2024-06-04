<template>
    <button :class="['bg-button-light-600 dark:bg-button-dark-600',{ 'rounded-md':roundBorders, 'rounded-full':round, }]"> <Text>{{ props.text }}</Text> </button>
</template>

<script setup>
import Text from "./Text.vue";


const props = defineProps({
    text: String,
    roundBorders : Boolean,
    round : Boolean,

})

</script>