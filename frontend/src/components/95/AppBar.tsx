import { useAuth } from '@/hooks/auth/useAuth';
import { useState } from 'react';
import { AppBar, Button, MenuList, MenuListItem, Toolbar } from 'react95';
import styled from 'styled-components';

const CustomAppBar = styled(AppBar)`
  position: relative;
`;

export const AccountAppBar = () => {
  const [open, setOpen] = useState(false);
  const { user, logout } = useAuth();
  return (
    <CustomAppBar className='z-50'>
      <Toolbar>
        <div style={{ position: 'relative', display: 'inline-block' }}>
          <Button onClick={() => setOpen(!open)} active={open} style={{ fontWeight: 'bold' }}>
            {user?.username || user?.email || 'Account'}
          </Button>
          {open && (
            <MenuList
              style={{
                position: 'absolute',
                left: '0',
                top: '100%',
              }}
              onClick={() => setOpen(false)}
            >
              <MenuListItem onClick={() => logout()}>
                <span role='img' aria-label='🔙'>
                  🔙
                </span>
                Cerrar Sesión
              </MenuListItem>
            </MenuList>
          )}
        </div>
      </Toolbar>
    </CustomAppBar>
  );
};
