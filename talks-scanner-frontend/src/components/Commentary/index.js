import React, { useEffect, useState, useCallback } from "react";

// mui
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";

// components
import UserAvatar from "@root/components/UserAvatar";
import Pagination from "@root/components/Pagination";

// icons
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";

// router
import { useParams } from "react-router-dom";

// store
import { selectCurrentUser } from "@root/store/authSlice";
import { useSelector } from "react-redux";

const emptyQuery = {
  page: 0,
  size: 10,
};

export default function Commentary({
  getAllComments,
  createCommentary,
  updateCommentary,
  deleteCommentary,
  storageKey,
}) {
  const user = useSelector(selectCurrentUser);
  const initialValues = localStorage.getItem(storageKey) ?? "";

  const { id } = useParams();
  const [queryParams, setQueryParams] = useState({ ...emptyQuery, id });

  const [curCommentId, setCurrCommentId] = useState("");
  const [commentaryData, setCommentaryData] = useState(null);

  const [text, setText] = useState(initialValues);

  const handleTextChange = (event) => {
    event.target.focus();
    setText(event.target.value);
  };

  const handleDelete = async (comment) => {
    await deleteCommentary({ id, commentId: comment.id });
    setCommentaryData(await getAllComments(queryParams).unwrap());
  };

  const handleUpdateClick = (comment) => {
    setText(comment.text);
    setCurrCommentId(comment.id);
  };

  const handleClear = () => {
    setText("");
    setCurrCommentId("");
  };

  const handleSubmitSendCommentary = async () => {
    if (text) {
      if (curCommentId) {
        await updateCommentary({
          id,
          commentId: curCommentId,
          text: text,
        });
      } else {
        await createCommentary({ id: id, text: text });
      }
      localStorage.removeItem(storageKey);
      setCurrCommentId("");
      setText("");
      setCommentaryData(await getAllComments(queryParams).unwrap());
    }
  };

  const getAllCommentsFromBE = useCallback(async () => {
    setCommentaryData(await getAllComments(queryParams).unwrap());
  }, [getAllComments, queryParams]);

  useEffect(() => {
    getAllCommentsFromBE(queryParams);
  }, [getAllCommentsFromBE, queryParams]);

  useEffect(() => {
    localStorage.setItem(storageKey, text);
  }, [text, setText, storageKey]);

  useEffect(() => {
    return () => {
      if (localStorage.getItem(storageKey) === "") {
        localStorage.removeItem(storageKey);
      }
    };
  }, [storageKey]);

  const ManageCommentary = ({ user, comment }) => {
    return user.id === comment.userId ? (
      <Box sx={{ cursor: "pointer" }}>
        <DeleteIcon sx={{ mr: 1 }} onClick={() => handleDelete(comment)} />
        <EditIcon onClick={() => handleUpdateClick(comment)} />
      </Box>
    ) : null;
  };

  return (
    <Box
      sx={{ display: "flex", flexDirection: "column", mt: 2, width: "100%" }}
    >
      {commentaryData?.content.length > 0 ? (
        <>
          <Typography variant="h4">Commentary</Typography>
          {commentaryData?.content.map((commentary) => {
            return (
              <Paper
                key={commentary.id}
                sx={{ display: "flex", flexDirection: "column", mt: 4 }}
                elevation={0}
              >
                <Typography>{commentary.text}</Typography>
                <Box
                  sx={{
                    mt: 1,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "space-between",
                    position: "relative",
                  }}
                >
                  <ManageCommentary user={user} comment={commentary} />
                  <Box
                    sx={{
                      display: "flex",
                      alignItems: "center",
                      position: "absolute",
                      right: 0,
                    }}
                  >
                    <UserAvatar name={commentary.authorFirstName} />
                    <Typography>{`${commentary.authorFirstName} ${commentary.authorLastName}`}</Typography>
                  </Box>
                </Box>
              </Paper>
            );
          })}
          <Pagination
            queryParams={queryParams}
            setQueryParams={setQueryParams}
            totalElements={commentaryData?.totalElements}
          />
        </>
      ) : null}

      <Typography sx={{ mb: 2 }} variant="h4">
        Write your commentary
      </Typography>
      <TextField
        multiline
        rows={10}
        fullWidth
        value={text}
        onChange={handleTextChange}
      />
      <Box sx={{ display: "flex" }}>
        <Button
          type="submit"
          sx={{ width: 100, mt: 1, mr: 4 }}
          onClick={handleSubmitSendCommentary}
        >
          Send
        </Button>
        {curCommentId ? (
          <Button
            type="button"
            color="error"
            sx={{ width: 100, mt: 1 }}
            onClick={handleClear}
          >
            Clear
          </Button>
        ) : null}
      </Box>
    </Box>
  );
}
