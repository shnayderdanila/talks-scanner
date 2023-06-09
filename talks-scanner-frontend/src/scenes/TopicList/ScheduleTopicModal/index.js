import React, { useState } from "react";

// mui
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";

// dayjs
import dayjs from "dayjs";

// mui calendar
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { StaticDateTimePicker } from "@mui/x-date-pickers/StaticDateTimePicker";

// store
import {
  useUpdateScheduleMutation,
  useChangeTopicStatusMutation,
} from "@root/store/api/topicApi";

// modal
import ModalLayout from "@root/components/Modal";

// const
import { TopicStatus } from "@root/const/topicStatus";

export default function ScheduleTopicModal({ open, handleClose, topicData }) {
  const [changeTopicStatus] = useChangeTopicStatusMutation();
  const [updateSchedule] = useUpdateScheduleMutation();

  const handleSubmit = async () => {
    if (topicData) {
      const status = await changeTopicStatus({
        id: topicData.id,
        body: { status: TopicStatus.SCHEDULED },
      }).unwrap();
      if (status) {
        await updateSchedule({
          id: topicData.id,
          body: { eventDate: eventDate.format("YYYY-MM-DD HH:mm:ss") },
        }).unwrap();
        handleClose();
      }
    }
  };

  const [eventDate, setEventDate] = useState(dayjs());

  return (
    <ModalLayout
      open={open}
      handleClose={handleClose}
      label={"Schedule Topic"}
      isControlled={false}
    >
      <Box
        sx={{ display: "flex", flexDirection: "column", alignItems: "center" }}
      >
        <Typography>
          Date: {eventDate.format("YYYY-MM-DD HH:mm:ss")}{" "}
        </Typography>
        <LocalizationProvider dateAdapter={AdapterDayjs} sx={{ width: "100%" }}>
          <StaticDateTimePicker
            value={eventDate}
            onChange={(newValue) => setEventDate(newValue)}
            minDateTime={dayjs()}
            onAccept={handleSubmit}
            onClose={handleClose}
          />
        </LocalizationProvider>
      </Box>
    </ModalLayout>
  );
}
