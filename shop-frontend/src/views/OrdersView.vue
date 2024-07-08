<template>
    <p>{{ api.data}}</p>
    <Order></Order>
    <div class="" v-for="(order, index) in api.data">
          <Order class="max-w-7xl"
            :id="order.id"
            :paid="order.paid"
            v-model:cost="order.totalCost"
            :shippingAddress="order.shippingAddress"
            v-model:shippingCost="order.shippingCOst"
            :status="order.status"
            @remove="api.data.splice(index, 1)"
          ></Order>
          <!-- <p>{{ order }}</p> -->
        </div>
</template>
id: Number,
Paid: Boolean,
shippingAddress: String,
shppingCost: Number,
totalCost: Number,
status: String,
<script setup>
import useAxios from "@/composables/useAPI";
import Order from "@/components/Order.vue"
const api = useAxios();

api.get("/orders/1")


</script>