import React, { useState, useEffect, useCallback } from "react";

// components
import Layout from "@root/components/Layout";
import EntityTable from "@root/components/EntityTable";
import SearchInput from "@root/components/SearchInput";
import AddEntityButton from "@root/components/AddEntityButton";
import Pagination from "@root/components/Pagination";

// mui
import Typography from "@mui/material/Typography";
import TableCell from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import Box from "@mui/material/Box";

// icons
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";

// router
import { useNavigate } from "react-router-dom";

// store
import {
  useLazyGetAllIdeasQuery,
  useDeleteIdeaMutation,
} from "@root/store/api/ideaApi";
import { selectCurrentUser } from "@root/store/authSlice";
import { useSelector } from "react-redux";

// theme
import { tableColor } from "@root/theme/variables";

// components
import CreateIdeaModal from "@root/scenes/IdeaList/CreateIdeaModal";

import { cloneDeep } from "lodash";

const emptyQuery = {
  titleStartsWith: "",
  descriptionStartsWith: "",
  page: 0,
  size: 10,
  sort: "title",
};

const pageTitleStyle = {
  display: "flex",
  width: "100%",
  mt: 4,
  mb: 2,
  justifyContent: "space-between",
  alignItems: "center",
};

const styleNthTableRow = {
  "&:nth-of-type(odd)": {
    backgroundColor: tableColor,
  },
};

const headRow = ["Title", "Author", "Description", "Rating", "Actions"];

export default function IdeaList() {
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);

  const [queryParams, setQueryParams] = useState(emptyQuery);

  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = async () => {
    setOpen(false);
    setIdea(null);
    const fromBe = await getAllIdeas(queryParams).unwrap();
    setIdeas(cloneDeep(fromBe));
  };

  const [ideas, setIdeas] = useState(null);
  const [idea, setIdea] = useState(null);

  const [getAllIdeas] = useLazyGetAllIdeasQuery();
  const [deleteIdea] = useDeleteIdeaMutation();

  const getIdeasFromBE = useCallback(async () => {
    const fromBe = await getAllIdeas(queryParams).unwrap();
    setIdeas({ ...fromBe, content: [...fromBe.content] });
  }, [getAllIdeas, queryParams]);

  useEffect(() => {
    getIdeasFromBE();
  }, [queryParams, getIdeasFromBE]);

  const handleChangeTitle = (filter) => {
    setQueryParams({ ...queryParams, titleStartsWith: filter });
  };

  const searchProps = {
    sx: { alignSelf: "flex-start" },
    value: queryParams.titleStartsWith,
    setValue: handleChangeTitle,
    placeholder: "Search Idea",
  };

  const handleDelete = async (idea) => {
    await deleteIdea({ id: idea.id });
  };

  const handleUpdateClick = async (idea) => {
    setIdea(idea);
    handleOpen();
  };

  const ManageIdea = ({ user, idea }) => {
    return user.id === idea.user.userId ? (
      <Box sx={{ cursor: "pointer" }}>
        <DeleteIcon sx={{ mr: 1 }} onClick={() => handleDelete(idea)} />
        <EditIcon onClick={() => handleUpdateClick(idea)} />
      </Box>
    ) : null;
  };

  const IdeaContent = ({ ideas }) => {
    return ideas?.content?.map((row) => (
      <TableRow key={row.id} sx={{ ...styleNthTableRow, cursor: "pointer" }}>
        <TableCell onClick={() => navigate(`/ideas/${row.id}`)} align="center">
          {row.title}
        </TableCell>
        <TableCell onClick={() => navigate(`/ideas/${row.id}`)} align="center">
          {row?.user.firstname + " " + row?.user.lastname ?? "N/A"}
        </TableCell>
        <TableCell onClick={() => navigate(`/ideas/${row.id}`)} align="center">
          {row.description}
        </TableCell>
        <TableCell onClick={() => navigate(`/ideas/${row.id}`)} align="center">
          {row?.rate ?? "N/A"}
        </TableCell>
        <TableCell>
          <ManageIdea user={user} idea={row} />
        </TableCell>
      </TableRow>
    ));
  };

  return (
    <Layout>
      <Box sx={pageTitleStyle}>
        <Typography variant="h4">Ideas</Typography>
        <AddEntityButton label={"Add idea"} onClick={handleOpen} />
      </Box>
      <SearchInput {...searchProps} />
      <EntityTable headRow={headRow}>
        <IdeaContent ideas={ideas} />
      </EntityTable>
      <Pagination
        queryParams={queryParams}
        setQueryParams={setQueryParams}
        totalElements={ideas?.totalElements}
      />
      <CreateIdeaModal
        open={open}
        ideaData={idea}
        handleClose={handleClose}
        label={"Create Idea"}
        refetch={getIdeasFromBE}
      />
    </Layout>
  );
}
