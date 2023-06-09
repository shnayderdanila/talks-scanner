import React, { useEffect, useState, useCallback } from "react";

// components
import EntityLayout from "@root/components/EntityLayout";
import Layout from "@root/components/Layout";
import Commentary from "@root/components/Commentary";
import Rate from "@root/components/Rate";

//store
import {
  useCreateCommentaryMutation,
  useLazyGetAllCommentsQuery,
  useDeleteCommentaryMutation,
  useUpdateCommentaryMutation,
  useLazyGetIdeaByIdQuery,
  useCreateRateMutation,
  useDeleteRateMutation,
  useUpdateRateMutation,
  useLazyGetUserRateQuery,
} from "@root/store/api/ideaApi";

// router
import { useParams } from "react-router-dom";

// const
import { IDEA_COMMENT } from "@root/const/localstorage-key.js";

export default function Idea() {
  const { id } = useParams();
  const [getIdeaById] = useLazyGetIdeaByIdQuery();

  const [ideaData, setIdeaData] = useState({});

  const [getAllComments] = useLazyGetAllCommentsQuery();
  const [createCommentary] = useCreateCommentaryMutation();
  const [deleteCommentary] = useDeleteCommentaryMutation();
  const [updateCommentary] = useUpdateCommentaryMutation();

  const [getUserRate] = useLazyGetUserRateQuery();
  const [createRate] = useCreateRateMutation();
  const [deleteRate] = useDeleteRateMutation();
  const [updateRate] = useUpdateRateMutation();

  const getIdeaByIdFromBE = useCallback(async () => {
    setIdeaData(await getIdeaById(id).unwrap());
  }, [getIdeaById, id]);

  useEffect(() => {
    getIdeaByIdFromBE();
  }, [id, getIdeaByIdFromBE]);

  return (
    <Layout>
      <>
        <EntityLayout
          entityData={{
            ...ideaData,
            author: `${ideaData.user?.firstname} ${ideaData.user?.lastname}`,
          }}
        />
        <Commentary
          getAllComments={getAllComments}
          createCommentary={createCommentary}
          deleteCommentary={deleteCommentary}
          updateCommentary={updateCommentary}
          storageKey={IDEA_COMMENT}
        />
        <Rate
          getRate={getUserRate}
          createRate={createRate}
          deleteRate={deleteRate}
          updateRate={updateRate}
        />
      </>
    </Layout>
  );
}
