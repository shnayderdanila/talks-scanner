import React from "react";

// mui
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableCell from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import Box from "@mui/material/Box";

export default function EntityTable({ headRow, children }) {
  return (
    <TableContainer component={Box}>
      <Table aria-label="enity table">
        <TableHead>
          <TableRow>
            {headRow?.map((row) => (
              <TableCell align="center" key={row}>
                {row}
              </TableCell>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>{children}</TableBody>
      </Table>
    </TableContainer>
  );
}
