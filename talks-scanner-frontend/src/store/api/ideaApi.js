import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { prepareHeaders } from "@root/utils/prepareHeaders";
import { apiUrl } from "@root/utils/fetch";

export const ideaApi = createApi({
  reducerPath: "ideaApi",
  baseQuery: fetchBaseQuery({
    baseUrl: apiUrl("/api/v1/ideas").toString(),
    mode: "cors",
    prepareHeaders,
  }),
  tagTypes: ["Ideas"],
  endpoints: (builder) => ({
    getAllIdeas: builder.query({
      query: (arg) => ({ url: "", params: { ...arg } }),
      providesTags: (result) =>
        result
          ? [
              ...result.content.map(({ id }) => ({ type: "Ideas", id })),
              "Ideas",
            ]
          : ["Ideas"],
    }),
    getIdeaById: builder.query({
      query: (params) => ({ url: `/${params}`, method: "GET", params: params }),
      providesTags: (result) => {
        if (result) {
          const { id } = result;
          return [{ type: "Ideas", id }];
        }
      },
    }),
    createIdea: builder.mutation({
      query: ({ ...body }) => ({
        url: "",
        method: "POST",
        body,
      }),
      providesTags: (result) => {
        if (result) {
          const { id } = result;
          return [{ type: "Ideas", id }];
        }
      },
      invalidatesTags: ["Ideas"],
    }),
    updateIdea: builder.mutation({
      query: (params) => ({
        url: `/${params.id}`,
        method: "PUT",
        body: params.body,
      }),
      providesTags: (result) => {
        if (result) {
          const { id } = result;
          return [{ type: "Ideas", id }];
        }
      },
      invalidatesTags: ["Ideas"],
    }),
    deleteIdea: builder.mutation({
      query: (params) => ({
        url: `/${params.id}`,
        method: "DELETE",
      }),
      providesTags: (result) => {
        if (result) {
          const { id } = result;
          return [{ type: "Ideas", id }];
        }
      },
      invalidatesTags: ["Ideas"],
    }),
    createCommentary: builder.mutation({
      query: (params) => ({
        url: `/${params.id}/comments`,
        method: "POST",
        body: { text: params.text },
      }),
    }),
    getAllComments: builder.query({
      query: (params) => ({
        url: `/${params.id}/comments`,
        params: { page: params.page, size: params.size },
      }),
    }),
    updateCommentary: builder.mutation({
      query: (params) => ({
        url: `/${params.id}/comments/${params.commentId}`,
        method: "PUT",
        body: { text: params.text },
      }),
    }),
    deleteCommentary: builder.mutation({
      query: (params) => ({
        url: `/${params.id}/comments/${params.commentId}`,
        method: "DELETE",
      }),
    }),
    createRate: builder.mutation({
      query: (params) => ({
        url: `/${params.id}/rates`,
        method: "POST",
        body: { rate: params.rate },
      }),
    }),
    getUserRate: builder.query({
      query: (params) => ({
        url: `/${params.id}/rates`,
      }),
    }),
    updateRate: builder.mutation({
      query: (params) => ({
        url: `/${params.id}/rates/${params.rateId}`,
        method: "PUT",
        body: { rate: params.rate },
      }),
    }),
    deleteRate: builder.mutation({
      query: (params) => ({
        url: `/${params.id}/rates/${params.rateId}`,
        method: "DELETE",
      }),
    }),
  }),
});

export const {
  useLazyGetAllIdeasQuery,
  useCreateIdeaMutation,
  useLazyGetIdeaByIdQuery,
  useCreateCommentaryMutation,
  useLazyGetAllCommentsQuery,
  useDeleteCommentaryMutation,
  useUpdateCommentaryMutation,
  useLazyGetUserRateQuery,
  useCreateRateMutation,
  useDeleteRateMutation,
  useUpdateRateMutation,
  useUpdateIdeaMutation,
  useDeleteIdeaMutation,
} = ideaApi;
