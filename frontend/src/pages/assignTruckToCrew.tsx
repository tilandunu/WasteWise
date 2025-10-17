import React, { useState, useEffect } from "react";
import axios from "../config/axiosIntance";

interface CrewMember {
  id: string;
  username: string;
  assignedTruck?: {
    id: string;
    registrationNumber: string;
  } | null;
}

interface Truck {
  id: string;
  registrationNumber: string;
  // Removed assignedCrew since it's unidirectional now
}

const AssignTruckToCrew: React.FC = () => {
  const [crewMembers, setCrewMembers] = useState<CrewMember[]>([]);
  const [trucks, setTrucks] = useState<Truck[]>([]);
  const [selectedCrewId, setSelectedCrewId] = useState<string>("");
  const [selectedTruckId, setSelectedTruckId] = useState<string>("");

  useEffect(() => {
    fetchCrewMembers();
    fetchTrucks();
  }, []);

  const fetchCrewMembers = async () => {
    try {
      const res = await axios.get("/api/crew/crew-members");
      setCrewMembers(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchTrucks = async () => {
    try {
      const res = await axios.get("/api/trucks/getAll");
      setTrucks(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleAssign = async () => {
    if (!selectedCrewId || !selectedTruckId) {
      alert("Please select both a crew member and a truck");
      return;
    }

    try {
      // Only one API call needed - assign truck to crew member
      await axios.put(`/api/crew/${selectedCrewId}/assign-truck/${selectedTruckId}`);

      alert("Truck assigned to crew member successfully!");
      fetchCrewMembers();
      fetchTrucks();
      
      // Reset selections
      setSelectedCrewId("");
      setSelectedTruckId("");
    } catch (err: any) {
      console.error(err);
      alert(err.response?.data || "Error assigning truck to crew member");
    }
  };

  return (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">Assign Truck to Crew Member</h2>

      <div className="mb-2">
        <label className="block font-medium">Select Crew Member:</label>
        <select
          value={selectedCrewId}
          onChange={(e) => setSelectedCrewId(e.target.value)}
          className="border p-2 w-full"
        >
          <option value="">-- Select Crew Member --</option>
          {crewMembers.map((crew) => (
            <option key={crew.id} value={crew.id}>
              {crew.username}
              {crew.assignedTruck && ` (Currently assigned to: ${crew.assignedTruck.registrationNumber})`}
            </option>
          ))}
        </select>
      </div>

      <div className="mb-4">
        <label className="block font-medium">Select Truck:</label>
        <select
          value={selectedTruckId}
          onChange={(e) => setSelectedTruckId(e.target.value)}
          className="border p-2 w-full"
        >
          <option value="">-- Select Truck --</option>
          {trucks.map((truck) => (
            <option key={truck.id} value={truck.id}>
              {truck.registrationNumber}
            </option>
          ))}
        </select>
      </div>

      <button
        onClick={handleAssign}
        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
      >
        Assign Truck
      </button>
    </div>
  );
};

export default AssignTruckToCrew;