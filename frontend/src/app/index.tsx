import { CardFromContext } from '@/components/Card';
import { CardForm } from '@/components/CardForm';
import { CardProvider } from '@/contexts/CardProvider';
import styled from 'styled-components';

export const AppContainer = styled.div`
  background: ${({ theme }) => theme.desktopBackground};   // @ts-ignore this is a workaround for the theme not being recognized
  min-height: 100vh;
`;

const App = () => {
  return (
    <AppContainer>
      <CardProvider>
        <CardForm />
        <CardFromContext default={{ x: 120, y: 120, width: 400, height: 560 }}/>
      </CardProvider>
    </AppContainer>
  );
};

export default App;
