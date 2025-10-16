// components/Header.tsx
import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";

interface BackendUser {
  id: string;
  username: string;
  address?: string;
  contactNumber?: string;
}

const Header: React.FC = () => {
  const [backendUser, setBackendUser] = useState<BackendUser | null>(null);
  const navigate = useNavigate();

  // Load user from localStorage on mount
  useEffect(() => {
    const storedUser = localStorage.getItem("loggedUser");
    if (storedUser) {
      setBackendUser(JSON.parse(storedUser));
    }
  }, []);

  const handleSignOut = () => {
    localStorage.removeItem("backendUser");
    setBackendUser(null);
    navigate("/login");
  };

  return (
    <header className="w-full bg-white shadow-md p-4 flex justify-between items-center">
      <h1 className="text-xl font-bold">My App</h1>

      {backendUser ? (
        <div className="flex items-center space-x-4">
          <div className="text-right">
            <p className="text-sm font-medium">{backendUser.username}</p>
            <p className="text-xs text-gray-500">Mongo ID: {backendUser.id}</p>
          </div>

          <Link
            to="/register-premises"
            className="ml-2 px-3 py-1 bg-blue-500 text-white text-sm rounded hover:bg-blue-600"
          >
            Register Premises
          </Link>

          <button
            onClick={handleSignOut}
            className="ml-2 px-3 py-1 bg-red-500 text-white text-sm rounded hover:bg-red-600"
          >
            Sign Out
          </button>
        </div>
      ) : (
        <div className="flex items-center space-x-2">
          <p className="text-sm text-gray-500">Not logged in</p>
          <Link
            to="/login"
            className="px-3 py-1 bg-blue-500 text-white text-sm rounded hover:bg-blue-600"
          >
            Login
          </Link>
          <Link
            to="/register"
            className="px-3 py-1 bg-green-500 text-white text-sm rounded hover:bg-green-600"
          >
            Register
          </Link>
        </div>
      )}
    </header>
  );
};

export default Header;
