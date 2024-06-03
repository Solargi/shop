<template>
    <div class="dark:bg-black">
  <CText>test</CText>
  <CText bold>{{ getItems.data }}</CText>
  <cartItemCard></cartItemCard>
</div>
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