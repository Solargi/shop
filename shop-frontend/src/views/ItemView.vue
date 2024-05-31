<template>
  <!-- <p>{{item}} </p> -->
  <div v-if="item">
    <div class="flex justify-center flex-row flex-wrap">
      <div class="flex, flex-col, justify-start flex-warp">
        <div
          class="relative mx-3 mt-3 flex-warp flex h-96 overflow-hidden justify-start rounded-xl cursor-pointer max-w-xl"
        >
          <img
            class="object-cover rounded-xl border-black"
            :src="item.imageUrl"
            alt="product image"
          />
          <span
            v-if="item.discount"
            class="absolute top-0 left-0 m-2 rounded-full bg-black px-2 text-center text-sm font-medium text-white"
            >39% OFF</span
          >
        </div>
        
      </div>
      <div class="flex flex-col flex-wrap max-w-xl">
            <h1>Product: {{ item.name }}</h1>
            <p>Description:</p>
            <p>{{ item.description }}</p>
            <p>{{ item.price }} CHF</p>
            <div class="mb-2">
                <button @click="subQuantity()" class="bg-slate-900 text-white rounded "> <i class=" fa-xl fa-solid fa-minus" style="color: #ffffff;"></i></button>
                <input @keydown="keysOnlyNumbers" @paste="pasteOnlyNumbers" inputmode="numeric" type="text" class="max-w-10 text-center" v-model.number="quantity"> </input>
                <button @click="addQuantity()" class="bg-slate-900 text-white rounded "> <i class=" fa-xl fa-solid fa-plus" style="color: #ffffff;"></i></button>
            </div>
            <div>
                <button @click="$emit('addItemToChart')" href="#"
                    class="flex items-center justify-center rounded-md bg-slate-900 px-5 py-2.5 text-center text-sm font-medium text-white hover:bg-gray-700 focus:outline-none focus:ring-4 focus:ring-blue-300">
                    <svg xmlns="http://www.w3.org/2000/svg" class="mr-2 h-6 w-6" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round"
                            d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                    </svg>
                    Add to cart</button>
            </div>
          </div>
    </div>
    <!-- other item details -->
  </div>
  <div v-else>
    <p>Loading...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import axios from "axios";
let quantity = defineModel({default:0});
const route = useRoute();
const item = ref(null);

const fetchItem = async (id) => {
  try {
    const response = await axios.get(`/items/${id}`);
    item.value = response.data;
  } catch (error) {
    console.error("Error fetching item:", error);
  }
};

onMounted(() => {
  fetchItem(route.params.id);
});

watch(
  () => route.params.id,
  (newId) => {
    fetchItem(newId);
  }
);


function addQuantity() {
    if (item) {
      console.log(quantity.value, item.value.availableQuantity)
        if (quantity.value < item.value.availableQuantity) {
            quantity.value++;
        }
    }
}
function subQuantity() {
    if (item) {
        if (quantity.value > 0) {
            quantity.value--;
        }
    }
}

//allows only numbers to be inserted
function keysOnlyNumbers(event){
       // Allow: backspace, delete, tab, escape, enter, and .
       if ([46, 8, 9, 27, 13, 110].includes(event.keyCode) ||
          // Allow: Ctrl+A
          (event.keyCode === 65 && event.ctrlKey === true) ||
          // Allow: Ctrl+C
          (event.keyCode === 67 && event.ctrlKey === true) ||
          // Allow: Ctrl+X
          (event.keyCode === 88 && event.ctrlKey === true) ||
          // Allow: Ctrl+V
          (event.keyCode === 86 && event.ctrlKey === true) ||
          // Allow: home, end, left, right
          (event.keyCode >= 35 && event.keyCode <= 39)) {
        // Let it happen, don't do anything
        return;
      }

      // Prevent non-numeric input
      if ((event.shiftKey || (event.keyCode < 48 || event.keyCode > 57)) && (event.keyCode < 96 || event.keyCode > 105)) {
        event.preventDefault();
      }
}
//allows only numbers to be pasted if there is a character that is not a number
//the paste action is prevented
function pasteOnlyNumbers(event) {
    const pastedText = (event.clipboardData || window.clipboardData).getData('text');
    if (!/^\d+$/.test(pastedText)) {
        event.preventDefault();
    }

}
</script>
