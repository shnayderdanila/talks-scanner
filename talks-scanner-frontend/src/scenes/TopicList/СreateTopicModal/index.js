import React, { useEffect, useState } from "react";

// mui
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import Chip from "@mui/material/Chip";

// store
import {
  useCreateTopicMutation,
  useChangeTopicStatusMutation,
  useUpdateBeingPreparedMutation,
} from "@root/store/api/topicApi";
import { selectCurrentUser } from "@root/store/authSlice";
import { useSelector } from "react-redux";

// modal
import ModalLayout from "@root/components/Modal";

// const
import { CREATE_TOPIC_MODAL } from "@root/const/localstorage-key";
import { TopicStatus } from "@root/const/topicStatus";

const empty = {
  title: "",
  description: "",
  tag: "",
  tags: [],
};

export default function CreateTopicModal({
  open,
  handleClose,
  topicData,
  setTopicData,
}) {
  const initialValues = localStorage.getItem(CREATE_TOPIC_MODAL)
    ? JSON.parse(localStorage.getItem(CREATE_TOPIC_MODAL))
    : empty;

  const user = useSelector(selectCurrentUser);

  const [createTopic] = useCreateTopicMutation();
  const [changeTopicStatus] = useChangeTopicStatusMutation();
  const [updateBeingPrepared] = useUpdateBeingPreparedMutation();

  const [formValues, setFormValues] = useState(initialValues);

  const [tagsError, setTagsError] = useState("");

  const emptyEquals = (object) => {
    return !object?.title && !object?.description && object?.tags.length === 0;
  };

  useEffect(() => {
    if (open && !topicData) {
      localStorage.setItem(CREATE_TOPIC_MODAL, JSON.stringify(formValues));
    }
  }, [open, topicData, formValues]);

  useEffect(() => {
    return () => {
      const fromLocalStorage = JSON.parse(
        localStorage.getItem(CREATE_TOPIC_MODAL)
      );
      if (emptyEquals(fromLocalStorage)) {
        localStorage.removeItem(CREATE_TOPIC_MODAL);
      }
    };
  }, []);

  useEffect(() => {
    if (topicData) {
      setFormValues({
        title: topicData?.title,
        description: topicData?.description,
        tag: "",
        tags: topicData.tags ? topicData.tags.split(" ") : [],
      });
    } else {
      setFormValues(initialValues);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [topicData]);

  const handleChangeTitle = (event) => {
    setFormValues({ ...formValues, title: event.target.value });
  };

  const handleChangeDescription = (event) => {
    setFormValues({ ...formValues, description: event.target.value });
  };

  const handleChangeTag = (event) => {
    setFormValues({ ...formValues, tag: event.target.value });
  };

  const addTag = (tag) => {
    if (tag && !formValues.tags.includes(tag)) {
      setFormValues({
        ...formValues,
        tag: "",
        tags: [...formValues.tags, tag],
      });
      setTagsError("");
    }
  };

  const removeTag = (tag) => {
    setFormValues({
      ...formValues,
      tags: [...formValues.tags.filter((el) => el !== tag)],
    });
  };

  const handleSubmit = async () => {
    let topic;
    if (!emptyEquals(formValues)) {
      if (topicData) {
        if (formValues.tags.length > 0) {
          const updatedStatus = await changeTopicStatus({
            id: topicData.id,
            body: { status: TopicStatus.BEING_PREPARED },
          });
          if (updatedStatus) {
            topic = await updateBeingPrepared({
              id: topicData.id,
              body: {
                title: formValues.title,
                description: formValues.description,
                tags: formValues.tags.join(" "),
              },
            }).unwrap();
            handleClose();
          }
        } else {
          setTagsError("You need to specify at least one tag");
        }
      } else {
        topic = await createTopic({
          title: formValues.title,
          description: formValues.description,
          author: `${user.firstname} ${user.lastname}`,
        }).unwrap();
        setTopicData(topic);
      }
      localStorage.removeItem(CREATE_TOPIC_MODAL);
    }
  };

  return (
    <ModalLayout
      open={open}
      handleClose={handleClose}
      handleSubmit={handleSubmit}
      label={"Create Topic"}
    >
      <Box sx={{ mb: 4 }}>
        <Typography>Title</Typography>
        <TextField
          sx={{ mt: 2, "#title": { pt: 0 } }}
          id="title"
          name="title"
          fullWidth
          variant="standard"
          onChange={handleChangeTitle}
          value={formValues.title}
          error={Boolean(!formValues.title)}
          helperText={Boolean(!formValues.title) ? "Required" : " "}
        />
        <Typography>Description</Typography>
        <TextField
          id="description"
          sx={{ mt: 2, "#description": { pt: 0 } }}
          name="description"
          fullWidth
          multiline
          rows={10}
          onChange={handleChangeDescription}
          value={formValues.description}
          error={Boolean(!formValues.description)}
          helperText={Boolean(!formValues.description) ? "Required" : " "}
        />
        {topicData ? (
          <>
            <Box
              sx={{
                mt: 2,
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              <Typography sx={{ mr: 1 }}>Tags:</Typography>
              <TextField
                id="tags"
                sx={{ mt: 3.5, "#tags": { pt: 0 } }}
                name="tags"
                variant="standard"
                fullWidth
                onChange={handleChangeTag}
                value={formValues.tag}
                error={Boolean(tagsError)}
                helperText={tagsError ? tagsError : " "}
              />
              <Button
                sx={{ textTransform: "none" }}
                onClick={() => addTag(formValues.tag)}
              >
                Add
              </Button>
            </Box>

            <Box>
              {formValues.tags.map((tag) => {
                return (
                  <Chip
                    key={tag}
                    sx={{ mr: 1, mb: 1 }}
                    color="primary"
                    label={tag}
                    onDelete={() => removeTag(tag)}
                  />
                );
              })}
            </Box>
          </>
        ) : null}
      </Box>
    </ModalLayout>
  );
}
