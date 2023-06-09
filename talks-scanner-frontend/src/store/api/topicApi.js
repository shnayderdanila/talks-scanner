import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { prepareHeaders } from "@root/utils/prepareHeaders";
import { apiUrl } from "@root/utils/fetch";

export const topicApi = createApi({
  reducerPath: "topicApi",
  baseQuery: fetchBaseQuery({
    baseUrl: apiUrl("/api/v1/topics").toString(),
    mode: "cors",
    prepareHeaders,
  }),
  tagTypes: ["Topics"],
  endpoints: (builder) => ({
    getAllTopics: builder.query({
      query: (arg) => ({ url: "", params: { ...arg } }),
      providesTags: (result) =>
        result
          ? [
              ...result.content.map(({ id }) => ({ type: "Topics", id })),
              "Topics",
            ]
          : ["Topics"],
    }),
    getTopicById: builder.query({
      query: (params) => ({ url: `/${params}`, method: "GET", params: params }),
      providesTags: (result) => {
        if (result) {
          const { id } = result;
          return [{ type: "Topics", id }];
        }
      },
    }),
    createTopic: builder.mutation({
      query: ({ ...body }) => ({
        url: "",
        method: "POST",
        body,
      }),
      providesTags: (result) => {
        if (result) {
          const { id } = result;
          return [{ type: "Topics", id }];
        }
      },
      invalidatesTags: ["Topics"],
    }),
    changeTopicStatus: builder.mutation({
      query: (params) => ({
        url: `/${params.id}/status`,
        method: "PUT",
        body: params.body,
      }),
      providesTags: (result) => {
        if (result) {
          const { id } = result;
          return [{ type: "Topics", id }];
        }
      },
      invalidatesTags: ["Topics"],
    }),
    updateBeingPrepared: builder.mutation({
      query: (params) => ({
        url: `${params.id}/being-prepared`,
        method: "PUT",
        body: params.body,
      }),
      providesTags: (result) => {
        if (result) {
          const { id } = result;
          return [{ type: "Topics", id }];
        }
      },
      invalidatesTags: ["Topics"],
    }),
    updateSchedule: builder.mutation({
      query: (params) => ({
        url: `/${params.id}/scheduled`,
        method: "PUT",
        body: params.body,
      }),
      providesTags: (result) => {
        if (result) {
          const { id } = result;
          return [{ type: "Topics", id }];
        }
      },
      invalidatesTags: ["Topics"],
    }),
    updateFinish: builder.mutation({
      query: (params) => ({
        url: `${params.id}/done`,
        method: "PUT",
        body: params.body,
      }),
      providesTags: (result) => {
        if (result) {
          const { id } = result;
          return [{ type: "Topics", id }];
        }
      },
      invalidatesTags: ["Topics"],
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
  useLazyGetAllTopicsQuery,
  useCreateTopicMutation,
  useLazyGetTopicByIdQuery,
  useCreateCommentaryMutation,
  useLazyGetAllCommentsQuery,
  useDeleteCommentaryMutation,
  useUpdateCommentaryMutation,
  useLazyGetUserRateQuery,
  useCreateRateMutation,
  useDeleteRateMutation,
  useUpdateRateMutation,
  useChangeTopicStatusMutation,
  useUpdateBeingPreparedMutation,
  useUpdateScheduleMutation,
  useUpdateFinishMutation,
} = topicApi;
