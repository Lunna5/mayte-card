import { login, type LoginResponse } from '@/api/auth/login';
import { useMutation } from '@tanstack/react-query';

export const useLogin = () => {
  return useMutation<LoginResponse, unknown, { email: string; otpCode: string }>({
    mutationFn: ({ email, otpCode }) => login(email, otpCode).then((res) => res.data),
  });
};
