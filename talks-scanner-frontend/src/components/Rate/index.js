import React, { useEffect, useState, useCallback } from "react";

// mui
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Rating from "@mui/material/Rating";
import Button from "@mui/material/Button";

// const
import { TopicStatus } from "@root/const/topicStatus";

// router
import { useParams } from "react-router-dom";

export default function Rate({
  getRate,
  createRate,
  updateRate,
  deleteRate,
  status = null,
}) {
  const isRate = status === null || status === TopicStatus.DONE;

  const { id } = useParams();

  const [rate, setRate] = useState(0);
  const [rateId, setRateId] = useState(0);

  const getRateFromBE = useCallback(async () => {
    if (isRate) {
      const rateFromBe = await getRate({ id }).unwrap();
      if (rateFromBe) {
        setRateId(rateFromBe.id);
        setRate(rateFromBe.rate);
      }
    }
  }, [getRate, id, isRate]);

  useEffect(() => {
    getRateFromBE(rate);
  }, [getRateFromBE, rate]);

  const handleRate = async (rate) => {
    if (rateId) {
      await updateRate({ id, rateId, rate });
    } else {
      await createRate({ id, rate });
    }
    setRate(rate);
    setRateId(rateId);
  };

  const handleClearRate = async () => {
    await deleteRate({ id, rateId });
    setRate(0);
    setRateId(0);
  };

  const ClearButton = rate ? (
    <Button
      sx={{ textTransform: "none" }}
      color="error"
      onClick={handleClearRate}
    >
      Clear
    </Button>
  ) : null;

  return (
    <Box
      sx={{
        display: "flex",
        mt: 1,
        alignItems: "center",
        height: 60,
        alignSelf: "flex-start",
      }}
    >
      {isRate ? (
        <>
          <Typography sx={{ mr: 2 }}>Your rate:</Typography>
          <Rating
            name="rate"
            value={rate}
            onChange={(event, newValue) => {
              handleRate(newValue);
            }}
          />
        </>
      ) : (
        <Typography>You can rate done Topic</Typography>
      )}

      {ClearButton}
    </Box>
  );
}
