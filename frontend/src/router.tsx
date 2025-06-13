import { createBrowserRouter } from 'react-router';
import App from './app/index.tsx';
import { LoginPage } from './app/login.tsx';
import { AuthenticatedLayout, LoginLayout } from './layouts/AuthLayouts.tsx';

export const router = createBrowserRouter([
  {
    element: <LoginLayout />,
    children: [
      {
        path: '/login',
        Component: LoginPage,
      }
    ]
  },
  {
    element: <AuthenticatedLayout />,
    children: [
      {
        path: '/',
        Component: App,
      },
    ]
  }
]);
