import { Button, Frame, GroupBox, Window, WindowContent, WindowHeader } from 'react95';
import styled from 'styled-components';
import { WindowWrapper } from './95/WindowWrapper';

const Wrapper = styled(WindowWrapper)`
  .window {
    width: 400px;
    min-height: 560px;
    max-height: 560px;
  }
`;

const Card = () => {
  return (
    <Wrapper>
      <Window className='window'>
        <WindowHeader className='window-title'>
          <span>#1 Julieta Emilia</span>
          <Button>
            <span className='close-icon' />
          </Button>
        </WindowHeader>
        <WindowContent>
          <CardPhotoFrame
            src={
              'https://files.lamega.com.rcnra-dev.com/assets/public/styles/main_image_amp_webp/public/media/image/image/2024-06/cazzu.png?VersionId=Ral3VutbB23Amv70W9PkOGmi1OSg8gny&h=a9c2e262&itok=J-bwGDMa'
            }
          />

          <div className={'mt-4 max-h-[200px] h-[200px] flex'}>
            <GroupBox label='Dedicatoria 😍'>
              <span className={'wrap-break-word overflow-hidden'}>
                AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
              </span>
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
