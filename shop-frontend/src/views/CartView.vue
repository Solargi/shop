<template>
  <div class="dark:bg-black">

  <CText>test</CText>
  
  <cartItemCard></cartItemCard>
    <div v-for="cartItem in getItems.data" >
      <cartItemCard
      :id="cartItem.id"
        :name="cartItem.itemDTO.name"
        :price="cartItem.totalCost"
        :imageUrl="cartItem.itemDTO.imageUrl"
        v-model:quantity="cartItem.quantity"
      ></cartItemCard>
    </div>
  </div>
<CText bold></CText>
</template>
  
<script setup>
import CText from '@/components/Text.vue';
import cartItemCard from '@/components/CartItemCard.vue'
import useAxios from "@/composables/useAPI"
import { useAuthStore } from '@/stores/AuthStore';
import { ref } from 'vue';
const test = ref(5);
const authStore = useAuthStore();
const getItems = useAxios();
getItems.get("/cartItems/" + authStore.userId,{
        "Content-Type": "application/json",
          Accept: "application/json",
          withCredentials: true,
      });
</script>