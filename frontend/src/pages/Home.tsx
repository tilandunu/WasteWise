// components/Header.tsx
import React from "react";
import { useAuth } from "../context/AuthContext";
import { getAuth, signOut } from "firebase/auth";
import { app } from "../../auth/firebase";
import { Link, useNavigate } from "react-router-dom"; // ✅ Import useNavigate

const Header: React.FC = () => {
  const { user, loading } = useAuth();
  const navigate = useNavigate(); // ✅ Initialize useNavigate
  const { backendUserId } = useAuth();
  console.log("Backend User ID:", backendUserId);

  const handleSignOut = async () => {
    try {
      const auth = getAuth(app);
      await signOut(auth);
      navigate("/"); // ✅ Navigate to the sign-in page after signing out
    } catch (err) {
      console.error("Error signing out:", err);
    }
  };

  return (
    <header className="w-full bg-white shadow-md p-4 flex justify-between items-center">
      <h1 className="text-xl font-bold">My App</h1>

      {loading ? (
        <p className="text-sm text-gray-500">Loading...</p>
      ) : user ? (
        <div className="flex items-center space-x-4">
          <div className="text-right">
            <p className="text-sm font-medium">{user.displayName || "Anonymous"}</p>
            <p className="text-xs text-gray-500">{user.email}</p>
            {backendUserId && (
              <p className="text-xs text-gray-500">Mongo ID: {backendUserId}</p>
            )}
          </div>
          {user.photoURL ? (
            <img
              src={user.photoURL}
              alt="User Avatar"
              className="w-10 h-10 rounded-full border"
            />
          ) : (
            <div className="w-10 h-10 rounded-full bg-gray-300 flex items-center justify-center text-xs">
              N/A
            </div>
          )}
          <button
            onClick={handleSignOut}
            className="ml-2 px-3 py-1 bg-red-500 text-white text-sm rounded hover:bg-red-600"
          >
            Sign Out
          </button>
        </div>
      ) : (
        <p className="text-sm text-gray-500">Not logged in</p>
      )}

      <Link to="/register-premises" className="px-3 py-1 bg-blue-500 text-white text-sm rounded hover:bg-blue-600" /> 
      <Link to="/assign-truck" className="px-3 py-1 bg-green-500 text-white text-sm rounded hover:bg-green-600" /> 
    </header>
  );
};

export default Header;
