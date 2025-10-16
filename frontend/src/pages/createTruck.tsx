import React, { useState } from "react";
import { useAuth } from "../context/AuthContext"; // Firebase Auth context
import axios from "../config/axiosIntance"; // Your axios instance

interface Truck {
  registrationNumber: string;
  model: string;
  status: string;
}

const CreateTruck: React.FC = () => {
  const { user } = useAuth(); // Get current Firebase user
  const [formData, setFormData] = useState<Truck>({
    registrationNumber: "",
    model: "",
    status: "ACTIVE",
  });

  const [loading, setLoading] = useState(false);
  const [responseMsg, setResponseMsg] = useState("");

  // Handle form input changes
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Handle form submission
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!user) {
      setResponseMsg("❌ User not logged in.");
      return;
    }

    setLoading(true);
    setResponseMsg("");

    try {
      // ✅ Get Firebase token
      const token = await user.getIdToken();

      // ✅ Send POST request to backend with Authorization header
      const res = await axios.post(
        "/api/trucks/create", // backend endpoint (adjust if needed)
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // ✅ Handle success
      setResponseMsg(`✅ Truck ${res.data.registrationNumber} created successfully!`);
      setFormData({
        registrationNumber: "",
        model: "",
        status: "ACTIVE",
      });
    } catch (err: any) {
      console.error("Axios error:", err);
      setResponseMsg(
        err.response?.data?.message || "❌ Error creating truck. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto mt-8 p-6 bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4 text-center">Create New Truck</h2>

      {responseMsg && (
        <div className="mb-4 p-2 bg-gray-100 border rounded text-center text-gray-700">
          {responseMsg}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Registration Number */}
        <div>
          <label className="block mb-1 font-medium">Registration Number</label>
          <input
            type="text"
            name="registrationNumber"
            placeholder="e.g., WP KA-1234"
            value={formData.registrationNumber}
            onChange={handleChange}
            className="w-full p-2 border rounded focus:outline-none focus:ring focus:ring-blue-300"
            required
          />
        </div>

        {/* Model */}
        <div>
          <label className="block mb-1 font-medium">Model</label>
          <input
            type="text"
            name="model"
            placeholder="e.g., Isuzu NKR"
            value={formData.model}
            onChange={handleChange}
            className="w-full p-2 border rounded focus:outline-none focus:ring focus:ring-blue-300"
            required
          />
        </div>

        {/* Status */}
        <div>
          <label className="block mb-1 font-medium">Status</label>
          <select
            name="status"
            value={formData.status}
            onChange={handleChange}
            className="w-full p-2 border rounded focus:outline-none focus:ring focus:ring-blue-300"
          >
            <option value="ACTIVE">Active</option>
            <option value="INACTIVE">Inactive</option>
            <option value="MAINTENANCE">Maintenance</option>
          </select>
        </div>

        {/* Submit button */}
        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition disabled:bg-gray-400"
        >
          {loading ? "Creating..." : "Create Truck"}
        </button>
      </form>
    </div>
  );
};

export default CreateTruck;
