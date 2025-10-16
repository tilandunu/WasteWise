import React, { useEffect, useState } from "react";
import axios from "../config/axiosIntance";
import { useAuth } from "../context/AuthContext"; // Firebase Auth context

interface User {
  id: string;
  name: string;
  email: string;
}

interface Route {
  id: string;
  routeName: string;
}

interface Truck {
  id: string;
  registrationNumber: string;
  model: string;
  status: string;
  route?: Route;
  assignedUsers: User[];
}

const AssignTruck: React.FC = () => {
  const { user } = useAuth(); // Firebase user
  const [trucks, setTrucks] = useState<Truck[]>([]);
  const [users, setUsers] = useState<User[]>([]);


  const [selectedTruckId, setSelectedTruckId] = useState<string>("");
  const [selectedUserId, setSelectedUserId] = useState<string>("");


  const [loading, setLoading] = useState(false);
  const [responseMsg, setResponseMsg] = useState("");

  // Fetch trucks, users, routes with Firebase auth token
  useEffect(() => {
    const fetchData = async () => {
      if (!user) return;

      try {
        const token = await user.getIdToken();

        const [truckRes, userRes] = await Promise.all([
          axios.get<Truck[]>("/api/trucks/getAll", {
            headers: { Authorization: `Bearer ${token}` },
          }),
          axios.get<User[]>("/api/auth/users", {
            headers: { Authorization: `Bearer ${token}` },
          }),
        //   axios.get<Route[]>("/routes", {
        //     headers: { Authorization: `Bearer ${token}` },
        //   }),
        ]);

        setTrucks(truckRes.data);
        setUsers(userRes.data);
        //setRoutes(routeRes.data);
        console.log("trucks", truckRes.data);
      } catch (err) {
        console.error("Error fetching data:", err);
        setResponseMsg("❌ Failed to load data. Please try again.");
      }
    };

    fetchData();
  }, [user]);

  // Assign user to truck
  const handleAssignUser = async () => {
    if (!selectedTruckId || !selectedUserId) {
      setResponseMsg("⚠️ Please select a truck and user.");
      return;
    }

    try {
      setLoading(true);
      const token = await user?.getIdToken();

      const res = await axios.post<Truck>(
        `/api/trucks/${selectedTruckId}/assign-user/${selectedUserId}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );

      setResponseMsg(`✅ User assigned successfully to ${res.data.registrationNumber}.`);
      setTrucks((prev) => prev.map((t) => (t.id === res.data.id ? res.data : t)));
    } catch (err) {
      console.error(err);
      setResponseMsg("❌ Error assigning user.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-xl mx-auto bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4 text-center">Assign Trucks</h2>

      {responseMsg && (
        <div className="mb-4 p-2 bg-gray-100 border rounded text-center text-gray-700">
          {responseMsg}
        </div>
      )}

      {/* Select Truck */}
      <div className="mb-4">
        <label className="block mb-1 font-medium">Select Truck:</label>
        <select
          className="border p-2 w-full rounded focus:ring focus:ring-blue-300"
          value={selectedTruckId}
          onChange={(e) => setSelectedTruckId(e.target.value)}
        >
          <option value="">-- Select Truck --</option>
          {trucks.map((truck) => (
            <option key={truck.id} value={truck.id}>
              {truck.registrationNumber} — {truck.model}
            </option>
          ))}
        </select>
      </div>

      {/* Assign User */}
      <div className="mb-4">
        <label className="block mb-1 font-medium">Assign User (Crew):</label>
        <select
          className="border p-2 w-full rounded focus:ring focus:ring-blue-300"
          value={selectedUserId}
          onChange={(e) => setSelectedUserId(e.target.value)}
        >
          <option value="">-- Select User --</option>
          {users.map((user) => (
            <option key={user.id} value={user.id}>
              {user.name} ({user.email})
            </option>
          ))}
        </select>
        <button
          className="mt-2 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition disabled:bg-gray-400"
          onClick={handleAssignUser}
          disabled={loading}
        >
          {loading ? "Assigning..." : "Assign User to Truck"}
        </button>
      </div>

      {/* Truck details */}
      {selectedTruckId && (
        <div className="mt-6 p-4 border rounded bg-gray-50">
          <h3 className="font-semibold mb-2">Truck Details</h3>
          {trucks
            .filter((t) => t.id === selectedTruckId)
            .map((truck) => (
              <div key={truck.id}>
                <p>Registration: {truck.registrationNumber}</p>
                <p>Model: {truck.model}</p>
                <p>Status: {truck.status}</p>
                <p>Route: {truck.route?.routeName || "Not assigned"}</p>
                <p>
                  Crew:{" "}
                  {truck.assignedUsers.length > 0
                    ? truck.assignedUsers.map((u) => u.name).join(", ")
                    : "None"}
                </p>
              </div>
            ))}
        </div>
      )}
    </div>
  );
};

export default AssignTruck;
