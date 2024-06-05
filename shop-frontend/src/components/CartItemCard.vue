
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
        <div class="flex place-items-center mx-4">
          <CButton class="" round text="+"></CButton>
          <Field> <FormInput
          v-model:model-value="quantity"
          type="text"
        ></FormInput></Field>
          <button class="mr-2">plus Quantity {{ quantity }} minus trallallaAAAAA</button>
          <CButton round text="-"></CButton>
        </div>

      </div>
    </div>
  </div>
</template>
<script setup>
import CText from "./Text.vue";
import Field from "./Field.vue";
import FormInput from "./FormInput.vue";
import CartItemImage from "./CartItemImage.vue";
import CButton from "./CButton.vue";
import Text from "./Text.vue";
// tot parince + qunatity
const quantity = defineModel("quantity");
//use define model to pass the whole cartitem then cut it off in pieces for html
// do action listener to update cart using put/post requests and refresh data with answer
const props = defineProps({
  id: {
    type: Object},
  name: String,
  price: Number,
  imageUrl: {
    type: String,
    default:
      "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg",
  },
});
</script>
