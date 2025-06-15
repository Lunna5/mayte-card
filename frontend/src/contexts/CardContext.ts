import { createContext } from 'react';

export interface CardContextType {
  cardNumber: number;
  setCardNumber: (cardNumber: number) => void;
  cardHolderName: string;
  setCardHolderName: (cardHolderName: string) => void;
  dedication: string;
  setDedication: (dedication: string) => void;
  photoSrc: string;
  setPhotoSrc: (photoSrc: string) => void;
  cardTheme: string;
  setCardTheme: (cardTheme: string) => void;
}

export const CardContext = createContext<CardContextType>({
  cardNumber: 0,
  setCardNumber: () => {},
  cardHolderName: '',
  setCardHolderName: () => {},
  dedication: '',
  setDedication: () => {},
  photoSrc: '',
  setPhotoSrc: () => {},
  cardTheme: 'candy',
  setCardTheme: () => {},
});
