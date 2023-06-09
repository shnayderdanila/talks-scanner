import React, { useEffect } from "react";

// mui
import Grid from "@mui/material/Grid";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";

// formik
import { useFormik } from "formik";

// store
import {
  useCreateIdeaMutation,
  useUpdateIdeaMutation,
} from "@root/store/api/ideaApi";

// modal
import ModalLayout from "@root/components/Modal";

// const
import { CREATE_IDEA_MODAL } from "@root/const/localstorage-key";

const initialValues = localStorage.getItem(CREATE_IDEA_MODAL)
  ? JSON.parse(localStorage.getItem(CREATE_IDEA_MODAL))
  : { title: "", description: "" };

const validate = (values) => {
  const errors = {};
  if (!values.title) {
    errors.title = "Required";
  }

  if (!values.description) {
    errors.description = "Required";
  }

  return errors;
};

export default function CreateIdeaModal({ open, handleClose, ideaData }) {
  const [createIdea] = useCreateIdeaMutation();
  const [updateIdea] = useUpdateIdeaMutation();

  const handleSubmit = async (payload) => {
    if (ideaData) {
      await updateIdea({
        id: ideaData.id,
        body: { title: payload.title, description: payload.description },
      }).unwrap();
    } else {
      await createIdea(payload).unwrap();
    }
    localStorage.removeItem(CREATE_IDEA_MODAL);
    formik.resetForm();
    handleClose();
  };

  const formik = useFormik({
    initialValues,
    validate,
    onSubmit: (values) => {
      handleSubmit(values);
    },
  });

  useEffect(() => {
    if (open && ideaData) {
      formik.setValues({
        title: ideaData.title,
        description: ideaData.description,
      });
    }
  }, [open, ideaData, formik]);

  useEffect(() => {
    if (open) {
      localStorage.setItem(CREATE_IDEA_MODAL, JSON.stringify(formik.values));
    }
  }, [open, formik.values]);

  useEffect(() => {
    if (!Boolean(formik.values.title) && !Boolean(formik.values.description)) {
      return () => {
        localStorage.removeItem(CREATE_IDEA_MODAL);
      };
    }
  });

  return (
    <ModalLayout
      open={open}
      handleClose={handleClose}
      handleSubmit={formik.handleSubmit}
      label={"Create Idea"}
    >
      <Grid container rowSpacing={1} sx={{ mb: 4 }}>
        <Grid item xs={3}>
          <Typography>Title</Typography>
        </Grid>
        <Grid item xs={9}>
          <TextField
            sx={{ "#title": { pt: 0 } }}
            id="title"
            name="title"
            fullWidth
            variant="standard"
            onChange={formik.handleChange}
            value={formik.values.title}
            error={formik.touched.title && Boolean(formik.errors.title)}
            helperText={
              formik.touched.title && Boolean(formik.errors.title)
                ? formik.errors.title
                : " "
            }
          />
        </Grid>
        <Grid item xs={3}>
          <Typography>Description</Typography>
        </Grid>
        <Grid item xs={9}>
          <TextField
            id="description"
            sx={{ "#description": { pt: 0 } }}
            name="description"
            variant="standard"
            fullWidth
            onChange={formik.handleChange}
            value={formik.values.description}
            error={
              formik.touched.description && Boolean(formik.errors.description)
            }
            helperText={
              formik.touched.description && Boolean(formik.errors.description)
                ? formik.errors.description
                : " "
            }
          />
        </Grid>
      </Grid>
    </ModalLayout>
  );
}
