import React, { useMemo } from "react";

// mui
import InputBase from "@mui/material/InputBase";
import Box from "@mui/material/Box";
import IconButton from "@mui/material/IconButton";
import SearchIcon from "@mui/icons-material/Search";

// lodash
import debounce from "lodash.debounce";

export default function SearchInput({
  placeholder,
  value,
  setValue,
  debounceTime = 1000,
  sx,
}) {
  const style = { ...sx, display: "flex" };

  const debounceSearchOnChange = useMemo(
    () =>
      debounce((event) => {
        setValue(event.target.value);
      }, debounceTime),
    [debounceTime, setValue]
  );

  return (
    <Box sx={style}>
      <IconButton type="button" sx={{ p: "0px" }} aria-label="search icon">
        <SearchIcon />
      </IconButton>
      <InputBase
        sx={{ ml: 1 }}
        placeholder={placeholder}
        inputProps={{ "aria-label": placeholder }}
        onChange={debounceSearchOnChange}
      />
    </Box>
  );
}
