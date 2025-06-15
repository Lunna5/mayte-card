import { updateUserCard, type Card, type CardCreateRequest } from '@/api/card/userCard';
import { useMutation } from '@tanstack/react-query';
import { submitUserCard } from '@/api/card/userCard';

export const useCreateCardRequest = () => {
  return useMutation<Card, unknown, CardCreateRequest>({
    mutationFn: async ({ cardHolderName, dedication, image, theme }) => {
      const res = await submitUserCard({
        cardHolderName,
        dedication,
        image,
        theme,
      });

      return res.data;
    },
  });
};

export const useUpdateCardRequest = () => {
  return useMutation<Card, unknown, CardCreateRequest>({
    mutationFn: async ({ cardHolderName, dedication, image, theme }) => {
      const res = await updateUserCard({
        cardHolderName,
        dedication,
        image,
        theme,
      });

      return res.data;
    },
  });
};