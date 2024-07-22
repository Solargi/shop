<template>
<Text bold class=" text-xl"> Order ID: {{ id }}</Text>
<Text bold class=" text-xl"> Paid: {{ Paid }}</Text>
<Text bold class=" text-xl"> User ID: {{ user.id }}</Text>
<Text bold class=" text-xl"> Shipped To: {{ user.name}} {{ user.surname }}</Text>
<Text bold class=" text-xl"> Address: {{ shippingAddress.country }} {{ shippingAddress.city}} {{ shippingAddress.street}} {{ shippingAddress.zipCode}}</Text>
<Text bold class=" text-xl"> Status: {{ status }}</Text>
<Text bold class=" text-xl"> shippingCost: {{ shippingCost }} {{ currency }}</Text>
<Text bold class=" text-xl"> total: {{ totalCost }} {{ currency }}</Text>


</template>

<script setup>
import CartItemCard from './CartItemCard.vue'
import Text from './Text.vue';
const totalCost = defineModel('totalCost')
const shippingCost =defineModel('shippingCost')
const currency = defineModel({default:"USD"},'currency')
const props = defineProps({
    id: Number,
    Paid: Boolean,
    shippingAddress: String,
    status: String,
    user: String,
})
</script>
