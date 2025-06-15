import themes from 'react95/dist/themes';

export const themeNames = Object.keys(themes) as Array<keyof typeof themes>;
export const themeNamesWithIndex = themeNames.map((name, index) => ({
    name,
    index,
}));

export const getTheme = (name: string) => {
  if (name in themes) {
    return themes[name as keyof typeof themes];
  }

  return themes.candy; // Fallback to candy theme if not found
};

export const getThemeByIndex = (index: number) => {
  const themeKeys = Object.keys(themes);
  if (index >= 0 && index < themeKeys.length) {
    return themes[themeKeys[index] as keyof typeof themes];
  }
  return themes.candy; // Fallback to candy theme if index is out of bounds
};
