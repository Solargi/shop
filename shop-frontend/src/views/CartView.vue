<template>
  <div class="dark:bg-black">

  <CText>test</CText>
  
  <cartItemCard></cartItemCard>
    <div v-for="cartItem in getItems.data" :key="cartItem.id">
      <cartItemCard
      :id="cartItem.id.itemId"
        :name="cartItem.itemDTO.name"
        :price="cartItem.totalCost"
        :imageUrl="cartItem.itemDTO.imageUrl"
        :availableQuantity="cartItem.quantity"
      ></cartItemCard>
    </div>
  </div>
<CText bold>{{ getItems.data}}</CText>
</template>
  
<script setup>
import CText from '@/components/Text.vue';
import cartItemCard from '@/components/CartItemCard.vue'
import useAxios from "@/composables/useAPI"
import { useAuthStore } from '@/stores/AuthStore';
import { onMounted } from 'vue';

const authStore = useAuthStore();
const getItems = useAxios();
getItems.get("/cartItems/" + authStore.userId,{
        "Content-Type": "application/json",
          Accept: "application/json",
          withCredentials: true,
      });

</script>