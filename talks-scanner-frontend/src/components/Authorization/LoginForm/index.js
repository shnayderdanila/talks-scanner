import React, { useEffect } from "react";

// mui
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";

// components
import LoginField from "@root/components/Authorization/LoginField";
import PasswordField from "@root/components/Authorization/PasswordField";

// consts
import { LOGIN_FORM_STORAGE_KEY } from "@root/const/localstorage-key.js";

// formik
import { useFormik } from "formik";

const validate = (values) => {
  const errors = {};
  if (!values.login) {
    errors.login = "Required";
  }

  if (!values.password) {
    errors.password = "Required";
  }

  return errors;
};

const initialValues = () => {
  const data = localStorage.getItem(LOGIN_FORM_STORAGE_KEY);
  return data
    ? JSON.parse(data)
    : {
        login: "",
        password: "",
      };
};

export default function LoginForm({ handleSubmit, apiError }) {
  const formik = useFormik({
    initialValues: initialValues(),
    validate,
    onSubmit: (values) => {
      handleSubmit(values);
    },
  });

  useEffect(() => {
    localStorage.setItem(LOGIN_FORM_STORAGE_KEY, JSON.stringify(formik.values));
  }, [formik.values]);

  const hasLoginError =
    Boolean(apiError) || (formik.touched.login && Boolean(formik.errors.login));

  const errorLoginMessage = apiError
    ? apiError
    : formik.touched.login && Boolean(formik.errors.login)
    ? formik.errors.login
    : " ";

  return (
    <Box
      component="form"
      onSubmit={formik.handleSubmit}
      noValidate
      sx={{ mt: 1 }}
    >
      <LoginField
        value={formik.values.login}
        handleChange={formik.handleChange}
        hasError={hasLoginError}
        errorMessage={errorLoginMessage}
      />
      <PasswordField
        id={"password"}
        label={"Password"}
        autoComplete={"current-password"}
        hasError={formik.touched.password && Boolean(formik.errors.password)}
        errorMessage={formik.errors.password}
        value={formik.values.password}
        handleChange={formik.handleChange}
      />
      <Button type="submit" fullWidth variant="contained" sx={{ mt: 1 }}>
        Sign in
      </Button>
    </Box>
  );
}
