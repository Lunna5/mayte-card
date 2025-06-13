import { useEffect, useState } from 'react';
import { Frame, ProgressBar } from 'react95';
import styled from 'styled-components';

const Overlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.3);  // Fondo oscuro semi-transparente
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;  // Por encima de casi todo
`;

const LoaderBox = styled(Frame)`
    width: 300px;
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
`;

interface GlobalLoaderProps {
  loading: boolean;
  onFinish?: () => void;
}

export const GlobalLoader = ({ loading, onFinish }: GlobalLoaderProps) => {
  const [percent, setPercent] = useState(0);

  useEffect(() => {
    let timer: number | null = null;

    if (loading) {
      timer = window.setInterval(() => {
        setPercent(prev => {
          if (prev >= 90) return prev;
          return Math.min(prev + Math.random() * 5, 90);
        });
      }, 200);
    } else {
      setPercent(100);
    }

    return () => {
      if (timer) clearInterval(timer);
    };
  }, [loading]);

  useEffect(() => {
    if (percent === 100 && onFinish) {
      const timeout = setTimeout(() => {
        onFinish();
      }, 300);
      return () => clearTimeout(timeout);
    }
  }, [percent, onFinish]);

  return (
    <Overlay>
      <LoaderBox>
        <ProgressBar variant="tile" value={Math.floor(percent)} />
      </LoaderBox>
    </Overlay>
  );
};
