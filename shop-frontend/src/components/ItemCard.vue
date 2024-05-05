<template>
    <div
        class="relative m-10 flex w-full max-w-xs flex-col overflow-hidden rounded-lg border border-gray-100 bg-white shadow-md">
        <a class="relative mx-3 mt-3 flex h-60 overflow-hidden rounded-xl" href="#">
            <img class="object-cover"
                :src="imageUrl"
                alt="product image" />
            <span v-if="discount"
                class="absolute top-0 left-0 m-2 rounded-full bg-black px-2 text-center text-sm font-medium text-white">39%
                OFF</span>
        </a>
        <div class="mt-4 px-5 pb-5">
            <a href="#" class="mb-62">
                <h5 class="text-xl tracking-tight text-slate-900">{{ name }}</h5>
            </a>

            <div class="mt-2 mb-5 flex items-center ">
                <p class="line-clamp-3">
                    asdjlkasdjklasjdjsad
                    asdkjlsajdlsakdjlaskdjlaskdlsakdj
                    sajldjaslkd
                    jasldkasjdaslk
                    ajlskdjklasdjlasjd
                    jaslkdjlaskd
                    {{ descriprion }}
                </p>
            </div>
            <p class="text-center">
                <span class="text-3xl font-bold text-slate-900">${{ price }}</span>
                <span v-if="discount" class="text-sm text-slate-900 line-through">$699</span>
            </p>
            <div class="flex flex-row justify-center mb-2 flex-wrap">
                <button @click="subQuantity()" class="bg-slate-900 text-white rounded "> <i class=" fa-xl fa-solid fa-minus" style="color: #ffffff;"></i></button>
                <input type="text" class="max-w-10 text-center" v-model="quantity"> {{ quantity }}</input>
                <button @click="addQuantity()" class="bg-slate-900 text-white rounded "> <i class=" fa-xl fa-solid fa-plus" style="color: #ffffff;"></i></button>
            </div>
            <div class="flex flex-row justify-center">
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
</template>

<script setup>

const quantity = defineModel({default:0});
function addQuantity() {
    if (availableQuantity) {
        if (quantity.value < availableQuantity) {
            quantity.value++;
        }
    }
}
function subQuantity() {
    if (availableQuantity) {
        if (quantity.value > 0) {
            quantity--;
        }
    }
}

defineProps(
    {
        imageUrl:{
            type: String,
            default:"https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"
        },
        name: String,
        price: Number,
        descriprion: String,
        availableQuantity: Number,
        discount:Boolean
    }
);

</script>