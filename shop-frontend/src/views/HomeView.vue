<script>
import ItemCard from '@/components/ItemCard.vue';
import axios from '../axios-config'

export default {
  components: {
    ItemCard
  },
  data() {
    return {
      items: [] // Corrected the declaration of items in data
    };
  },
  mounted() {
    const baseUrl = import.meta.env.VITE_API_URL; // Moved baseUrl inside mounted
    axios.get("/items").then(response => {
      console.log(response.data); // Changed index to 0, assuming you want to log the first item's name
      response.data.forEach(item => {
        console.log(item.name); // Corrected the way of accessing item.name
      });
      this.items = response.data;
    }).catch(error => {
      console.error('Error fetching items:', error);
    });
  }
}
</script>

<template>
  <main>
    <div class="flex flex-wrap flex-col sm:flex-row items-center">
      <div v-for="item in items" :key="item.id">
      <ItemCard
        :id="item.id"
        :name="item.name"
        :description="item.description"
        :price="item.price"
        :imageUrl="item.imageUrl"
        :availableQuantity="item.availableQuantity"
        :discount="item.discount"
        @addItemToCart="addItemToCart(item)"
      />
    </div>
    </div>
  </main>
</template>
