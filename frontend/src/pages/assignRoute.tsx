import React, { useEffect, useState } from "react";
import axios from "../config/axiosIntance";
import { useAuth } from "../context/AuthContext"; // Firebase Auth context

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
}

const AssignRoute: React.FC = () => {
  const { user } = useAuth();
  const [trucks, setTrucks] = useState<Truck[]>([]);
  const [routes, setRoutes] = useState<Route[]>([]);

  const [selectedTruckId, setSelectedTruckId] = useState<string>("");
  const [selectedRouteId, setSelectedRouteId] = useState<string>("");

  const [loading, setLoading] = useState(false);
  const [responseMsg, setResponseMsg] = useState("");

  // Fetch trucks and routes
  useEffect(() => {
    const fetchData = async () => {
      if (!user) return;

      try {
        const token = await user.getIdToken();

        const [truckRes, routeRes] = await Promise.all([
          axios.get<Truck[]>("/api/trucks/getAll", {
            headers: { Authorization: `Bearer ${token}` },
          }),
          axios.get<Route[]>("/api/routes/getAll", {
            headers: { Authorization: `Bearer ${token}` },
          }),
        ]);

        setTrucks(truckRes.data);
        setRoutes(routeRes.data);
      } catch (err) {
        console.error("Error fetching data:", err);
        setResponseMsg("❌ Failed to load trucks or routes.");
      }
    };

    fetchData();
  }, [user]);

  // Assign route to selected truck
  const handleAssignRoute = async () => {
    if (!selectedTruckId || !selectedRouteId) {
      setResponseMsg("⚠️ Please select both a truck and a route.");
      return;
    }

    try {
      setLoading(true);
      const token = await user?.getIdToken();

      const res = await axios.post<Truck>(
        `/api/trucks/${selectedTruckId}/assign-route/${selectedRouteId}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );

      setResponseMsg(`✅ Route assigned successfully to ${res.data.registrationNumber}.`);
      setTrucks((prev) => prev.map((t) => (t.id === res.data.id ? res.data : t)));
    } catch (err) {
      console.error(err);
      setResponseMsg("❌ Error assigning route.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-xl mx-auto bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4 text-center">Assign Route to Truck</h2>

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

      {/* Select Route */}
      <div className="mb-4">
        <label className="block mb-1 font-medium">Select Route:</label>
        <select
          className="border p-2 w-full rounded focus:ring focus:ring-green-300"
          value={selectedRouteId}
          onChange={(e) => setSelectedRouteId(e.target.value)}
        >
          <option value="">-- Select Route --</option>
          {routes.map((route) => (
            <option key={route.id} value={route.id}>
              {route.routeName}
            </option>
          ))}
        </select>
      </div>

      {/* Assign Button */}
      <button
        className="w-full bg-green-600 text-white py-2 rounded hover:bg-green-700 transition disabled:bg-gray-400"
        onClick={handleAssignRoute}
        disabled={loading}
      >
        {loading ? "Assigning..." : "Assign Route"}
      </button>

      {/* Truck Details */}
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
              </div>
            ))}
        </div>
      )}
    </div>
  );
};

export default AssignRoute;
