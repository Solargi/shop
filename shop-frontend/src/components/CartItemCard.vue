<template>
  <div class="flex justify-center">
    <div
      class="relative m-10 flex w-full flex-wrap  max-w-full  overflow-hidden justify-items-center rounded-lg border-2 border-black bg-white dark:bg-green-600 shadow-md shadow-black"
    >
      <CartItemImage id="1" :imageUrl="imageUrl" > </CartItemImage>
      <button class="absolute right-0 mr-2">remove form cart</button>
      <div class="flex flex-col ">
        <Text bold> {{props.name}}</Text>
        <Text bold> USD: {{props.price}}</Text>
        <Text> BLABLABLA:</Text>
        <Text class="mt-5">dkslalkldsak dhashdgjhsg hjsdgjagd ahjs jhasgd hjsg dhjasg dhjsga djhsgdhjsgd jhal</Text>
        <Text> sdjkalkdjaslk kjasdlkasjdklasj </Text>
      </div>
      <div class="flex grow justify-end">
        <button class="mr-2">plus Quantity minus trallallaAAAAA</button>
      </div>
    </div>
  </div>
</template>
<script setup>
import CText from "./Text.vue";
import CartItemImage from "./CartItemImage.vue";
import Text from "./Text.vue";
const props = defineProps({
  id: Number,
  name: String,
  price: Number,
  availableQuantity: Number,
  imageUrl: {
    type: String,
    default:
      "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg",
  },
});
</script>
