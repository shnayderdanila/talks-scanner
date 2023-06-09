import { drawerWidth } from "@root/const/const";

export const sideBarAnimation = (
  open,
  theme,
  calculateWidth = `calc(100% - ${drawerWidth}px)`
) => {
  return {
    transition: theme.transitions.create(["margin", "width"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    ...(open && {
      width: calculateWidth,
      marginLeft: `${drawerWidth}px`,
      transition: theme.transitions.create(["margin", "width"], {
        easing: theme.transitions.easing.easeOut,
        duration: theme.transitions.duration.enteringScreen,
      }),
    }),
  };
};
