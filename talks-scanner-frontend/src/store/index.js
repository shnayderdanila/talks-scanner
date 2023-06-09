import { configureStore } from "@reduxjs/toolkit";
import { authApi } from "@root/store/api/authApi";
import { ideaApi } from "@root/store/api/ideaApi";
import { topicApi } from "@root/store/api/topicApi";
import authSliceReducer from "@root/store/authSlice";

const store = configureStore({
  reducer: {
    [authApi.reducerPath]: authApi.reducer,
    [topicApi.reducerPath]: topicApi.reducer,
    [ideaApi.reducerPath]: ideaApi.reducer,
    authSlice: authSliceReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware()
      .concat(authApi.middleware)
      .concat(topicApi.middleware)
      .concat(ideaApi.middleware),
});

export default store;
