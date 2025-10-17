import React, { useEffect, useState } from "react";
import axios from "../config/axiosIntance"; // Axios instance pointing to backend

interface LoggedUser {
  id: string;
  username: string;
  zone: { id: string; name: string };
  assignedTruck?: {
    id: string;
    registrationNumber: string;
    assignedRoute?: { id: string; routeName: string };
  };
}

interface CollectionEventResponse {
  message: string;
  success: boolean;
}

const CrewPortal: React.FC = () => {
  const [user, setUser] = useState<LoggedUser | null>(null);
  const [tagInput, setTagInput] = useState<string>("");
  const [feedback, setFeedback] = useState<string>("");

  useEffect(() => {
    const storedUser = localStorage.getItem("loggedUser");
    if (storedUser) setUser(JSON.parse(storedUser));
  }, []);

  const handleTagSubmit = async () => {
    if (!tagInput || !user?.assignedTruck?.assignedRoute) return;

    try {
      const res = await axios.post<CollectionEventResponse>("/collection-events", {
        tagId: tagInput,
        crewId: user.id,
        routeId: user.assignedTruck.assignedRoute.id,
      });

      if (res.data.success) {
        setFeedback(`✅ ${res.data.message}`);
        setTagInput(""); // reset input
      } else {
        setFeedback(`⚠️ ${res.data.message}`);
      }
    } catch (error: any) {
      setFeedback(`❌ ${error.response?.data || "Server error"}`);
    }
  };

  return (
    <div className="crew-portal p-4">
      <h1 className="text-2xl font-bold mb-4">CrewPortal Dashboard</h1>

      {user ? (
        <div className="mb-4">
          <p>
            Logged in as: <strong>{user.username}</strong>
          </p>
          <p>
            Truck: <strong>{user.assignedTruck?.registrationNumber || "N/A"}</strong>
          </p>
          <p>
            Route: <strong>{user.assignedTruck?.assignedRoute?.routeName || "N/A"}</strong>
          </p>
        </div>
      ) : (
        <p>Loading user info...</p>
      )}

      <div className="mb-4">
        <input
          type="text"
          placeholder="Enter or scan bin TagID"
          value={tagInput}
          onChange={(e) => setTagInput(e.target.value)}
          className="border p-2 rounded mr-2"
        />
        <button
          onClick={handleTagSubmit}
          className="bg-blue-500 text-white px-4 py-2 rounded"
        >
          Record Collection
        </button>
      </div>

      {feedback && <p className="mt-2">{feedback}</p>}
    </div>
  );
};

export default CrewPortal;
