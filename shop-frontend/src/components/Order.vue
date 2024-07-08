<template>
<Text bold class=" text-xl"> Order ID: {{ id }}</Text>
<Text bold class=" text-xl"> Paid: {{ Paid }}</Text>
<Text bold class=" text-xl"> Address: {{ shippingAddress }}</Text>
<Text bold class=" text-xl"> Status: {{ status }}</Text>
<Text bold class=" text-xl"> shippingCost: {{ shippingCost }}</Text>
<Text bold class=" text-xl"> total: {{ totalCost }}</Text>


</template>

<script setup>
import CartItemCard from './CartItemCard.vue'
import Text from './Text.vue';

const props = defineProps({
    id: Number,
    Paid: Boolean,
    shippingAddress: String,
    shppingCost: Number,
    totalCost: Number,
    status: String,

})
</script>
