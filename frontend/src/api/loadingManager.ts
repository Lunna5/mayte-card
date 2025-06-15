let setLoading: (loading: boolean) => void = () => {};

export const registerSetLoading = (fn: (loading: boolean) => void) => {
  setLoading = fn;
};

export const startLoading = () => setLoading(true);
export const stopLoading = () => setLoading(false);
