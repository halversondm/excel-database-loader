import {defineConfig} from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(() => {
    return {
        build: {
            outDir: 'target/classes/static',
        },
        plugins: [react()],
        test: {
            environment: 'jsdom',
            globals: true,
            setupFiles: 'src/test/javascript/setupTests.js'
        }
    };
});