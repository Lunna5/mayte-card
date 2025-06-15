import { Button, Select, TextInput, Window, WindowContent, WindowHeader } from 'react95';
import styled from 'styled-components';
import { WindowWrapper } from './95/WindowWrapper';
import { WindowDraggable } from './Dragable';
import { useEffect, useRef, useState } from 'react';
import Cropper, { type Area } from 'react-easy-crop';
import { useCard } from '@/hooks/useCard';
import { getCroppedImageBlob, getCroppedImageUrl } from '@/utils/getCroppedImage';
import { themeNames, themeNamesWithIndex } from '@/utils/themes';
import { fetchUserCard, getImageUrl } from '@/api/card/userCard';
import oopsify, { errorToRecord } from '@/utils/error';
import { AxiosError } from 'axios';
import { useCreateCardRequest, useUpdateCardRequest } from '@/hooks/card/useCardRequests';
import { ErrorSpan } from './ErrorSpan';

const Wrapper = styled(WindowWrapper)`
  .window {
    width: 400px;
    max-width: 100%;
    min-height: 560px;
  }
`;

export const CardForm = () => {
  return (
    <WindowDraggable>
      <Wrapper>
        <Window className='window'>
          <WindowHeader className='window-title cursor-move'>
            <span>Formulario</span>
            <Button>
              <span className='close-icon' />
            </Button>
          </WindowHeader>
          <WindowContent>
            <Form />
          </WindowContent>
        </Window>
      </Wrapper>
    </WindowDraggable>
  );
};

const Form = () => {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [file, setFile] = useState<File | null>(null);
  const [imageURL, setImageURL] = useState<string | null>(null);

  const [crop, setCrop] = useState({ x: 0, y: 0 });
  const [zoom, setZoom] = useState(1);
  const [croppedAreaPixels, setCroppedAreaPixels] = useState<Area>({ x: 0, y: 0, width: 0, height: 0 });

  const { cardHolderName, setCardHolderName, dedication, setDedication, setPhotoSrc, cardTheme, setCardTheme } =
    useCard();

  const [isNewCard, setIsNewCard] = useState(false);
  const [error, setError] = useState<Record<string, string>>({});

  useEffect(() => {
    const getCard = async () => {
      const [error, card] = await oopsify(fetchUserCard());

      if (error && error instanceof AxiosError) {
        if (error.status === 404) {
          setIsNewCard(true);
          console.error('No card found for the user');
        }

        return;
      }

      if (card) {
        const { cardHolderName, dedication, id } = card.data;

        setCardHolderName(cardHolderName);
        setDedication(dedication);
        const [error, imageUrl] = await oopsify(getImageUrl(id));

        if (error) {
          console.error('Error fetching card image:', error);
          setError({ message: 'Error fetching card image', ...errorToRecord(error) });
          return;
        }

        if (!imageUrl) {
          console.error('No image URL found for the card');
          setError({ message: 'No image URL found for the card' });
          return;
        }

        setPhotoSrc(imageUrl);

        setCardTheme(card.data.cardTheme || 'candy');
      }
    };

    getCard();
  }, [setCardHolderName, setDedication, setPhotoSrc, setCardTheme]);

  useEffect(() => {
    if (isNewCard) setCardTheme('candy');
  }, [setCardTheme, isNewCard]);

  const { mutate: createCardMutate, isPending: creatingCard } = useCreateCardRequest();

  const handleCardCreation = async () => {
    if (!imageURL || !croppedAreaPixels || !cardHolderName || !dedication) {
      console.error('Missing required fields for card creation');
      setError({
        message: 'Faltan campos requeridos para crear la tarjeta.',
        image: !imageURL ? 'La imagen es requerida.' : '',
        cardHolderName: !cardHolderName ? 'El nombre del titular es requerido.' : '',
        dedication: !dedication ? 'La dedicatoria es requerida.' : '',
      });
      return;
    }

    if (dedication.length > 244) {
      console.error('Dedication exceeds maximum length of 244 characters');
      setError({
        ...error,
        dedication: 'La dedicatoria no puede exceder los 244 caracteres.',
      });
      return;
    }

    const imageBlob = await getCroppedImageBlob(imageURL, { ...croppedAreaPixels });

    createCardMutate(
      {
        cardHolderName: cardHolderName,
        dedication: dedication,
        image: imageBlob,
        theme: cardTheme,
      },
      {
        onSuccess: (data) => {
          console.log('Card created successfully:', data);
          if (isNewCard) {
            setIsNewCard(false);
          }
        },
        onError: (error) => {
          console.error('Error creating card:', error);
          setError({
            message: 'Error creating card',
            ...errorToRecord(error),
          });
        },
      },
    );
  };

  const { mutate: updateCardMutate, isPending: updatingCard } = useUpdateCardRequest();

  const handleCardUpdate = async () => {
    if (dedication.length > 244) {
      console.error('Dedication exceeds maximum length of 244 characters');
      setError({
        ...error,
        dedication: 'La dedicatoria no puede exceder los 244 caracteres.',
      });
      return;
    }

    let imageBlob: Blob | undefined;

    if (imageURL && croppedAreaPixels) {
      imageBlob = await getCroppedImageBlob(imageURL, { ...croppedAreaPixels });
    } else {
      imageBlob = undefined;
      return;
    }

    updateCardMutate(
      {
        cardHolderName: cardHolderName,
        dedication: dedication,
        image: imageBlob,
        theme: cardTheme,
      },
      {
        onSuccess: (data) => {
          console.log('Card updated successfully:', data);
        },
        onError: (error) => {
          console.error('Error updating card:', error);
          setError({
            message: 'Error updating card',
            ...errorToRecord(error),
          });
        },
      },
    );
  };
  const badStyle = {
    color: 'red',
    fontStyle: 'bold',
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files?.[0];
    if (selectedFile) {
      setFile(selectedFile);
      setImageURL(URL.createObjectURL(selectedFile));
      // Handle file upload logic here
      console.log('Selected file:', selectedFile);
    }
  };

  const onCropComplete = (_croppedArea: Area, croppedAreaPixels: Area) => {
    if (!imageURL) return;
    setCroppedAreaPixels(croppedAreaPixels);

    const image = getCroppedImageUrl(imageURL, { ...croppedAreaPixels });

    image
      .then((croppedImage) => {
        setPhotoSrc(croppedImage);
        console.log('Cropped image URL:', croppedImage);
      })
      .catch((error) => {
        console.error('Error cropping image:', error);
      });
  };

  return (
    <form className='flex flex-col gap-4'>
      <div>
        <ErrorSpan error={error.message} />
        <ErrorSpan error={error.cardHolderName} />
        <label htmlFor='name'>Tu nombre:</label>
        <TextInput
          placeholder={'Mayte Lopez...'}
          defaultValue={cardHolderName}
          onChange={(e) => setCardHolderName(e.target.value)}
        />
      </div>

      <div className='flex flex-col gap-2'>
        <ErrorSpan error={error.image} />
        <label htmlFor='photo'>Foto:</label>
        <Button onClick={() => fileInputRef.current?.click()} className='w-full'>
          Subir Imagen
        </Button>
        <input type='file' accept='image/*' className='hidden' ref={fileInputRef} onChange={handleFileChange} />
        {file && (
          <div>
            <span>Archivo seleccionado: {file.name}</span>
            <div className='mt-2 relative w-full h-64'>
              <Cropper
                image={imageURL || ''}
                crop={crop}
                zoom={zoom}
                aspect={16 / 9}
                onCropChange={setCrop}
                onZoomChange={setZoom}
                onCropComplete={onCropComplete}
              />
            </div>
          </div>
        )}
        <div className='flex flex-col gap-2'>
          <ErrorSpan error={error.theme} />
          <label htmlFor='theme-select'>Tema</label>
          <Select
            id='theme-select'
            defaultValue={7}
            menuMaxHeight={160}
            options={themeNames.map((theme, index) => ({
              label: theme,
              value: index,
            }))}
            className='w-full'
            onChange={(e) => e.value !== undefined && setCardTheme(themeNamesWithIndex[e.value].name)}
          />
        </div>
      </div>

      <div>
        <ErrorSpan error={error.dedication} />
        <label style={dedication.length > 244 ? badStyle : {}} htmlFor='dedication'>
          Dedicatoria: ({dedication.length}/244)
        </label>
        <TextInput
          multiline
          rows={4}
          placeholder={'Dedicatoria...'}
          defaultValue={dedication}
          onChange={(e) => setDedication(e.target.value)}
        />
      </div>

      {isNewCard ? (
        <Button
          onClick={handleCardCreation}
          className='w-full'
          disabled={creatingCard || !imageURL || !croppedAreaPixels || !cardHolderName || !dedication}
        >
          Crear Tarjeta
        </Button>
      ) : (
        <Button
          onClick={handleCardUpdate}
          className='w-full'
          disabled={creatingCard || updatingCard || !cardHolderName || !dedication}
        >
          Actualizar Tarjeta
        </Button>
      )}
    </form>
  );
};
