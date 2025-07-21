// src/services/axios.ts
import axios from "axios";

const backend_Path = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_URL, // .env file
});

export default backend_Path;
