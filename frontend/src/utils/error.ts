import { AxiosError } from "axios";

const oopsify = async <T>(promise: Promise<T>): Promise<[Error | null, T | null]> => {
  try {
    const result = await promise;
    return [null, result];
  } catch (error) {
    if (error instanceof Error) {
      return [error, null];
    } else {
      return [new Error('An unknown error occurred'), null];
    }
  }
};

export const errorToRecord = (error: unknown): Record<string, string> => {
  if (error instanceof AxiosError && error.response) {
    return error.response.data as Record<string, string>;
  }

  if (error instanceof Error) {
    return { message: error.message };
  }

  return { message: 'Ha ocurrido un error desconocido' };
}

export default oopsify;