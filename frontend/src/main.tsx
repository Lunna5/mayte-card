import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';

import './App.css';

import { createGlobalStyle, ThemeProvider } from 'styled-components';
import candy from 'react95/dist/themes/candy';

/* Original Windows95 font (optional) */
import ms_sans_serif from '@/assets/fonts/MSW98UI-Regular.ttf';
import ms_sans_serif_bold from '@/assets/fonts/MSW98UI-Bold.ttf';
import { AuthProvider } from './contexts/AuthProvider.tsx';
import { AppContent } from './components/AppContent.tsx';
import { LoadingProvider } from './contexts/LoadingProvider.tsx';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const GlobalStyles = createGlobalStyle`
  @font-face {
    font-family: 'ms_sans_serif';
    src: url('${ms_sans_serif}') format('woff2');
    font-weight: 400;
    font-style: normal
  }

  @font-face {
    font-family: 'ms_sans_serif';
    src: url('${ms_sans_serif_bold}') format('woff2');
    font-weight: bold;
    font-style: normal
  }

  body, input, select, textarea {
    font-family: 'ms_sans_serif', serif;
  }
`;

const queryClient = new QueryClient();

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <GlobalStyles />
    <LoadingProvider>
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <ThemeProvider theme={candy}>
            <AppContent />
          </ThemeProvider>
        </AuthProvider>
      </QueryClientProvider>
    </LoadingProvider>
  </StrictMode>
);
