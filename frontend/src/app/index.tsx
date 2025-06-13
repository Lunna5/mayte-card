import Card from '@/components/Card.tsx';
import styled from 'styled-components';

export const AppContainer = styled.div`
  background: ${({ theme }) => theme.desktopBackground};
  min-height: 100vh;
`;

const App = () => {
  return (
    <AppContainer>
      <Card />
    </AppContainer>
  );
};

export default App;
