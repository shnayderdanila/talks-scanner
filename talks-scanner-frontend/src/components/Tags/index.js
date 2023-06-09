import React from "react";

// mui
import Chip from "@mui/material/Chip";

export default function Tags({ tags }) {
  return tags
    ? tags.split(" ").map((tag) => {
        return (
          <Chip key={tag} sx={{ mr: 1, mb: 1 }} color="primary" label={tag} />
        );
      })
    : null;
}
