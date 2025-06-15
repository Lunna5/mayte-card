import { WindowWrapper } from '@/components/95/WindowWrapper';
import { ErrorSpan } from '@/components/ErrorSpan';
import { useAuth } from '@/hooks/auth/useAuth';
import { useState, type SetStateAction, type FormEvent } from 'react';
import { Button, TextInput, Window, WindowContent, WindowHeader } from 'react95';
import { useRequestOtp } from '@/hooks/auth/useRequestOtp';
import { errorToRecord } from '@/utils/error';
import { useLogin } from '@/hooks/auth/useLogin';

export const LoginPage = () => {
  return (
    <div className='flex flex-col items-center justify-center h-screen'>
      <LoginForm />
    </div>
  );
};

export const LoginForm = () => {
  const { login } = useAuth();
  const [email, setEmail] = useState('');
  const [showOtpInput, setShowOtpInput] = useState(false);
  const [otpCode, setOtpCode] = useState('');
  const [error, setError] = useState<Record<string, string>>({});

  const { mutate: requestOtpMutate, isPending: loading } = useRequestOtp();
  const { mutate: requestLoginMutate, isPending: loginLoading } = useLogin();

  const handleEmailForm = (e: FormEvent) => {
    e.preventDefault();
    setError({});

    requestOtpMutate(email, {
      onSuccess: () => {
        setShowOtpInput(true);
      },

      onError: (error: unknown) => {
        setError(errorToRecord(error));
      },
    });
  };

  const handleOtpForm = async (e: FormEvent) => {
    e.preventDefault();
    setError({});

    requestLoginMutate(
      { email, otpCode },
      {
        onSuccess: async (data) => {
          login(data);
        },
        onError: (error: unknown) => {
          setError(errorToRecord(error));
        },
      },
    );
  };

  return (
    <WindowWrapper>
      <Window className='window'>
        <WindowHeader className='window-title'>
          <span>Iniciar sesión</span>
          <Button>
            <span className='close-icon' />
          </Button>
        </WindowHeader>
        <WindowContent>
          <h1>Introduce tu correo @g.educaand.es</h1>
          <ErrorSpan error={error.message} />
          <ErrorSpan error={error.email} />
          <div className='flex flex-row gap-4 w-full p-2'>
            <TextInput
              value={email}
              placeholder='tucorreo123@g.educaand.es'
              onChange={(e: { target: { value: SetStateAction<string> } }) => setEmail(e.target.value)}
              fullWidth
            />

            <Button onClick={handleEmailForm} disabled={loading || showOtpInput}>
              Siguiente
            </Button>
          </div>

          <ErrorSpan error={error.otpCode} />
          {showOtpInput && (
            <form onSubmit={handleOtpForm} className='flex flex-row gap-4 w-full p-2'>
              <TextInput
                value={otpCode}
                placeholder='Introduce el OTP'
                onChange={(e: { target: { value: SetStateAction<string> } }) => setOtpCode(e.target.value)}
                fullWidth
              />
              <Button style={{ width: '200px' }} type='submit' disabled={loginLoading}>
                Iniciar sesión
              </Button>
            </form>
          )}
        </WindowContent>
      </Window>
    </WindowWrapper>
  );
};
