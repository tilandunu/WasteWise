import React, { useState } from "react";
import axios from "../config/axiosIntance"; // Your configured axios instance
import { getAuth } from "firebase/auth";
import { app } from "../../auth/firebase";

const AddZonePage: React.FC = () => {
  const [zoneName, setZoneName] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!zoneName.trim()) {
      setMessage("Zone name cannot be empty");
      return;
    }

    setLoading(true);
    setMessage("");

    try {
      // Get current Firebase user
      const auth = getAuth(app);
      const user = auth.currentUser;

      if (!user) {
        setMessage("You must be logged in to add a zone");
        setLoading(false);
        return;
      }

      // Get Firebase ID token
      const token = await user.getIdToken();

      // Call backend endpoint with Authorization header
      const res = await axios.post(
        "/api/zones/create",
        { name: zoneName },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setMessage(`Zone created! ID: ${res.data.id}`);
      setZoneName(""); // Clear input
    } catch (err: any) {
      console.error(err);
      setMessage(err.response?.data || "Error creating zone");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto mt-8 p-6 bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4">Add Zone</h2>

      {message && <p className="mb-4 text-green-600">{message}</p>}

      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="text"
          placeholder="Zone Name"
          value={zoneName}
          onChange={(e) => setZoneName(e.target.value)}
          className="w-full p-2 border rounded"
          required
        />

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          {loading ? "Creating..." : "Add Zone"}
        </button>
      </form>
    </div>
  );
};

export default AddZonePage;
