<template>
  <div class="dark:bg-black">
    <div class="flex items-start flex-wrap justify-center">
      <div class="flex-col">
        <div class="" v-for="(cartItem, index) in getItems.data">
          <cartItemCard class="max-w-7xl"
            :id="cartItem.id"
            :itemId="cartItem.id.itemId"
            :name="cartItem.itemDTO.name"
            v-model:price="cartItem.totalCost"
            :imageUrl="cartItem.itemDTO.imageUrl"
            v-model:quantity="cartItem.quantity"
            :availableQuantity="cartItem.itemDTO.availableQuantity"
            @remove="removeItem(cartItem.id)"
          ></cartItemCard>
          <!-- <p>{{ cartItem.id.itemId }}</p> -->
        </div>
      </div>
      <PriceSummary :subTotal="subTotal" :shippingFee="shippingFee" :total="subTotal + shippingFee" class="min-w-80 mt-3">
      </PriceSummary>
    </div>
  </div>
  <CText bold></CText>
</template>

<script setup>
import CText from "@/components/Text.vue";
import cartItemCard from "@/components/CartItemCard.vue";
import useAxios from "@/composables/useAPI";
import { useAuthStore } from "@/stores/AuthStore";
import { useCartStore } from "@/stores/CartStore"
import PriceSummary from "@/components/PriceSummary.vue";
import { computed, ref } from "vue";
const authStore = useAuthStore();
const cartStore = useCartStore();
const getItems = useAxios();
const shippingFee = 10
const subTotal = computed(()=>{
  if (getItems.data){
    return getItems.data.reduce((sum, cartItem)=> 
    sum+cartItem.totalCost, 0)
  }
})
getItems.get("/cartItems/" + authStore.userId, {
  "Content-Type": "application/json",
  Accept: "application/json",
  withCredentials: true,
});

async function removeItem(cartItemId){
  console.log(cartItemId)
  // delete request already done in CartItemCart, here we just need to update
  // the collection to update v-for loop list
  //await getItems.delete("/cartItems/" + cartItemId.userId + "/" + cartItemId.itemId )
  // await getItems.delete("/cartItems/" + cartItemId.userId + "/" + cartItemId.itemId )
  await getItems.get("/cartItems/" + authStore.userId, {
  "Content-Type": "application/json",
  Accept: "application/json",
  withCredentials: true,
});
cartStore.updateCart(); 
}


</script>
