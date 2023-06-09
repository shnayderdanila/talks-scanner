import React, { useEffect, useState, useCallback } from "react";

// components
import EntityLayout from "@root/components/EntityLayout";
import Layout from "@root/components/Layout";
import Commentary from "@root/components/Commentary";
import Rate from "@root/components/Rate";

// store
import {
  useCreateCommentaryMutation,
  useLazyGetAllCommentsQuery,
  useDeleteCommentaryMutation,
  useUpdateCommentaryMutation,
  useLazyGetTopicByIdQuery,
  useLazyGetUserRateQuery,
  useCreateRateMutation,
  useDeleteRateMutation,
  useUpdateRateMutation,
} from "@root/store/api/topicApi";

// router
import { useParams } from "react-router-dom";

// const
import { TOPIC_COMMENT } from "@root/const/localstorage-key.js";

export default function Topic() {
  const { id } = useParams();
  const [topicData, setTopicData] = useState({});

  const [getTopicById] = useLazyGetTopicByIdQuery();

  const [getAllComments] = useLazyGetAllCommentsQuery();
  const [createCommentary] = useCreateCommentaryMutation();
  const [deleteCommentary] = useDeleteCommentaryMutation();
  const [updateCommentary] = useUpdateCommentaryMutation();

  const [getUserRate] = useLazyGetUserRateQuery();
  const [createRate] = useCreateRateMutation();
  const [deleteRate] = useDeleteRateMutation();
  const [updateRate] = useUpdateRateMutation();

  const getTopicByIdFromBE = useCallback(async () => {
    setTopicData(await getTopicById(id).unwrap());
  }, [getTopicById, id]);

  useEffect(() => {
    getTopicByIdFromBE();
  }, [id, getTopicByIdFromBE]);

  return (
    <Layout>
      <>
        <EntityLayout entityData={topicData} getAllComments={getAllComments} />
        <Commentary
          getAllComments={getAllComments}
          createCommentary={createCommentary}
          deleteCommentary={deleteCommentary}
          updateCommentary={updateCommentary}
          storageKey={TOPIC_COMMENT}
        />
        <Rate
          getRate={getUserRate}
          createRate={createRate}
          deleteRate={deleteRate}
          updateRate={updateRate}
          status={topicData.status}
        />
      </>
    </Layout>
  );
}
