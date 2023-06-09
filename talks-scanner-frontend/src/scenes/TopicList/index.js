import React, { useState, useEffect, useCallback } from "react";

// components
import Layout from "@root/components/Layout";
import EntityTable from "@root/components/EntityTable";
import SearchInput from "@root/components/SearchInput";
import AddEntityButton from "@root/components/AddEntityButton";
import Tags from "@root/components/Tags";
import Pagination from "@root/components/Pagination";

// mui
import Typography from "@mui/material/Typography";
import TableCell from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import MenuItem from "@mui/material/MenuItem";
import Link from "@mui/material/Link";

// router
import { useNavigate } from "react-router-dom";

// store
import { useLazyGetAllTopicsQuery } from "@root/store/api/topicApi";
import { useSelector } from "react-redux";
import { selectCurrentUser } from "@root/store/authSlice";

// theme
import { tableColor } from "@root/theme/variables";

// modal
import CreateTopicModal from "@root/scenes/TopicList/СreateTopicModal";
import FinishTopicModal from "@root/scenes/TopicList/FinishTopicModal";
import ScheduleTopicModal from "@root/scenes/TopicList/ScheduleTopicModal";

// const
import { TopicStatus, topicStatusOptions } from "@root/const/topicStatus";

const emptyQuery = {
  status: "",
  tags: "",
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

const headRow = [
  "Title",
  "Author",
  "Tags",
  "Status",
  "Links",
  "Rating",
  "Action",
];

export default function TopicList() {
  const user = useSelector(selectCurrentUser);
  const navigate = useNavigate();

  const [queryParams, setQueryParams] = useState(emptyQuery);
  const [topics, setTopics] = useState([]);
  const [topic, setTopic] = useState(null);

  const [openCreateModal, setOpenCreateModal] = useState(false);
  const [openScheduleModal, setOpenScheduleModal] = useState(false);
  const [openFinishModal, setOpenFinishModal] = useState(false);

  const handleOpenCreateModal = (topic) => {
    setTopic(topic);
    setOpenCreateModal(true);
  };

  const handleCloseCreateModal = () => {
    setTopic(null);
    setOpenCreateModal(false);
    getTopicsFromBE();
  };

  const handleOpenScheduleModal = (topic) => {
    setTopic(topic);
    setOpenScheduleModal(true);
  };

  const handleCloseScheduleModal = () => {
    setOpenScheduleModal(false);
    getTopicsFromBE();
  };

  const handleOpenFinishModal = (topic) => {
    setTopic(topic);
    setOpenFinishModal(true);
  };

  const handleCloseFinishModal = () => {
    setOpenFinishModal(false);
    getTopicsFromBE();
  };

  const [getAllTopics] = useLazyGetAllTopicsQuery();

  const getTopicsFromBE = useCallback(async () => {
    const fromBe = await getAllTopics({
      ...(queryParams.tags ? { tags: queryParams.tags } : {}),
      ...(queryParams.status ? { status: queryParams.status } : {}),
      page: queryParams.page,
      size: queryParams.size,
      sort: queryParams.sort,
    }).unwrap();
    setTopics({
      ...fromBe,
      content: [...fromBe.content],
    });
  }, [getAllTopics, queryParams]);

  useEffect(() => {
    getTopicsFromBE();
  }, [queryParams, getTopicsFromBE]);

  const handleChangeTags = (filter) => {
    setQueryParams({ ...queryParams, tags: filter });
  };

  const handleChangeStatus = (event) => {
    setQueryParams({ ...queryParams, status: event.target.value });
  };

  const searchProps = {
    sx: { alignSelf: "flex-start" },
    value: queryParams.tags,
    setValue: handleChangeTags,
    placeholder: "Search Topic by tags",
  };

  const ButtonPreparation = ({ topicData }) => (
    <Button
      sx={{
        textTransform: "none",
        fontSize: 16,
        color: "green",
        borderColor: "green",
      }}
      variant="outlined"
      onClick={() => handleOpenCreateModal(topicData)}
    >
      Start Prepation
    </Button>
  );

  const ButtonFinishTopic = ({ topicData }) => (
    <Button
      sx={{
        textTransform: "none",
        fontSize: 16,
        color: "purple",
        borderColor: "purple",
      }}
      variant="outlined"
      onClick={() => handleOpenFinishModal(topicData)}
    >
      Finish Topic
    </Button>
  );

  const ButtonScheduled = ({ topicData }) => (
    <Button
      sx={{
        textTransform: "none",
        fontSize: 16,
        color: "red",
        borderColor: "red",
      }}
      variant="outlined"
      onClick={() => handleOpenScheduleModal(topicData)}
    >
      Schedule
    </Button>
  );

  const chooseButton = (topic) => {
    switch (topic.status) {
      case TopicStatus.ANNOUNCED:
        return <ButtonPreparation topicData={topic} />;
      case TopicStatus.BEING_PREPARED:
        return <ButtonScheduled topicData={topic} />;
      case TopicStatus.SCHEDULED:
        return <ButtonFinishTopic topicData={topic} />;
      default:
        return;
    }
  };

  return (
    <Layout>
      <Box sx={pageTitleStyle}>
        <Typography variant="h4">Topics</Typography>
        <AddEntityButton
          label={"Create Topic"}
          onClick={() => handleOpenCreateModal(topic)}
        />
      </Box>
      <Box sx={{ display: "flex", width: "100%" }}>
        <SearchInput {...searchProps} />
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <Typography sx={{ mr: 2 }}>Status:</Typography>
          <TextField
            id="status"
            select
            defaultValue="Status"
            variant="standard"
            onChange={handleChangeStatus}
            sx={{ minWidth: 130 }}
          >
            <MenuItem value="">None</MenuItem>
            {topicStatusOptions.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </TextField>
        </Box>
      </Box>

      <EntityTable headRow={headRow}>
        {topics.content?.map((row) => (
          <TableRow
            key={row.id}
            sx={{ ...styleNthTableRow, cursor: "pointer" }}
          >
            <TableCell
              onClick={() => navigate(`/topics/${row.id}`)}
              align="center"
            >
              {row.title}
            </TableCell>
            <TableCell
              onClick={() => navigate(`/topics/${row.id}`)}
              align="center"
            >
              {row?.author ?? "N/A"}
            </TableCell>
            <TableCell
              onClick={() => navigate(`/topics/${row.id}`)}
              align="center"
            >
              <Tags tags={row?.tags} />
            </TableCell>
            <TableCell
              onClick={() => navigate(`/topics/${row.id}`)}
              align="center"
            >
              {row.status}
            </TableCell>
            <TableCell align="center">
              {row?.presentationLink ? (
                <Box sx={{ display: "flex", flexDirection: "column" }}>
                  <Link href={row?.presentationLink} underline="hover">
                    See presentation
                  </Link>
                  <Link href={row?.videoLink} underline="hover">
                    See video
                  </Link>
                </Box>
              ) : null}
            </TableCell>
            <TableCell
              onClick={() => navigate(`/topics/${row.id}`)}
              align="center"
            >
              {row?.rate ?? "N/A"}
            </TableCell>
            <TableCell align="center">
              {user?.id === row?.userId ? chooseButton(row) : null}
            </TableCell>
          </TableRow>
        ))}
      </EntityTable>
      <Pagination
        queryParams={queryParams}
        setQueryParams={setQueryParams}
        totalElements={topics?.totalElements}
      />
      <CreateTopicModal
        open={openCreateModal}
        handleClose={handleCloseCreateModal}
        label={"Create Topic"}
        topicData={topic}
        setTopicData={setTopic}
      />
      <ScheduleTopicModal
        open={openScheduleModal}
        handleClose={handleCloseScheduleModal}
        topicData={topic}
      />
      <FinishTopicModal
        open={openFinishModal}
        handleClose={handleCloseFinishModal}
        topicData={topic}
      />
    </Layout>
  );
}
