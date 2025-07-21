import React, { useState } from "react";
import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";
import { app } from "../../auth/firebase";
import { useNavigate } from "react-router-dom"; // ✅ Import useNavigate
import { useAuth } from "../context/AuthContext";
import {googleLogin} from '../services/AuthServices'

const SignInPage: React.FC = () => {
  const [isSigningIn, setIsSigningIn] = useState(false);
  const navigate = useNavigate(); // ✅ Initialize useNavigate
  const { setBackendUserId } = useAuth();
  const auth = getAuth(app);

  const signInWithGoogle = async () => {
    const provider = new GoogleAuthProvider();
    setIsSigningIn(true);

    try {
      const result = await signInWithPopup(auth, provider);
      const firebaseUser = result.user;
      const idToken = await firebaseUser.getIdToken();
      const response = await googleLogin(idToken);

      console.log("User info from backend:", response.data.user._id);
      setBackendUserId(response.data.user._id);

      // ✅ Navigate to home after successful login
      navigate("/home");
    } catch (error: any) {
      console.error("Google Sign-In Error:", error.message);
    } finally {
      setIsSigningIn(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4 bg-gray-100">
      <div className="w-full max-w-md bg-white p-6 rounded shadow">
        <h2 className="text-xl font-semibold mb-4 text-center">Sign in</h2>
        <button
          onClick={signInWithGoogle}
          disabled={isSigningIn}
          className="w-full flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded disabled:opacity-50"
        >
          <span>{isSigningIn ? "Signing in..." : "Continue with Google"}</span>
        </button>
      </div>
    </div>
  );
};

export default SignInPage;
