import React, { useEffect, useState } from "react";
import axios from "../config/axiosIntance";

interface Zone {
  id: string;
  name: string;
}

interface Resident {
  id: string;
  username: string;
  address: string;
  contactNumber: string;
  zone: Zone;
  activated: boolean;
  bin?: any | null;
  premisesType: string;
}

interface Bin {
  id: string;
  type: string;
  status: string;
  assignedUser?: any | null;
}

interface Tag {
  id: string;
  tagId: string;
  active: boolean;
}

const AllUsersPage: React.FC = () => {
  const [users, setUsers] = useState<Resident[]>([]);
  const [bins, setBins] = useState<Bin[]>([]);
  const [tags, setTags] = useState<Tag[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [assigningUserId, setAssigningUserId] = useState<string | null>(null);
  const [selectedBinId, setSelectedBinId] = useState("");
  const [selectedTagId, setSelectedTagId] = useState("");

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await axios.get<Resident[]>("/api/users/all");
        const unassigned = res.data.filter((user) => !user.bin);
        setUsers(unassigned);

        const binsRes = await axios.get<Bin[]>("/api/bins/unassigned");
        console.log("Bins Data " , binsRes.data);
        const availableBins = binsRes.data;
        setBins(availableBins);

        const tagsRes = await axios.get<Tag[]>("/api/tags/all");
        const availableTags = tagsRes.data.filter((t) => t.active);
        setTags(availableTags);
      } catch (err: any) {
        console.error(err);
        setError("Failed to fetch data");
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  const handleAssign = async () => {
    if (!assigningUserId || !selectedBinId || !selectedTagId) return;

    try {
      console.log("Assigning bin to user:", assigningUserId);
      console.log("Selected bin ID:", selectedBinId);
      console.log("Selected tag ID:", selectedTagId);
      // Step 1: Assign tag to bin
      await axios.post("/api/officer/assign-tag", null, { params: { binId: selectedBinId, tagId: selectedTagId } });
      // Step 2: Assign bin to resident
      await axios.post("/api/officer/assign-bin", null, { params: { binId: selectedBinId, residentId: assigningUserId } });

      alert("Bin assigned successfully!");
      // Refresh users list
      const res = await axios.get<Resident[]>("/api/users/all");
      setUsers(res.data.filter((user) => !user.bin));
      setAssigningUserId(null);
      setSelectedBinId("");
      setSelectedTagId("");
    } catch (err: any) {
      console.error(err);
      alert("Failed to assign bin");
    }
  };

  if (loading) return <p className="text-center mt-4">Loading...</p>;
  if (error) return <p className="text-center mt-4 text-red-600">{error}</p>;

  return (
    <div className="max-w-5xl mx-auto mt-8 p-6 bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4 text-center">Residents without Bin</h2>
      {users.length === 0 ? (
        <p className="text-center">All users have been assigned a bin.</p>
      ) : (
        <table className="w-full border-collapse border border-gray-300">
          <thead>
            <tr className="bg-gray-100">
              <th className="border px-4 py-2">Username</th>
              <th className="border px-4 py-2">Address</th>
              <th className="border px-4 py-2">Contact Number</th>
              <th className="border px-4 py-2">Zone</th>
              <th className="border px-4 py-2">Premises Type</th>
              <th className="border px-4 py-2">Activated</th>
              <th className="border px-4 py-2">Action</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td className="border px-4 py-2">{user.username}</td>
                <td className="border px-4 py-2">{user.address}</td>
                <td className="border px-4 py-2">{user.contactNumber}</td>
                <td className="border px-4 py-2">{user.zone?.name || "N/A"}</td>
                <td className="border px-4 py-2">{user.premisesType}</td>
                <td className="border px-4 py-2">{user.activated ? "Yes" : "No"}</td>
                <td className="border px-4 py-2">
                  {assigningUserId === user.id ? (
                    <div className="flex flex-col space-y-1">
                      <select
                        value={selectedBinId}
                        onChange={(e) => setSelectedBinId(e.target.value)}
                        className="border p-1 rounded"
                      >
                        <option value="">Select Bin</option>
                        {bins.map((b) => (
                          <option key={b.id} value={b.id}>{b.type}</option>
                        ))}
                      </select>
                      <select
                        value={selectedTagId}
                        onChange={(e) => setSelectedTagId(e.target.value)}
                        className="border p-1 rounded"
                      >
                        <option value="">Select Tag</option>
                        {tags.map((t) => (
                          <option key={t.id} value={t.id}>{t.tagId}</option>
                        ))}
                      </select>
                      <button
                        onClick={handleAssign}
                        className="px-2 py-1 bg-green-500 text-white rounded hover:bg-green-600"
                      >
                        Confirm
                      </button>
                      <button
                        onClick={() => setAssigningUserId(null)}
                        className="px-2 py-1 bg-gray-300 rounded hover:bg-gray-400"
                      >
                        Cancel
                      </button>
                    </div>
                  ) : (
                    <button
                      onClick={() => setAssigningUserId(user.id)}
                      className="px-2 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
                    >
                      Assign Bin
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default AllUsersPage;
