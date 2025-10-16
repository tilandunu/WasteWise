// pages/RegisterBinPage.tsx
import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import axios from "../config/axiosIntance";

const RegisterBinPage: React.FC = () => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    binCode: "",
    type: "General",
  });
  const [loading, setLoading] = useState(false);
  const [responseMsg, setResponseMsg] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!user) {
      setResponseMsg("User not logged in");
      return;
    }

    setLoading(true);
    setResponseMsg("");

    try {
      const token = await user.getIdToken();
      const res = await axios.post("/api/bins/create", formData, {
        headers: { Authorization: `Bearer ${token}` },
      });

      setResponseMsg(`Bin registered successfully! ID: ${res.data.id}`);
      setFormData({ binCode: "", type: "General" });
    } catch (err: any) {
      console.error(err);
      setResponseMsg(err.response?.data || "Error registering bin");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto mt-8 p-6 bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4">Register Bin</h2>
      {responseMsg && <p className="mb-4 text-green-600">{responseMsg}</p>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="text"
          name="binCode"
          placeholder="Bin Code"
          value={formData.binCode}
          onChange={handleChange}
          className="w-full p-2 border rounded"
          required
        />

        <select
          name="type"
          value={formData.type}
          onChange={handleChange}
          className="w-full p-2 border rounded"
        >
          <option value="General">General</option>
          <option value="Recyclable">Recyclable</option>
        </select>

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          {loading ? "Registering..." : "Register Bin"}
        </button>
      </form>
    </div>
  );
};

export default RegisterBinPage;
