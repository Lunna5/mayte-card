import { useEffect, useState, type ReactNode } from 'react';
import { type LoginResponse } from '@/api/auth/login';
import { fetchCurrentUser, type User } from '@/api/auth/user';
import { AuthContext } from './AuthContext';
import oopsify from '@/utils/error';
import { useLoading } from '@/hooks/useLoading';
import { useCallback } from 'react';

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [user, setUser] = useState<User | null>(null);
  const { loading, setLoading } = useLoading();

  const fetchUser = useCallback(async () => {
    setLoading(true);
    try {
      const [error, user] = await oopsify(fetchCurrentUser());

      if (error) {
        console.error('Failed to fetch current user:', error);
        setUser(null);
      } else {
        setUser(user?.data || null);
      }
    } catch (error) {
      console.error('An unexpected error occurred while fetching user:', error);
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, [setLoading]);

  useEffect(() => {
    if (localStorage.getItem('jwt')) {
      fetchUser();
    }
  }, [fetchUser]);

  const login = async (loginResponse: LoginResponse) => {
    localStorage.setItem('jwt', loginResponse.token);
    fetchUser();
  };

  const logout = () => {
    localStorage.removeItem('jwt');
    setUser(null);
  };

  return <AuthContext.Provider value={{ user, login, logout, loading }}>{children}</AuthContext.Provider>;
};
