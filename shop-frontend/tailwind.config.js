/** @type {import('tailwindcss').Config} */
const colors = require('tailwindcss/colors')
export default {
  darkMode: 'class',
  content: ["./src/**/*.{html,vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors:{
        text: {
          light: colors.black,
          dark: colors.red,
        },
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

