import api from '@/api/client';

export interface User {
  id: string;
  email: string;
  username: string;
}

export const fetchCurrentUser = async () => {
  return await api.get<User>('/api/v1/auth/me');
};
