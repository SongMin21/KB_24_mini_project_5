import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL, // proxy prefix(/api)와 일치
  headers: { 'Content-Type': 'application/json' },
})

export default api
