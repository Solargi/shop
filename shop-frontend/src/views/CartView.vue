<template>
  <div class="dark:bg-black">

  <CText>test</CText>
  
  <cartItemCard></cartItemCard>
    <div v-for="(cartItem, index) in getItems.data" >
      <cartItemCard
      :id="cartItem.id"
        :name="cartItem.itemDTO.name"
        v-model:price="cartItem.totalCost"
        :imageUrl="cartItem.itemDTO.imageUrl"
        v-model:quantity="cartItem.quantity"
        :availableQuantity="cartItem.itemDTO.availableQuantity"
        @remove = "getItems.data.splice(index, 1)"
      ></cartItemCard>
      <p>{{ cartItem}}</p>
    </div>
  </div>
<CText bold></CText>
</template>
  
<script setup>
import CText from '@/components/Text.vue';
import cartItemCard from '@/components/CartItemCard.vue'
import useAxios from "@/composables/useAPI"
import { useAuthStore } from '@/stores/AuthStore';
const authStore = useAuthStore();
const getItems = useAxios();
getItems.get("/cartItems/" + authStore.userId,{
        "Content-Type": "application/json",
          Accept: "application/json",
          withCredentials: true,
      });
</script>