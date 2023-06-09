import * as React from "react";
import Button from "@mui/material/Button";

export default function AddEntityButton({ label, onClick }) {
  const style = {
    textTransform: "none",
  };
  return (
    <Button sx={style} onClick={onClick} variant="contained">
      {label}
    </Button>
  );
}
