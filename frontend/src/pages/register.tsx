import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../config/axiosIntance"; // Axios instance pointing to backend

interface Zone {
  id: string;
  name: string;
}

// Use const array for premises types
const PREMISES_TYPES = ["HOUSE", "BUSINESS"];

const RegisterPage: React.FC = () => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    address: "",
    contactNumber: "",
    zoneId: "",
    premisesType: "HOUSE", // default value
  });

  const [zones, setZones] = useState<Zone[]>([]);
  const [loading, setLoading] = useState(false);
  const [responseMsg, setResponseMsg] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchZones = async () => {
      try {
        const res = await axios.get("/api/zones/all");
        setZones(res.data);
      } catch (err) {
        console.error("Failed to fetch zones, using mock data.");
      }
    };
    fetchZones();
  }, []);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setResponseMsg("");

    try {
      await axios.post("/api/users/register", formData);
      setResponseMsg("Registration successful");
      navigate("/login");
    } catch (err: any) {
      console.error("Registration error:", err);
      setResponseMsg(err.response?.data || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-4 bg-gray-100">
      <div className="w-full max-w-md bg-white p-6 rounded shadow">
        <h2 className="text-xl font-semibold mb-4 text-center">Register Account</h2>

        {responseMsg && (
          <p
            className={`mb-4 text-center ${
              responseMsg.includes("successful") ? "text-green-600" : "text-red-600"
            }`}
          >
            {responseMsg}
          </p>
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

          <input
            type="text"
            name="address"
            placeholder="Address"
            value={formData.address}
            onChange={handleChange}
            className="w-full p-2 border rounded"
            required
          />

          <input
            type="text"
            name="contactNumber"
            placeholder="Contact Number"
            value={formData.contactNumber}
            onChange={handleChange}
            className="w-full p-2 border rounded"
            required
          />

          <select
            name="zoneId"
            value={formData.zoneId}
            onChange={handleChange}
            className="w-full p-2 border rounded"
            required
          >
            <option value="">Select Zone</option>
            {zones.map((zone) => (
              <option key={zone.id} value={zone.id}>
                {zone.name}
              </option>
            ))}
          </select>

          {/* Premises Type Dropdown */}
          <select
            name="premisesType"
            value={formData.premisesType}
            onChange={handleChange}
            className="w-full p-2 border rounded"
            required
          >
            {PREMISES_TYPES.map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 disabled:opacity-50"
          >
            {loading ? "Registering..." : "Register"}
          </button>
        </form>

        <p className="mt-4 text-center text-sm text-gray-600">
          Already have an account?{" "}
          <button
            className="text-blue-600 hover:underline"
            onClick={() => navigate("/login")}
          >
            Login here
          </button>
        </p>
      </div>
    </div>
  );
};

export default RegisterPage;
