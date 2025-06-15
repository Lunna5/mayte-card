import React, { useState } from 'react';
import { CardContext, type CardContextType } from './CardContext';

interface CardProviderProps {
  children: React.ReactNode;
}

export const CardProvider: React.FC<CardProviderProps> = ({ children }) => {
  const [cardNumber, setCardNumber] = useState(0);
  const [cardHolderName, setCardHolderName] = useState('');
  const [dedication, setDedication] = useState('');
  const [photoSrc, setPhotoSrc] = useState('');
  const [cardTheme, setCardTheme] = useState('candy');

  const value: CardContextType = {
    cardNumber,
    setCardNumber,
    cardHolderName,
    setCardHolderName,
    dedication,
    setDedication,
    photoSrc,
    setPhotoSrc,
    cardTheme,
    setCardTheme,
  };

  return <CardContext.Provider value={value}>{children}</CardContext.Provider>;
};
