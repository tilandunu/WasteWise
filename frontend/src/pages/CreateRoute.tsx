import React, { useState, useEffect } from "react";
import axios from "../config/axiosIntance"; // ✅ your configured axios with Firebase token support

interface Zone {
  id: string;
  zoneName: string;
}

interface Route {
  id: string;
  routeName: string;
  active: boolean;
  zone?: Zone;
}

const CreateRoute: React.FC = () => {
  const [routeName, setRouteName] = useState("");
  const [selectedZoneId, setSelectedZoneId] = useState("");
  const [zones, setZones] = useState<Zone[]>([]);
  const [routes, setRoutes] = useState<Route[]>([]);
  const [loading, setLoading] = useState(false);

  // ✅ Fetch routes and zones on load
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [routesRes, zonesRes] = await Promise.all([
          axios.get<Route[]>("/api/routes"),
          axios.get<Zone[]>("/api/zones"),
        ]);
        setRoutes(routesRes.data);
        setZones(zonesRes.data);
      } catch (err) {
        console.error("Error fetching data:", err);
        alert("Error fetching routes or zones");
      }
    };
    fetchData();
  }, []);

  // ✅ Create Route
  const handleCreateRoute = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!routeName || !selectedZoneId) {
      alert("Please enter route name and select a zone");
      return;
    }

    setLoading(true);
    try {
      const newRoute = {
        routeName,
        active: true,
        zone: { id: selectedZoneId },
      };
      const res = await axios.post<Route>("/api/routes", newRoute);

      setRoutes(prev => [...prev, res.data]);
      setRouteName("");
      setSelectedZoneId("");
      alert("✅ Route created successfully!");
    } catch (err) {
      console.error("Error creating route:", err);
      alert("Error creating route");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-xl mx-auto">
      <h2 className="text-2xl font-bold mb-4 text-center">Create Route</h2>

      {/* --- Route Creation Form --- */}
      <form onSubmit={handleCreateRoute} className="bg-white p-4 shadow rounded">
        <div className="mb-3">
          <label className="block mb-1 font-medium">Route Name:</label>
          <input
            type="text"
            value={routeName}
            onChange={e => setRouteName(e.target.value)}
            className="border p-2 w-full rounded"
            placeholder="e.g. North Zone Route"
          />
        </div>

        <div className="mb-3">
          <label className="block mb-1 font-medium">Select Zone:</label>
          <select
            value={selectedZoneId}
            onChange={e => setSelectedZoneId(e.target.value)}
            className="border p-2 w-full rounded"
          >
            <option value="">-- Select Zone --</option>
            {zones.map(zone => (
              <option key={zone.id} value={zone.id}>
                {zone.zoneName}
              </option>
            ))}
          </select>
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:bg-gray-400"
        >
          {loading ? "Creating..." : "Create Route"}
        </button>
      </form>

      {/* --- Display Existing Routes --- */}
      <div className="mt-6">
        <h3 className="text-lg font-semibold mb-2">Existing Routes</h3>
        {routes.length === 0 ? (
          <p className="text-gray-500">No routes available.</p>
        ) : (
          <ul className="space-y-2">
            {routes.map(route => (
              <li key={route.id} className="p-3 border rounded bg-gray-50">
                <p><strong>Name:</strong> {route.routeName}</p>
                <p>
                  <strong>Zone:</strong> {route.zone ? route.zone.zoneName : "None"}
                </p>
                <p>
                  <strong>Status:</strong>{" "}
                  <span className={route.active ? "text-green-600" : "text-red-600"}>
                    {route.active ? "Active" : "Inactive"}
                  </span>
                </p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default CreateRoute;
