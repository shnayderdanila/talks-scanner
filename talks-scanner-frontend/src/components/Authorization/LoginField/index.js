import React from "react";

// mui
import TextField from "@mui/material/TextField";

export default function LoginField({
  value,
  handleChange,
  hasError,
  errorMessage,
}) {
  return (
    <TextField
      id="login"
      label="Login"
      name="login"
      autoComplete="username"
      margin="dense"
      required
      fullWidth
      autoFocus
      value={value}
      onChange={handleChange}
      error={hasError}
      helperText={errorMessage}
    />
  );
}
