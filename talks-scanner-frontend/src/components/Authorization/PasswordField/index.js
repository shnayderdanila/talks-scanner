import React, { useState } from "react";

// mui
import OutlinedInput from "@mui/material/OutlinedInput";
import InputLabel from "@mui/material/InputLabel";
import InputAdornment from "@mui/material/InputAdornment";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import FormControl from "@mui/material/FormControl";
import FormHelperText from "@mui/material/FormHelperText";
import IconButton from "@mui/material/IconButton";

export default function PasswordField({
  id,
  label,
  autoComplete,
  value,
  handleChange,
  hasError,
  errorMessage,
}) {
  const [show, setShow] = useState(false);

  return (
    <FormControl fullWidth required margin="dense" error={hasError}>
      <InputLabel htmlFor={id}>{label}</InputLabel>
      <OutlinedInput
        id={id}
        name={id}
        autoComplete={autoComplete}
        onChange={handleChange}
        value={value}
        type={show ? "text" : "password"}
        endAdornment={
          <InputAdornment position="end">
            <IconButton
              aria-label="toggle visibility"
              onClick={() => setShow((show) => !show)}
              edge="end"
            >
              {show ? <VisibilityOff /> : <Visibility />}
            </IconButton>
          </InputAdornment>
        }
        label={label}
      />
      <FormHelperText>{hasError ? errorMessage : " "}</FormHelperText>
    </FormControl>
  );
}
