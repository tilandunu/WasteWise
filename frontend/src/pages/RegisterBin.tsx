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
  bin?: any | null; // bin can be null if not assigned
}

const AllUsersPage: React.FC = () => {
  const [users, setUsers] = useState<Resident[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await axios.get<Resident[]>("/api/users/all");
        // Filter only users without a bin
        const unassigned = res.data.filter((user) => !user.bin);
        setUsers(unassigned);
      } catch (err: any) {
        console.error("Failed to fetch users:", err);
        setError("Failed to fetch users");
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  if (loading) return <p className="text-center mt-4">Loading users...</p>;
  if (error) return <p className="text-center mt-4 text-red-600">{error}</p>;

  return (
    <div className="max-w-4xl mx-auto mt-8 p-6 bg-white rounded shadow">
      <h2 className="text-xl font-bold mb-4 text-center">
        Residents without Bin
      </h2>
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
              <th className="border px-4 py-2">Activated</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td className="border px-4 py-2">{user.username}</td>
                <td className="border px-4 py-2">{user.address}</td>
                <td className="border px-4 py-2">{user.contactNumber}</td>
                <td className="border px-4 py-2">{user.zone?.name || "N/A"}</td>
                <td className="border px-4 py-2">
                  {user.activated ? "Yes" : "No"}
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
