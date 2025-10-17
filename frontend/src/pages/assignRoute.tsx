import { useState, useEffect } from "react";
import axios from "../config/axiosIntance";

interface Truck {
  id: string;
  registrationNumber: string;
}

interface Route {
  id: string;
  routeName: string;
}

const AssignRoute = () => {
  const [trucks, setTrucks] = useState<Truck[]>([]);
  const [routes, setRoutes] = useState<Route[]>([]);
  const [selectedTruckId, setSelectedTruckId] = useState<string>("");
  const [selectedRouteId, setSelectedRouteId] = useState<string>("");
  const [response, setResponse] = useState<string>("");

  useEffect(() => {
    const fetchTrucks = async () => {
      try {
        const res = await axios.get("/api/trucks/getAll");
        setTrucks(res.data);
      } catch (err) {
        console.error("Error fetching trucks:", err);
      }
    };

    const fetchRoutes = async () => {
      try {
        const res = await axios.get("/api/routes/getAll");
        setRoutes(res.data);
      } catch (err) {
        console.error("Error fetching routes:", err);
      }
    };

    fetchTrucks();
    fetchRoutes();
  }, []);

  const handleAssignRoute = async () => {
    if (!selectedTruckId || !selectedRouteId) {
      setResponse("Please select both a truck and a route.");
      return;
    }

    try {
      const res = await axios.put(
        `/api/trucks/${selectedTruckId}/assign-route/${selectedRouteId}`
      );
      setResponse(`Route assigned successfully to Truck: ${res.data.registrationNumber}`);
    } catch (err: any) {
      console.error(err);
      setResponse(err?.response?.data || "Error assigning route");
    }
  };

  return (
    <div className="p-4 max-w-md mx-auto bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4">Assign Route to Truck</h2>

      <div className="mb-2">
        <label className="block mb-1">Select Truck:</label>
        <select
          value={selectedTruckId}
          onChange={(e) => setSelectedTruckId(e.target.value)}
          className="w-full p-2 border rounded"
        >
          <option value="">-- Select Truck --</option>
          {trucks.map((truck) => (
            <option key={truck.id} value={truck.id}>
              {truck.registrationNumber}
            </option>
          ))}
        </select>
      </div>

      <div className="mb-2">
        <label className="block mb-1">Select Route:</label>
        <select
          value={selectedRouteId}
          onChange={(e) => setSelectedRouteId(e.target.value)}
          className="w-full p-2 border rounded"
        >
          <option value="">-- Select Route --</option>
          {routes.map((route) => (
            <option key={route.id} value={route.id}>
              {route.routeName}
            </option>
          ))}
        </select>
      </div>

      <button
        onClick={handleAssignRoute}
        className="mt-2 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
      >
        Assign Route
      </button>

      {response && <p className="mt-4 text-gray-700">{response}</p>}
    </div>
  );
};

export default AssignRoute;
