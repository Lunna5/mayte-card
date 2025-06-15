import { AccountAppBar } from '@/components/95/AppBar';
import { useAuth } from '@/hooks/auth/useAuth';
import { Navigate, Outlet } from 'react-router';

export const LoginLayout = () => {
  const { user } = useAuth();
  if (user) {
    return <Navigate to='/' replace />;
  }

  return <Outlet />;
};

export const AuthenticatedLayout = () => {
  const { user } = useAuth();
  if (!user) {
    return <Navigate to='/login' replace />;
  }

  return (
    <>
      <AccountAppBar />
      <Outlet />
    </>
  );
};
