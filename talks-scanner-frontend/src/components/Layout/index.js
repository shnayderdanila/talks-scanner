import React, { useEffect, useState } from "react";

// mui
import Box from "@mui/material/Box";
import { createTheme, ThemeProvider } from "@mui/material/styles";

// component
import MyAppBar from "@root/components/AppBar";

// router
import { useNavigate } from "react-router-dom";

// store
import { useSelector } from "react-redux";
import { isAuthenticated } from "@root/store/authSlice";

// theme
import { typography } from "@root/theme/typography";
import { sideBarAnimation } from "@root/theme/animations";

// components
import Menu from "@root/components/Menu";

// const
import { layoutWidth, drawerWidth } from "@root/const/const";

const theme = createTheme({ typography });

export default function Layout({ children }) {
  const navigate = useNavigate();
  const isAuth = useSelector(isAuthenticated);

  const [open, setOpen] = useState(false);

  const centerContent = {
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",
    mb: 4,
  };

  const calculateWidth = `calc(${layoutWidth} - ${drawerWidth}px)`;

  useEffect(() => {
    if (!isAuth) {
      navigate("/signin");
    }
  }, [isAuth, navigate]);

  return (
    <ThemeProvider theme={theme}>
      <MyAppBar open={open} setOpen={setOpen} />
      <Menu open={open} setOpen={setOpen} />
      <Box sx={centerContent}>
        <Box
          sx={{
            width: layoutWidth,
            ...centerContent,
            ...sideBarAnimation(open, theme, calculateWidth),
          }}
        >
          {children}
        </Box>
      </Box>
    </ThemeProvider>
  );
}
