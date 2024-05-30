import { defineStore } from "pinia";
//needed sto store pinia data in local storage reactively:
import { useStorage } from "@vueuse/core"; 
import axios from "../axios-config";

export const useAuthStore = defineStore("authStore", {
  state: () => ({
    //this is not persistent if page is reloaded (no localstorage):
    // auth : null,
    //sto save to local storage reactively
    auth: useStorage("auth", null),//-> fucks up $resetfunction in pinia store
    userId: useStorage ("userId", null),
    roles: null,
  }),
  getters: {
    getAuth: (state) => state.auth 
  },
  actions: {
    login(user) {
      this.auth = true;
      this.userId = user.id;
      this.roles = user.roles;
      console.log("user " + user)
      // this.user = user; -> storing all user's data in localstroage not a good idea
    },

    logout() {
        this.auth = null;
        this.user = null;
        this.userId = null;
        this.role = null;
        axios.get('/users/logout');
        //log out axios call
        //  delete value in localstorage (vueuse):
        // this.auth = null;
        //reset value in pinia session storage
        // this.$reset(); -> using this will reset methods as well
    },
    },
  }
)