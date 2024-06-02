<template>
  
    <a
      @click="gotoItem(id)"
      class="relative mx-3 mt-3 mb-3 flex min-h-28 min-w-28 max-h-40 max-w-40 overflow-hidden rounded-xl cursor-pointer"
    >
      <img
        class="object-cover rounded-xl border-black"
        :src="imageUrl"
        alt="product image"
      />
      <span
        v-if="discount"
        class="absolute top-0 left-0 m-2 rounded-full bg-black px-2 text-center text-sm font-medium text-white"
        >39% OFF</span
      >
    </a>
    <div class="mt-4 px-5 pb-5"></div>
</template>
<script setup>
const props = defineProps({
  id: String,
  imageUrl: String,
  discount: String,
});
</script>