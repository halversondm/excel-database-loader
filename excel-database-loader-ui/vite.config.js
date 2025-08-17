import {defineConfig} from 'vite';
import react from '@vitejs/plugin-react';
//import eslint from 'vite-plugin-eslint';

export default defineConfig(() => {
    return {
        build: {
            outDir: 'build',
        },
        plugins: [react()],
        test: {
            environment: 'jsdom',
            globals: true,
            setupFiles: 'src/test/javascript/setupTests.js'
        }
    };
});