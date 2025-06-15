import { Button, Frame, GroupBox, Window, WindowContent, WindowHeader } from 'react95';
import styled, { ThemeProvider } from 'styled-components';
import { WindowWrapper } from './95/WindowWrapper';
import DefaultImage from '@/assets/images/default-image.jpg';
import { useCard } from '@/hooks/useCard';
import { WindowDraggable } from './Dragable';
import { useAuth } from '@/hooks/auth/useAuth';
import type { Props } from 'react-rnd';
import { getTheme } from '@/utils/themes';

const Wrapper = styled(WindowWrapper)`
  .window {
    width: 400px;
    min-height: 560px;
    max-height: 560px;
  }
`;

export interface CardProps {
  photoSrc?: string;
  id: number;
  name: string;
  dedication: string;
}

export const CardFromContext = ({ ...props }: Props) => {
  const card = useCard();

  if (!card) {
    console.error('Card context is not available');
    return null;
  }

  return (
    <ThemeProvider theme={getTheme(card.cardTheme || 'candy')}>
      <Card
        {...props}
        id={card.cardNumber}
        name={card.cardHolderName}
        dedication={card.dedication}
        photoSrc={card.photoSrc}
      />
    </ThemeProvider>
  );
};

const Card = ({ ...props }: CardProps & Props) => {
  return (
    <WindowDraggable {...props}>
      <CardContent {...props} />
    </WindowDraggable>
  );
};

const CardContent = ({ photoSrc, name, id, dedication }: CardProps) => {
  const { user } = useAuth();

  return (
    <Wrapper>
      <Window className='window'>
        <WindowHeader className='window-title cursor-move'>
          <span>
            #{id} {name || user?.username}
          </span>
          <Button>
            <span className='close-icon' />
          </Button>
        </WindowHeader>
        <WindowContent>
          <CardPhotoFrame src={photoSrc || DefaultImage} />

          <div className={'mt-4 max-h-[200px] h-[200px] flex'}>
            <GroupBox label='Dedicatoria 😍' className='w-full'>
              <span className={'wrap-break-word overflow-hidden'}>{dedication || 'Introduce tu dedicatoria...'}</span>
            </GroupBox>
          </div>
        </WindowContent>
      </Window>
    </Wrapper>
  );
};

const CardPhotoWrapper = styled.div`
  .photo-frame {
    width: 100%;
    aspect-ratio: 16 / 9;
    padding: 5px 6px 6px 5px;
    display: flex;
  }

  .photo-frame img {
    width: 100%;
    height: auto;
    object-fit: cover;
  }
`;

const CardPhotoFrame = (props: { src: string }) => {
  return (
    <CardPhotoWrapper>
      <Frame className={'photo-frame'} variant={'inside'} shadow>
        <img src={props.src} alt='Card Photo' />
      </Frame>
    </CardPhotoWrapper>
  );
};

export default Card;
