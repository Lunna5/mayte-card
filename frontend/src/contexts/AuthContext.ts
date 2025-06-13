import type { LoginResponse } from '@/api/auth/login';
import type { User } from '@/api/auth/user';
import { createContext } from 'react';

export interface AuthContextType {
  user: User | null;
  login: (loginResponse: LoginResponse) => void;
  logout: () => void;
  loading: boolean;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);
