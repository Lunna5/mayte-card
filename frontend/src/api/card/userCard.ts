import api from '@/api/client';

export interface Card {
  id: number;
  cardNumber: number;
  cardHolderName: string;
  dedication: string;
  photoSrc: string;
  theme: string;
}

export interface CardCreateRequest {
  cardHolderName?: string;
  dedication?: string;
  image?: File | Blob;
  theme?: string;
}

export const fetchUserCard = async () => {
  return await api.get<Card>(`/api/v1/card/get/me`);
};

export const generateFormData = ({ cardHolderName, dedication, image, theme }: CardCreateRequest) => {
  const formData = new FormData();
  if (cardHolderName != undefined) formData.append('cardHolderName', cardHolderName);
  if (dedication != undefined) formData.append('dedication', dedication);
  if (image != undefined) formData.append('image', image);
  if (theme != undefined) formData.append('theme', theme);

  return formData;
};

export const submitUserCard = async ({ ...props }: CardCreateRequest) => {
  const formData = generateFormData(props);

  return await api.post<Card>('/api/v1/card/create', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

export const updateUserCard = async ({ ...props }: CardCreateRequest) => {
  const formData = generateFormData(props);

  return await api.patch<Card>(`/api/v1/card/`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};


export const getAllUserCards = async () => {
  return await api.get<Card[]>('/api/v1/card/get-all');
}

export const getImage = (id: number) => {
  return api.get(`/api/v1/card/image/${id}`, {
    responseType: 'blob',
  });
}

export const getImageUrl = (id: number) => {
  return api.get(`/api/v1/card/image/${id}`, {
    responseType: 'blob',
  }).then((response) => {
    return URL.createObjectURL(response.data);
  });
}