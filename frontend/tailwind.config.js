/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        'nav-gradient-start': '#1a2a6c',
        'nav-gradient-middle': '#b21f1f',
        'nav-gradient-end': '#fdbb2d'
      }
    }
  },
  plugins: []
}
