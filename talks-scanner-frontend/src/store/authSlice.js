import { createSlice } from "@reduxjs/toolkit";

// consts
import {
  REGISTER_FORM_STORAGE_KEY,
  LOGIN_FORM_STORAGE_KEY,
} from "@root/const/localstorage-key.js";

const STORAGE_KEY = "auth";

const initialState = JSON.parse(localStorage.getItem(STORAGE_KEY)) ?? {
  user: null,
  token: null,
};

const authSlice = createSlice({
  name: "authSlice",
  initialState: initialState,
  reducers: {
    saveUser: (state, action) => {
      state.user = action.payload.user;
      state.token = action.payload.token;

      localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
      localStorage.removeItem(REGISTER_FORM_STORAGE_KEY);
      localStorage.removeItem(LOGIN_FORM_STORAGE_KEY);
    },
    setToken: (state, action) => {
      state.token = action.payload.token;
    },
    deleteUser: (state) => {
      localStorage.removeItem(STORAGE_KEY);
      state.token = null;
      state.user = null;
    },
  },
});

export const { saveUser, setToken, deleteUser } = authSlice.actions;

export const isAuthenticated = (state) => state.authSlice.user !== null;
export const selectCurrentUser = (state) => state.authSlice.user;

export default authSlice.reducer;
