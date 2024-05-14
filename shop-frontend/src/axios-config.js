import axios from "axios";

//set default url
const baseURL = import.meta.env.VITE_API_URL;
axios.defaults.baseURL = baseURL;
axios.defaults.withCredentials = true;

//request interceptor to redirict to login page if answer is 401
axios.interceptors.response.use(
    //if respose is received with no error then return it
    function(response){
        return response
    },
    function(error){
        //in case of error in response:
        if(error.response && error.response.status == 401){
            //redirict to login page
            window.location.href = "/login";
        }
        //else pass error to next handler
        return Promise.reject(error)
    }
)
export default axios;