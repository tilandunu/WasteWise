import backend_Path from "../config/axiosIntance";

// Function to handle Google login
export const googleLogin = async (idToken: string) => {
  return await backend_Path.post(
    "/api/auth/google-login",
    {},
    {
      headers: {
        Authorization: `Bearer ${idToken}`,
      },
    }
  );
};
