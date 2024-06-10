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
        <div class="flex justify-start">
            <Text bold>
                Saved:
            </Text>
            <div v-if="saved" class=" flex grow"></div>
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
            <CButton round-borders text="Checkout" class=" min-w-32"></CButton>
        </div>
    </div>
</template>

<script setup>
import CButton from './CButton.vue';
import Text from './Text.vue'

const props = defineProps({
    total: Number,
    subTotal: Number,
    saved: Number,
    shippingFee: Number,

});

</script>