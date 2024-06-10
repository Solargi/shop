<template>
    <p>{{ api.data}}</p>
</template>
  
<script setup>
import useAxios from "@/composables/useAPI";
const api = useAxios();

api.get("/orders")

</script>