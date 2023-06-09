import React from "react";

// mui
import Avatar from "@mui/material/Avatar";

export default function UserAvatar({ name = "" }) {
  return <Avatar sx={{ mr: 1 }}>{name.charAt(0).toUpperCase()}</Avatar>;
}
