import api from '@/api/client';

export interface LoginResponse {
  token: string;
}

export interface OtpRequestResponse {
  valid: boolean;
  username: string;
  email: string;
  newAccount: boolean;
}

export const login = async (email: string, otpCode: string) => {
  return await api.post<LoginResponse>('/api/v1/auth/login', { email, otpCode });
};

export const requestOtp = async (email: string) => {
  return await api.post<OtpRequestResponse>('/api/v1/auth/request-otp', { email });
};