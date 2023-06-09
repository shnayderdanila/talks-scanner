import React, { useEffect } from "react";

// router
import { useNavigate } from "react-router-dom";

// store
import { useSelector } from "react-redux";
import { isAuthenticated } from "@root/store/authSlice";

export default function Home() {
  const navigate = useNavigate();

  const isAuth = useSelector(isAuthenticated);

  useEffect(() => {
    if (isAuth) {
      navigate("/topics");
    }
  }, [isAuth, navigate]);

  return <></>;
}
