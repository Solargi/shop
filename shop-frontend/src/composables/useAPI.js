import axios from "@/axios-config";
import { reactive } from "vue";

export default function useAPI() {
    return reactive({
        processing: false,
        error: null,
        response:null,
        data: [],

        async sendRequest(method, url, data = null, headers={}) {
            if (this.processing) return;
            this.processing = true;
            this.data = [];
            this.response = null;
            this.error = null;
            console.log("data: " + data)

            try {
                this.response = await axios({
                    method,
                    url,
                    data,
                    headers
                });
                this.data = this.response.data;
            } catch (err) {
                this.error = err;
                if (err.response) {
                    // The request was made and the server responded with a status code
                    // that falls out of the range of 2xx
                    console.error('Status:', err.response.status);
                    console.error('Data:', err.response.data);
                    // this.error = err;
                  } else if (err.request) {
                    // The request was made but no response was received
                    console.error('No response received:', err.request);
                    //errorMessage.value = 'No response from server. Please try again later.';
                  } else {
                    // Something happened in setting up the request that triggered an Error
                    console.error('Error:', err.message);
                    //errorMessage.value = 'An unexpected error occurred. Please try again later.';
                  }
            } finally{
                this.processing = false;
            }
        },

        async get (url, headers = {}) {
            await this.sendRequest("get", url, null, headers)
        },
        async post (url, payload, headers = {}) {
            await this.sendRequest("post", url, payload, headers)
        },
        async put (url, payload, headers = {}) {
            console.log("composable put ")
            await this.sendRequest("put", url, payload, headers)
        },
        async delete (url, headers = {}) {
            await this.sendRequest("delete", url, null, headers)
        },
        })
        
    }