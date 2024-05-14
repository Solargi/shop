import { defineStore } from "pinia";
//needed sto store pinia data in local storage reactively:
import { useStorage } from "@vueuse/core"; 

export const useAuthStore = defineStore("authStore", {
  state: () => ({
    //this is not persistent if page is reloaded (no localstorage):
    // auth : null,
    //sto save to local storage reactively
    auth: useStorage("auth", null),//-> fucks up $resetfunction in pinia store
    user: null,
  }),
  getters: {
    getAuth: (state) => state.auth,
  },
  actions: {
    login(user) {
      this.auth = true;
      this.user = user;
    },
    logout() {
        // this.$reset()
        this.auth = null;
        this.$reset();
        // localStorage.removeItem('auth') //done like
    }
    },
  }
)