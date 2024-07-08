import { defineStore } from "pinia";
//needed sto store pinia data in local storage reactively:
import { useStorage } from "@vueuse/core"; 
import useAPI from "../composables/useAPI"
const api = useAPI();


export const useCartStore = defineStore("cartStore", {
  state: () => ({
    //this is not persistent if page is reloaded (no localstorage):
    // auth : null,
    //sto save to local storage reactively
    cartLegnth: useStorage("cartLenght", localStorage.cartLegnth),
  }),
  actions: {
    async updateCart() {
        console.log("auth store before requesting")
        await api.get("/cartItems/" + localStorage.userId)
        console.log("from store: " + api.data)
        // if (!api.error && api.data && api.data.length !==0){
          this.cartLegnth = api.data.length
          console.log("length :" + api.data.length);
        // }
    },
    },
  }
)