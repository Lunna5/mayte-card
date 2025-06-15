import { CardContext } from '@/contexts/CardContext';
import { useContext } from 'react';

export const useCard = () => useContext(CardContext);
