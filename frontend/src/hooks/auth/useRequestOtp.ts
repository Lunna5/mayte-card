import { requestOtp, type OtpRequestResponse } from '@/api/auth/login';
import { useMutation, type UseMutationResult } from '@tanstack/react-query';

export const useRequestOtp = (): UseMutationResult<OtpRequestResponse, unknown, string, unknown> => {
  return useMutation<OtpRequestResponse, unknown, string>({
    mutationFn: (email) => requestOtp(email).then((res) => res.data),
  });
};
