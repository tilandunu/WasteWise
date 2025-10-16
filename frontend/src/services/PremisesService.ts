// src/services/PremisesService.ts
import backend_Path from "../config/axiosIntance"; // your configured axios instance
import { getAuth } from "firebase/auth";
import { app } from "../../auth/firebase";


// Function to register premises
export const registerPremises = async (data: any) => {
  const auth = getAuth(app);
  const user = auth.currentUser;

  if (!user) throw new Error("User not logged in");

  const token = await user.getIdToken();

  console.log("Token in PromisesService:", token);

  return backend_Path.post("/api/premises/register", data, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};
