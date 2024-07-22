<template>
    <div class="" v-for="(order, index) in api.data">
          <Order class="max-w-7xl"
            :id="order.id"
            :paid="order.paid"
            :user="order.user"
            v-model:totalCost="order.totalCost"
            :shippingAddress="order.shippingAddress"
            v-model:shippingCost="order.shippingCost"
            :status="order.status"
            @remove="api.data.splice(index, 1)"
          ></Order>
          <!-- <p>{{ order }}</p> -->
        </div>
</template>

<script setup>
import useAxios from "@/composables/useAPI";
import Order from "@/components/Order.vue"
const api = useAxios();

api.get("/orders/user/" + localStorage.userId)


</script>