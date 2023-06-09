import React, { useEffect, useState } from "react";

// mui
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";

// store
import {
  useUpdateFinishMutation,
  useChangeTopicStatusMutation,
} from "@root/store/api/topicApi";

// modal
import ModalLayout from "@root/components/Modal";

// const
import { TopicStatus } from "@root/const/topicStatus";
import { MAX_LENGTH_LINKS } from "@root/const/const";

const format = "https://";

const lengthErrorMessage = `Links cannot be more than ${MAX_LENGTH_LINKS}`;
const formatErrorMessage = `Links must start with ${format}`;

export default function FinishTopicModal({ open, handleClose, topicData }) {
  const [updateFinish] = useUpdateFinishMutation();
  const [changeTopicStatus] = useChangeTopicStatusMutation();

  const [presentationLink, setPresentationLink] = useState("");
  const [videoLink, setVideoLink] = useState("");

  const [errorPressentationLink, setErrorPressentationLink] = useState("");
  const [errorVideoLink, setErrorVideoLink] = useState("");

  useEffect(() => {
    if (presentationLink.length > MAX_LENGTH_LINKS) {
      setErrorPressentationLink(lengthErrorMessage);
    } else if (!presentationLink.startsWith(format)) {
      setErrorPressentationLink(formatErrorMessage);
    } else if (presentationLink.length === 0) {
      setErrorPressentationLink("Required");
    } else {
      setErrorPressentationLink("");
    }
    if (videoLink.length > MAX_LENGTH_LINKS) {
      setErrorVideoLink(lengthErrorMessage);
    } else if (!videoLink.startsWith(format)) {
      setErrorVideoLink(formatErrorMessage);
    } else if (presentationLink.length === 0) {
      setErrorVideoLink("Required");
    } else {
      setErrorVideoLink("");
    }
  }, [presentationLink, videoLink]);

  const handleSubmit = async () => {
    if (
      topicData &&
      videoLink &&
      presentationLink &&
      !errorPressentationLink &&
      !errorVideoLink
    ) {
      const finish = await changeTopicStatus({
        id: topicData.id,
        body: { status: TopicStatus.DONE },
      });
      if (finish) {
        await updateFinish({
          id: topicData.id,
          body: { presentationLink: presentationLink, videoLink: videoLink },
        });
        handleClose();
      }
    }
  };

  const handleChangePresentation = (event) => {
    setPresentationLink(event.target.value);
  };

  const handleChangeVideo = (event) => {
    setVideoLink(event.target.value);
  };

  return (
    <ModalLayout
      open={open}
      handleClose={handleClose}
      handleSubmit={handleSubmit}
      label={"Finish Topic"}
    >
      <Box
        sx={{ display: "flex", flexDirection: "column", alignItems: "center" }}
      >
        <Box sx={{ width: "100%" }}>
          <Typography>Presentation Link</Typography>
          <TextField
            sx={{ mt: 1 }}
            fullWidth
            onChange={handleChangePresentation}
            value={presentationLink}
            error={Boolean(errorPressentationLink)}
            helperText={errorPressentationLink ? errorPressentationLink : " "}
          />
        </Box>
        <Box sx={{ width: "100%", mt: 1, mb: 3 }}>
          <Typography>Video Link</Typography>
          <TextField
            sx={{ mt: 1 }}
            fullWidth
            onChange={handleChangeVideo}
            value={videoLink}
            error={Boolean(errorVideoLink)}
            helperText={errorVideoLink ? errorVideoLink : " "}
          />
        </Box>
      </Box>
    </ModalLayout>
  );
}
