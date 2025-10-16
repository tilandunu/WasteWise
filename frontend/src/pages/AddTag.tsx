import React, { useState } from "react";
import axios from "../config/axiosIntance";
import { useAuth } from "../context/AuthContext";

const CreateTagPage: React.FC = () => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    tagId: "",
    tagType: "QR_CODE",
  });
  const [loading, setLoading] = useState(false);
  const [responseMsg, setResponseMsg] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setResponseMsg("");

    try {
      const token = user ? await user.getIdToken() : null;

      const res = await axios.post("/api/tags/create", formData, {
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      });

      setResponseMsg(`✅ Tag created successfully! ID: ${res.data.id}`);
      setFormData({ tagId: "", tagType: "QR_CODE" });
    } catch (err: any) {
      console.error("Error creating tag:", err);
      setResponseMsg(err.response?.data || "❌ Failed to create tag");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-2xl shadow-md">
      <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">
        Create New Tag
      </h2>

      {responseMsg && (
        <div
          className={`mb-4 p-3 text-center rounded-lg ${
            responseMsg.startsWith("✅")
              ? "bg-green-100 text-green-800"
              : "bg-red-100 text-red-800"
          }`}
        >
          {responseMsg}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-semibold mb-1 text-gray-700">
            Tag ID
          </label>
          <input
            type="text"
            name="tagId"
            value={formData.tagId}
            onChange={handleChange}
            placeholder="Enter unique tag ID (e.g., TAG-001)"
            className="w-full border p-2 rounded-md focus:ring-2 focus:ring-blue-400"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-semibold mb-1 text-gray-700">
            Tag Type
          </label>
          <select
            name="tagType"
            value={formData.tagType}
            onChange={handleChange}
            className="w-full border p-2 rounded-md focus:ring-2 focus:ring-blue-400"
          >
            <option value="QR_CODE">QR Code</option>
            <option value="RFID">RFID</option>
            <option value="SENSOR">Sensor</option>
          </select>
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full py-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-md"
        >
          {loading ? "Creating..." : "Create Tag"}
        </button>
      </form>
    </div>
  );
};

export default CreateTagPage;
