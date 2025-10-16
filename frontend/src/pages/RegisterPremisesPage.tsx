// pages/RegisterPremisesPage.tsx
import React, { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import axios from "../config/axiosIntance"; // your axios instance

interface Zone {
  id: string;
  name: string;
}

const RegisterPremisesPage: React.FC = () => {
  const { user } = useAuth();
  const [zones, setZones] = useState<Zone[]>([]);
  const [formData, setFormData] = useState({
    address: "",
    type: "Household",
    contactNumber: "",
    zoneId: "68f1118b5aa47df7c3816c9c",
    binId: "", // <-- replace binType with binId
    tagId: "",
  });

  const [loading, setLoading] = useState(false);
  const [responseMsg, setResponseMsg] = useState("");

  // Simulate fetching zones (replace with API call if available)
  useEffect(() => {
    setZones([
      { id: "68f1118b5aa47df7c3816c9c", name: "Colombo Central" },
      { id: "68f111915aa47df7c3816c9d", name: "Colombo South" },
    ]);
  }, []);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
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
      const uid = user.uid; // Firebase UID

      const res = await axios.post(
        "/api/premises/register",
        {
          ...formData,
          ownerUid: uid, // Attach owner UID
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setResponseMsg(`Premises registered! ID: ${res.data.premisesId}`);
      setFormData({
        address: "",
        type: "Household",
        contactNumber: "",
        zoneId: zones[0]?.id || "",
        binId: "",
        tagId: "",
      });
    } catch (err: any) {
      console.error("Axios error:", err);
      setResponseMsg(err.response?.data || "Error registering premises");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-lg mx-auto mt-8 p-6 bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4">Register Premises & Assign Bin</h2>
      {responseMsg && <p className="mb-4 text-green-600">{responseMsg}</p>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="text"
          name="address"
          placeholder="Address"
          value={formData.address}
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
          <option value="Household">Household</option>
          <option value="Business">Business</option>
        </select>

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

        <input
          type="text"
          name="binId"
          placeholder="Bin ID"
          value={formData.binId}
          onChange={handleChange}
          className="w-full p-2 border rounded"
          required
        />

        <input
          type="text"
          name="tagId"
          placeholder="Tag ID (optional)"
          value={formData.tagId}
          onChange={handleChange}
          className="w-full p-2 border rounded"
        />

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          {loading ? "Registering..." : "Register Premises"}
        </button>
      </form>
    </div>
  );
};

export default RegisterPremisesPage;
