import React, { useState } from "react";

// components
import RegistrationForm from "@root/components/Authorization/RegistrationForm";
import AuthorizationLayout from "@root/components/Authorization/Layout";

//utils
import { createToken } from "@root/utils/createToken";

// lodash
import omit from "lodash/omit";

// store
import { useDispatch } from "react-redux";
import { useSignupMutation } from "@root/store/api/authApi";
import { saveUser } from "@root/store/authSlice";

const errorField = {
  errorMessage: null,
  value: null,
};

export default function SignIn() {
  const dispatch = useDispatch();
  const [signup, { isLoading }] = useSignupMutation();

  const [error, setError] = useState({
    login: { ...errorField },
    email: { ...errorField },
  });

  const handleSubmit = async (payload) => {
    payload = omit(payload, "repeatedPassword");
    payload.sex = payload.sex.toUpperCase();
    try {
      const user = await signup({ ...payload, pathLogo: "/" }).unwrap();
      dispatch(
        saveUser({ user, token: createToken(payload.login, payload.password) })
      );
    } catch (e) {
      if (e.status === 409) {
        const value = e.data.message.substring(
          e.data.message.lastIndexOf("(") + 1,
          e.data.message.lastIndexOf(")")
        );
        setError((prev) =>
          e.data.message.includes("user_login_key")
            ? {
                login: {
                  errorMessage: `User ${value} is already exists.`,
                  value,
                },
                email: { ...prev.email },
              }
            : {
                login: { ...prev.login },
                email: {
                  errorMessage: `Email ${value} is already in use.`,
                  value,
                },
              }
        );
      }
    }
  };

  return (
    <AuthorizationLayout
      label={"Sign up"}
      link={"/signin"}
      isLoading={isLoading}
    >
      <RegistrationForm handleSubmit={handleSubmit} apiError={error} />
    </AuthorizationLayout>
  );
}
