import React from "react";

// mui
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import { useTheme } from "@mui/material/styles";

// router
import { useNavigate } from "react-router-dom";

// store
import { useSelector, useDispatch } from "react-redux";
import { deleteUser, isAuthenticated } from "@root/store/authSlice";

// animation
import { sideBarAnimation } from "@root/theme/animations";

const linkStyle = {
  color: "white",
  textDecoration: "none",
  cursor: "pointer",
};

export default function MyAppBar({ open, setOpen }) {
  const theme = useTheme();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const isAuth = useSelector(isAuthenticated);

  const onLogin = () => {
    navigate("/signup");
  };

  const onLogout = () => {
    dispatch(deleteUser());
  };

  const authButton = !isAuth ? (
    <Button sx={{ textTransform: "none" }} color="inherit" onClick={onLogin}>
      Login
    </Button>
  ) : (
    <Button sx={{ textTransform: "none" }} color="inherit" onClick={onLogout}>
      Logout
    </Button>
  );

  const sideAnimation = sideBarAnimation(open, theme);

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar
        position="static"
        sx={{
          boxShadow: "none",
          ...sideAnimation,
        }}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            onClick={() => setOpen(true)}
            edge="start"
            sx={{ mr: 2, ...(open && { display: "none" }) }}
          >
            <MenuIcon />
          </IconButton>
          <Box sx={{ flexGrow: 1 }}>
            <Typography sx={linkStyle} variant="h4">
              Talks scanner
            </Typography>
          </Box>
          {authButton}
        </Toolbar>
      </AppBar>
    </Box>
  );
}
