import * as React from "react";

// mui
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Modal from "@mui/material/Modal";

// theme
import {
  modalBackdropFilter,
  modalBackgroundColor,
} from "@root/theme/variables";

const rowCenterStyle = { display: "flex", justifyContent: "center" };

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  bgcolor: "background.paper",
  boxShadow: 24,
  borderRadius: 4,
  p: 4,
};

export default function ModalLayout({
  open,
  handleClose,
  handleSubmit,
  label,
  children,
  isControlled = true,
}) {
  return (
    <Modal
      scroll="body"
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
      sx={{
        backdropFilter: modalBackdropFilter,
        backgroundColor: modalBackgroundColor,
      }}
    >
      <Box sx={style}>
        <Typography
          sx={{ ...rowCenterStyle, mb: 4, fontWeight: "bold" }}
          variant="h5"
        >
          {label}
        </Typography>
        {children}
        {isControlled ? (
          <Button
            sx={{ ...rowCenterStyle }}
            type="submit"
            variant="contained"
            fullWidth
            onClick={handleSubmit}
          >
            Create
          </Button>
        ) : null}
      </Box>
    </Modal>
  );
}
