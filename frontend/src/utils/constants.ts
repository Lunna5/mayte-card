export const isMobile = /iPhone|iPad|iPod|Android/i.test(navigator.userAgent);
export const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';