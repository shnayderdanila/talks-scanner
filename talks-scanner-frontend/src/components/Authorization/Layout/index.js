import React, { useEffect } from "react";

// mui
import Avatar from "@mui/material/Avatar";
import CssBaseline from "@mui/material/CssBaseline";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import CircularProgress from "@mui/material/CircularProgress";
import Link from "@mui/material/Link";
import { createTheme, ThemeProvider } from "@mui/material/styles";

// router
import { useNavigate } from "react-router-dom";

// store
import { useSelector } from "react-redux";
import { isAuthenticated } from "@root/store/authSlice";

// theme
import { typography } from "@root/theme/typography";

const theme = createTheme({ typography });

export default function AuthorizationLayout({
  label,
  link,
  isLoading,
  children,
}) {
  const navigate = useNavigate();
  const linkLabel = label === "Sign up" ? "Sign in" : "Sign up";

  const isAuth = useSelector(isAuthenticated);

  useEffect(() => {
    if (isAuth) {
      navigate("/topics");
    }
  }, [isAuth, navigate]);

  return (
    <>
      {Boolean(isLoading) ? (
        <CircularProgress />
      ) : (
        <ThemeProvider theme={theme}>
          <Container component="main" maxWidth="xs">
            <CssBaseline />
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
              }}
            >
              <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
                <LockOutlinedIcon />
              </Avatar>
              <Typography component="h1" variant="h5">
                {label}
              </Typography>
              <Typography component="h1" variant="h3">
                Talks Scanner
              </Typography>
              {children}
              <Link href={link} sx={{ mt: 2 }} underline="none">
                {linkLabel}
              </Link>
            </Box>
          </Container>
        </ThemeProvider>
      )}
    </>
  );
}
