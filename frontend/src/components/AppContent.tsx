import { useAuth } from '@/hooks/auth/useAuth';
import { GlobalLoader } from './GlobalLoader';
import { RouterProvider } from 'react-router';
import { router } from '@/router';
import { useEffect, useState } from 'react';
import { AppContainer } from '@/app';

export const AppContent = () => {
  const { loading } = useAuth();
  const [showLoader, setShowLoader] = useState(true);

  useEffect(() => {
    if (loading) {
      setShowLoader(true);
    }
  }, [loading]);

  return (
    <>
      {showLoader && (
        <GlobalLoader
          loading={loading}
          onFinish={() => {
            if (!loading) {
              setShowLoader(false);
            }
          }}
        />
      )}
      <AppContainer>
        <RouterProvider router={router} />
      </AppContainer>
    </>
  );
};
