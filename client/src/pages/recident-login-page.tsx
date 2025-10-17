import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../config/axiosInstance"; // Axios instance pointing to backend

const SignInPage: React.FC = () => {
  const [formData, setFormData] = useState({ username: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setErrorMsg("");

    try {
      // Call backend login endpoint
      const res = await axios.post("/api/users/login", formData);
      const user = res.data;

      // Store logged-in user in localStorage
      localStorage.setItem("loggedUser", JSON.stringify(user));

      if (user.assignedTruck != null) {
        navigate("/crew-portal");
      } else {
        navigate("/");
      }
    } catch (err: any) {
      console.error("Login error:", err);
      setErrorMsg(err.response?.data || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4 bg-gray-100">
      <div className="w-full max-w-md bg-white p-6 rounded shadow">
        <h2 className="text-xl font-semibold mb-4 text-center">Sign In</h2>

        {errorMsg && (
          <p className="text-red-600 mb-4 text-center">{errorMsg}</p>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            name="username"
            placeholder="Username"
            value={formData.username}
            onChange={handleChange}
            className="w-full p-2 border rounded"
            required
          />

          <input
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            className="w-full p-2 border rounded"
            required
          />

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 disabled:opacity-50"
          >
            {loading ? "Signing in..." : "Sign In"}
          </button>
        </form>

        <p className="mt-4 text-center text-sm text-gray-600">
          Don't have an account?{" "}
          <button
            className="text-blue-600 hover:underline"
            onClick={() => navigate("/register")}
          >
            Register here
          </button>
        </p>
      </div>
    </div>
  );
};

export default SignInPage;
