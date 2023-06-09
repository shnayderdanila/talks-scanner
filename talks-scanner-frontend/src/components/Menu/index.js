import * as React from "react";

// mui
import { useTheme } from "@mui/material/styles";
import Box from "@mui/material/Box";
import Drawer from "@mui/material/Drawer";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import IconButton from "@mui/material/IconButton";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemText from "@mui/material/ListItemText";

// router
import { useNavigate } from "react-router-dom";

import { drawerWidth } from "@root/const/const";

const menuItem = [
  {
    name: "Ideas",
    url: "/ideas",
  },
  {
    name: "Topics",
    url: "/topics",
  },
];

export default function Menu({ open, setOpen }) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleDrawerClose = () => {
    setOpen(false);
  };

  const drawerPosition = {
    width: drawerWidth,
    flexShrink: 0,
    "& .MuiDrawer-paper": {
      width: drawerWidth,
      boxSizing: "border-box",
    },
  };

  const drawerHeader = {
    display: "flex",
    minHeight: 63,
    alignItems: "center",
    padding: theme.spacing(0, 1),
    justifyContent: "flex-start",
  };

  return (
    <Drawer sx={drawerPosition} variant="persistent" anchor="left" open={open}>
      <Box sx={drawerHeader}>
        <IconButton onClick={handleDrawerClose}>
          <ChevronLeftIcon />
        </IconButton>
      </Box>
      <Divider />
      <List>
        {menuItem.map((menuItem) => (
          <ListItem key={menuItem.name} disablePadding>
            <ListItemButton>
              <ListItemText
                primary={menuItem.name}
                onClick={() => navigate(menuItem.url)}
              />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Drawer>
  );
}
