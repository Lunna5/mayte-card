import { useEffect, useState, type ReactNode } from 'react';
import { LoadingContext } from './LoadingContext';
import { registerSetLoading } from '@/api/loadingManager';

export const LoadingProvider = ({ children }: { children: ReactNode }) => {
  useEffect(() => {
    registerSetLoading(setLoading);
  }, []);

  const [loading, setLoading] = useState(false);

  return <LoadingContext.Provider value={{ loading, setLoading }}>{children}</LoadingContext.Provider>;
};
