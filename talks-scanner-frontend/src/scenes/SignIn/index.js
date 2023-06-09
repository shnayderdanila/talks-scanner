import React, { useState } from "react";

// components
import LoginForm from "@root/components/Authorization/LoginForm";
import AuthorizationLayout from "@root/components/Authorization/Layout";

// util
import { createToken } from "@root/utils/createToken";

// store
import { useDispatch } from "react-redux";
import { useLazyGetUserQuery } from "@root/store/api/authApi";
import { saveUser, setToken } from "@root/store/authSlice";

export default function SignIn() {
  const dispatch = useDispatch();
  const [getUser, result] = useLazyGetUserQuery();

  const [error, setError] = useState("");

  const handleSubmit = async (payload) => {
    try {
      const token = createToken(payload.login, payload.password);
      dispatch(setToken({ token }));

      const user = await getUser().unwrap();

      dispatch(saveUser({ user, token }));
    } catch (e) {
      if (e.status === 401) {
        setError("Wrong login or password.");
      }
    }
  };

  return (
    <AuthorizationLayout
      label={"Sign in"}
      link={"/signup"}
      isLoading={result.isLoading}
    >
      <LoginForm handleSubmit={handleSubmit} apiError={error} />
    </AuthorizationLayout>
  );
}
