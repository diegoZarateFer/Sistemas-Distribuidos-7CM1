import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { VitePWA } from 'vite-plugin-pwa'

export default defineConfig({
  plugins: [
    react(),
    VitePWA({
      registerType: 'autoUpdate',
      manifest: {
        name: 'Mi PWA con Vite',
        short_name: 'MiPWA',
        start_url: '/',
        display: 'standalone',
        background_color: '#ffffff',
        description: 'Una aplicación progresiva hecha con Vite y React',
        icons: [
          {
            src: '/icons/icon-192x192.png',
            sizes: '192x192',
            type: 'image/png',
          },
          {
            src: '/icons/icon-512x512.png',
            sizes: '512x512',
            type: 'image/png',
          }
        ],
      },
    }),
  ],
})
