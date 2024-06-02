<template>
  <div class="flex flex-warp justify-center">
    <div
      class="relative m-10 flex w-full max-w-2xl flexrow overflow-hidden justify-items-center rounded-lg border-2 border-black bg-white dark:bg-green-600 shadow-md shadow-black"
    >
      <CartItemImage id="1" :imageUrl="imageUrl"> </CartItemImage>
      <button class="absolute right-0 mr-2">x</button>
      <p class="mt-5">dkslalkldsak dhashdgjhsg hjsdgjagd ahjs jhasgd hjsg dhjasg dhjsga djhsgdhjsgd jhal</p>
      <p> sdjkalkdjaslk kjasdlkasjdklasj </p>
    </div>
  </div>
</template>
<script setup>
import CText from "./Text.vue";
import CartItemImage from "./CartItemImage.vue";
const props = defineProps({
  imageUrl: {
    type: String,
    default:
      "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg",
  },
});
</script>
