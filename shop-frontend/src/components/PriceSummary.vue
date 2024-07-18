<template>
    <div class="flex flex-col justify-center mx-4 border-slate-950">
        <div class="flex justify-start">
            <Text bold class="text-xl">
                Summary:
            </Text>
        </div>

        <div class="flex justify-start">
            <Text bold>
                SubTotal:
            </Text>
            <div class=" flex grow"></div>
            <Text >
                {{ subTotal }}USD
            </Text>
        </div>
        <div class="flex justify-start">
            <Text bold>
                Shipping fee:
            </Text>
            <div class=" flex grow"></div>
            <Text >
                {{ shippingFee }} USD
            </Text>
        </div>
        <div v-if="saved" class="flex justify-start">
            <Text bold>
                Saved:
            </Text>
            <div  class=" flex grow"></div>
            <Text >
                {{ saved }} USD
            </Text>
        </div>
        <div class="flex justify-start">
            <Text bold>
                Total:
            </Text>
            <div class=" flex grow"></div>
            <Text >
                {{ total }} USD
            </Text>
        </div>
        <div class="flex justify-center">
            <CButton round-borders @click="placeOrder" text="Checkout" class=" min-w-32"></CButton>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import CButton from './CButton.vue';
import Text from './Text.vue'
import useAxios from "@/composables/useAPI";
import { useCartStore } from '@/stores/CartStore';
const api = useAxios();
const router = useRouter();
const cartStore = useCartStore();
const props = defineProps({
    total: Number,
    subTotal: Number,
    saved: Number,
    shippingFee: Number,

});

async function placeOrder()
{
  await api.post("/orders/" + localStorage.userId)
  cartStore.updateCart();
  if (!api.error){
    // let uid = localStorage.userId
    router.push({name: "orders"});
  }
}

</script>