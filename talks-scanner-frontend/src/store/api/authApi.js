import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { prepareHeaders } from "@root/utils/prepareHeaders";
import { apiUrl } from "@root/utils/fetch";

export const authApi = createApi({
  reducerPath: "authApi",
  baseQuery: fetchBaseQuery({
    baseUrl: apiUrl("/api/v1/users").toString(),
    mode: "cors",
    prepareHeaders,
  }),
  endpoints: (builder) => ({
    signup: builder.mutation({
      query: ({ ...payload }) => ({
        method: "POST",
        body: JSON.stringify(payload),
      }),
    }),
    getUser: builder.query({
      query: () => "",
    }),
  }),
});

export const { useSignupMutation, useLazyGetUserQuery } = authApi;
