export const createToken = (login, password) => {
  return btoa(`${login}:${password}`);
};
