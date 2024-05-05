/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: ["./src/**/*.{html,vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors:{
        primary: '#59981A',
        secondary: {

        }
      }
    },
    fontFamily: {
      roboto: ['Roboto','sans-serif']
    }
  },
  plugins: [],
  
}

