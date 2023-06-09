import React from "react";

// mui
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import Link from "@mui/material/Link";

// components
import UserAvatar from "@root/components/UserAvatar";
import Tags from "@root/components/Tags";

// dayjs
import dayjs from "dayjs";

const spaceBetweenComponents = 2;

export default function EntityLayout({ entityData }) {
  const EventDate = entityData.eventDate ? (
    <Typography sx={{ mt: spaceBetweenComponents }}>
      Event date: {dayjs(entityData.eventDate).format("YYYY MM DD HH:mm:ss")}
    </Typography>
  ) : null;

  const Links =
    Boolean(entityData.presentationLink) || Boolean(entityData.videoLink) ? (
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          mt: spaceBetweenComponents,
        }}
      >
        <Link href={entityData?.presentationLink} underline="none" variant="h6">
          See presentation
        </Link>
        <Link href={entityData?.videoLink} underline="none" variant="h6">
          See video
        </Link>
      </Box>
    ) : null;

  return (
    <Box
      sx={{
        mt: 5,
        width: "100%",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      <Paper
        elevation={0}
        sx={{
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          width: "50%",
        }}
      >
        <Typography variant="h3">{entityData.title}</Typography>
        <Typography>Average rating: {entityData?.rate ?? "N/A"}</Typography>
      </Paper>
      <Typography sx={{ width: "50%", mt: spaceBetweenComponents }}>
        {entityData.description}
      </Typography>
      <Box
        sx={{
          width: "50%",
          mt: spaceBetweenComponents,
          mb: spaceBetweenComponents,
        }}
      >
        <>
          <Tags tags={entityData.tags} />
          {EventDate}
          {Links}
          <Box sx={{ display: "flex", mt: 1, alignItems: "center" }}>
            <UserAvatar name={entityData?.author} />
            <Typography sx={{ ml: 1 }}>{entityData?.author}</Typography>
          </Box>
        </>
      </Box>
    </Box>
  );
}
