function apiUrl(path) {
  return new URL(`${process.env.REACT_APP_API_URL}${path}`);
}

export { apiUrl };
