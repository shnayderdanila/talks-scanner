export const prepareHeaders = (headers, { getState }) => {
  const token = getState().authSlice.token;
  if (token) {
    headers.set("authorization", `Basic ${token}`);
  }
  headers.set("Content-Type", "application/json");
  return headers;
};
