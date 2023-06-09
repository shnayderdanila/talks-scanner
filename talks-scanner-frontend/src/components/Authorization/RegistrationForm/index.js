import React, { useEffect } from "react";

// mui
import TextField from "@mui/material/TextField";
import MenuItem from "@mui/material/MenuItem";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";

// components
import LoginField from "@root/components/Authorization/LoginField";
import PasswordField from "@root/components/Authorization/PasswordField";

// const
import { REGISTER_FORM_STORAGE_KEY } from "@root/const/localstorage-key.js";
import { MIN_LENGTH_LOGIN_PASSWORD } from "@root/const/const.js";

// formik
import { useFormik } from "formik";

const sex = [
  {
    value: "Male",
  },
  {
    value: "Female",
  },
];

const validate = (values) => {
  const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;

  const errors = {};
  if (!values.login) {
    errors.login = "Required";
  } else if (values.login.length < MIN_LENGTH_LOGIN_PASSWORD) {
    errors.login = `Login lenght must be more or equal ${MIN_LENGTH_LOGIN_PASSWORD}`;
  }

  if (!values.password) {
    errors.password = "Required";
  } else if (values.password.length < MIN_LENGTH_LOGIN_PASSWORD) {
    errors.password = `Password lenght must be more or equal ${MIN_LENGTH_LOGIN_PASSWORD}`;
  }

  if (!values.repeatedPassword) {
    errors.repeatedPassword = "Required";
  } else if (values.password !== values.repeatedPassword) {
    errors.repeatedPassword = "Passwords should be equals";
  }

  if (!values.firstname) {
    errors.firstname = "Required";
  }

  if (!values.lastname) {
    errors.lastname = "Required";
  }

  if (!values.email) {
    errors.email = "Required";
  } else if (!emailRegex.test(values.email)) {
    errors.email = "Invalid email address";
  }

  if (!values.sex) {
    errors.sex = "Required";
  }

  return errors;
};

const initialValues = () => {
  const data = localStorage.getItem(REGISTER_FORM_STORAGE_KEY);
  return data
    ? JSON.parse(data)
    : {
        login: "",
        password: "",
        repeatedPassword: "",
        firstname: "",
        lastname: "",
        email: "",
        sex: "",
      };
};

export default function RegistrationForm({ handleSubmit, apiError }) {
  const formik = useFormik({
    initialValues: initialValues(),
    validate,
    onSubmit: (values) => {
      handleSubmit(values);
    },
  });

  useEffect(() => {
    localStorage.setItem(
      REGISTER_FORM_STORAGE_KEY,
      JSON.stringify(formik.values)
    );
  }, [formik.values]);

  const hasLoginError =
    Boolean(apiError.login?.value === formik.values.login) ||
    (formik.touched.login && Boolean(formik.errors.login));

  const errorLoginMessage =
    apiError.login?.value === formik.values.login
      ? apiError.login.errorMessage
      : formik.touched.login && Boolean(formik.errors.login)
      ? formik.errors.login
      : " ";

  const hasEmailError =
    Boolean(apiError.email?.value === formik.values.email) ||
    (formik.touched.email && Boolean(formik.errors.email));

  const errorEmailMessage =
    apiError.email?.value === formik.values.email
      ? apiError.email.errorMessage
      : formik.touched.email && Boolean(formik.errors.email)
      ? formik.errors.email
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
        autoComplete={"new-password"}
        hasError={formik.touched.password && Boolean(formik.errors.password)}
        errorMessage={formik.errors.password}
        value={formik.values.password}
        handleChange={formik.handleChange}
      />
      <PasswordField
        id={"repeatedPassword"}
        label={"Repeate password"}
        autoComplete={"new-password"}
        hasError={
          formik.touched.repeatedPassword &&
          Boolean(formik.errors.repeatedPassword)
        }
        errorMessage={formik.errors.repeatedPassword}
        value={formik.values.repeatedPassword}
        handleChange={formik.handleChange}
      />
      <TextField
        id="email"
        label="Email"
        name="email"
        autoComplete="email"
        margin="dense"
        required
        fullWidth
        value={formik.values.email}
        onChange={formik.handleChange}
        error={hasEmailError}
        helperText={errorEmailMessage}
      />
      <TextField
        margin="dense"
        required
        fullWidth
        id="firstname"
        label="Firstname"
        name="firstname"
        autoComplete="given-name"
        value={formik.values.firstname}
        onChange={formik.handleChange}
        error={formik.touched.firstname && Boolean(formik.errors.firstname)}
        helperText={
          formik.touched.firstname && Boolean(formik.errors.firstname)
            ? formik.errors.firstname
            : " "
        }
      />
      <TextField
        margin="dense"
        required
        fullWidth
        id="lastname"
        label="Lastname"
        name="lastname"
        autoComplete="family-name"
        value={formik.values.lastname}
        onChange={formik.handleChange}
        error={formik.touched.lastname && Boolean(formik.errors.lastname)}
        helperText={
          formik.touched.lastname && Boolean(formik.errors.lastname)
            ? formik.errors.lastname
            : " "
        }
      />
      <TextField
        select
        id="sex"
        name="sex"
        margin="dense"
        label="Sex"
        required
        fullWidth
        autoComplete="sex"
        sx={{ textAlign: "left" }}
        value={formik.values.sex}
        onChange={formik.handleChange}
        error={formik.touched.sex && Boolean(formik.errors.sex)}
        helperText={
          formik.touched.sex && Boolean(formik.errors.sex)
            ? formik.errors.sex
            : " "
        }
      >
        {sex.map((option) => (
          <MenuItem key={option.value} value={option.value}>
            {option.value}
          </MenuItem>
        ))}
      </TextField>
      <Button type="submit" fullWidth variant="contained" sx={{ mt: 1 }}>
        Sign up
      </Button>
    </Box>
  );
}
