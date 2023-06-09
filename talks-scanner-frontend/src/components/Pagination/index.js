import React from "react";

// mui
import TablePagination from "@mui/material/TablePagination";

export default function Pagination({
  queryParams,
  setQueryParams,
  totalElements,
}) {
  const handleChangeRowsPerPage = (event) => {
    setQueryParams({
      ...queryParams,
      page: 0,
      size: parseInt(event.target.value),
    });
  };

  const handleChangePage = (event, newPage) => {
    setQueryParams({ ...queryParams, page: newPage });
  };

  return (
    <TablePagination
      component="div"
      sx={{ width: "100%" }}
      count={totalElements ?? queryParams.page}
      page={queryParams.page}
      onPageChange={handleChangePage}
      rowsPerPage={queryParams.size}
      onRowsPerPageChange={handleChangeRowsPerPage}
    />
  );
}
