export function getCroppedImageBlob(
  imageSrc: string,
  crop: { width: number; height: number; x: number; y: number }
): Promise<Blob> {
  return new Promise((resolve, reject) => {
    const image = new Image();
    image.src = imageSrc;
    image.crossOrigin = 'anonymous';

    image.onload = () => {
      const canvas = document.createElement('canvas');
      canvas.width = crop.width;
      canvas.height = crop.height;

      const ctx = canvas.getContext('2d');
      if (!ctx) return reject('No canvas context');

      ctx.drawImage(image, crop.x, crop.y, crop.width, crop.height, 0, 0, crop.width, crop.height);

      canvas.toBlob((blob) => {
        if (!blob) {
          reject('Canvas is empty');
          return;
        }
        resolve(blob);
      }, 'image/jpeg');
    };

    image.onerror = () => {
      reject('Error loading image');
    };
  });
}

export async function getCroppedImageUrl(
  imageSrc: string,
  crop: { width: number; height: number; x: number; y: number }
): Promise<string> {
  const blob = await getCroppedImageBlob(imageSrc, crop);
  return URL.createObjectURL(blob);
}
