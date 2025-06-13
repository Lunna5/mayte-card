import { useContext } from 'react';
import { AuthContext, type AuthContextType } from '@/contexts/AuthContext.ts';

/**
 * Custom hook to access authentication context.
 *
 * @returns {AuthContextType} The authentication context value.
 */
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }

  return context;
};
