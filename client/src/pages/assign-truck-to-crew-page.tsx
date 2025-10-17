import React, { useEffect, useState } from "react";
import { Button } from "@heroui/button";
import { Card, CardBody } from "@heroui/card";
import { Select, SelectItem } from "@heroui/select";
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from "@heroui/table";
import { Chip } from "@heroui/chip";
import { Divider } from "@heroui/react";

import Header from "../components/header";
import backend_Path from "../config/axiosInstance";

interface Route {
  id: string;
  routeName: string;
  startLocation: string;
  endLocation: string;
  active: boolean;
}

interface Truck {
  id: string;
  registrationNumber: string;
  model: string;
  capacity?: number;
  status: string;
  available: boolean;
  assignedRoute?: Route;
}

interface CrewMember {
  id: string;
  username: string;
  address?: string;
  contactNumber?: string;
  assignedTruck?: Truck;
}

const AssignTruckToCrew: React.FC = () => {
  const [trucks, setTrucks] = useState<Truck[]>([]);
  const [crewMembers, setCrewMembers] = useState<CrewMember[]>([]);
  const [selectedTruck, setSelectedTruck] = useState<string>("");
  const [selectedCrew, setSelectedCrew] = useState<string>("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchTrucks();
    fetchCrewMembers();
  }, []);

  const fetchTrucks = async () => {
    try {
      const response = await backend_Path.get("/api/trucks/getAll");
      setTrucks(response.data);
    } catch (error) {
      console.error("Error fetching trucks:", error);
    }
  };

  const fetchCrewMembers = async () => {
    try {
      const response = await backend_Path.get("/api/crew/crew-members");
      setCrewMembers(response.data);
    } catch (error) {
      console.error("Error fetching crew members:", error);
    }
  };

  const handleAssignTruck = async () => {
    if (!selectedTruck || !selectedCrew) {
      alert("Please select both a truck and a crew member");
      return;
    }

    try {
      setLoading(true);
      await backend_Path.put(
        `/api/crew/${selectedCrew}/assign-truck/${selectedTruck}`,
      );
      
      // Refresh data
      fetchTrucks();
      fetchCrewMembers();
      
      // Reset selections
      setSelectedTruck("");
      setSelectedCrew("");
      
      alert("Truck assigned to crew member successfully!");
    } catch (error) {
      console.error("Error assigning truck to crew:", error);
      alert("Failed to assign truck to crew member");
    } finally {
      setLoading(false);
    }
  };

  const handleUnassignTruck = async (crewId: string) => {
    if (!confirm("Are you sure you want to unassign this truck?")) return;

    try {
      await backend_Path.put(`/api/crew/${crewId}/unassign-truck`);
      fetchCrewMembers();
      fetchTrucks();
      alert("Truck unassigned successfully!");
    } catch (error) {
      console.error("Error unassigning truck:", error);
      alert("Failed to unassign truck");
    }
  };

  return (
    <>
      <Header />
      
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="mb-6">
          <h1 className="text-3xl font-bold">Assign Truck to Crew</h1>
          <p className="text-default-500 mt-1">Assign trucks to crew members for operational efficiency</p>
        </div>

        <Divider className="mb-6" />

        {/* Assignment Form */}
        <Card className="mb-8">
          <CardBody>
            <h2 className="text-xl font-semibold mb-4">New Assignment</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
              <Select
                label="Select Truck"
                placeholder="Choose a truck"
                selectedKeys={selectedTruck ? [selectedTruck] : []}
                onSelectionChange={(keys) => setSelectedTruck(Array.from(keys)[0] as string)}
              >
                {trucks
                  .filter((truck) => truck.status === "Available")
                  .map((truck) => (
                    <SelectItem key={truck.id}>
                      {truck.registrationNumber} - {truck.model}
                      {truck.capacity && ` (${truck.capacity}L)`}
                      {truck.assignedRoute && ` - Route: ${truck.assignedRoute.routeName}`}
                    </SelectItem>
                  ))}
              </Select>

              <Select
                label="Select Crew Member"
                placeholder="Choose a crew member"
                selectedKeys={selectedCrew ? [selectedCrew] : []}
                onSelectionChange={(keys) => setSelectedCrew(Array.from(keys)[0] as string)}
              >
                {crewMembers
                  .filter((crew) => !crew.assignedTruck)
                  .map((crew) => (
                    <SelectItem key={crew.id}>
                      {crew.username}
                      {crew.contactNumber && ` - ${crew.contactNumber}`}
                    </SelectItem>
                  ))}
              </Select>

              <Button
                color="primary"
                onPress={handleAssignTruck}
                isLoading={loading}
                isDisabled={!selectedTruck || !selectedCrew}
              >
                Assign Truck
              </Button>
            </div>
          </CardBody>
        </Card>

        {/* Current Assignments Table */}
        <Card>
          <CardBody>
            <h2 className="text-xl font-semibold mb-4">Current Truck Assignments</h2>
            <Table aria-label="Truck assignments table">
              <TableHeader>
                <TableColumn>CREW MEMBER</TableColumn>
                <TableColumn>CONTACT</TableColumn>
                <TableColumn>ASSIGNED TRUCK</TableColumn>
                <TableColumn>TRUCK MODEL</TableColumn>
                <TableColumn>TRUCK ROUTE</TableColumn>
                <TableColumn>TRUCK STATUS</TableColumn>
                <TableColumn>ACTIONS</TableColumn>
              </TableHeader>
              <TableBody>
                {crewMembers.map((crew) => (
                  <TableRow key={crew.id}>
                    <TableCell>
                      <div>
                        <p className="font-medium">{crew.username}</p>
                        {crew.address && (
                          <p className="text-sm text-default-500">{crew.address}</p>
                        )}
                      </div>
                    </TableCell>
                    <TableCell>{crew.contactNumber || "N/A"}</TableCell>
                    <TableCell>
                      {crew.assignedTruck ? (
                        <div className="font-medium">
                          {crew.assignedTruck.registrationNumber}
                        </div>
                      ) : (
                        <Chip color="default" size="sm" variant="flat">
                          No Truck
                        </Chip>
                      )}
                    </TableCell>
                    <TableCell>
                      {crew.assignedTruck ? (
                        <div>
                          <p>{crew.assignedTruck.model}</p>
                          {crew.assignedTruck.capacity && (
                            <p className="text-sm text-default-500">
                              Capacity: {crew.assignedTruck.capacity}L
                            </p>
                          )}
                        </div>
                      ) : (
                        "—"
                      )}
                    </TableCell>
                    <TableCell>
                      {crew.assignedTruck?.assignedRoute ? (
                        <div>
                          <p className="font-medium">{crew.assignedTruck.assignedRoute.routeName}</p>
                          <p className="text-sm text-default-500">
                            {crew.assignedTruck.assignedRoute.startLocation} → {crew.assignedTruck.assignedRoute.endLocation}
                          </p>
                        </div>
                      ) : (
                        <Chip color="warning" size="sm" variant="flat">
                          No Route
                        </Chip>
                      )}
                    </TableCell>
                    <TableCell>
                      {crew.assignedTruck ? (
                        <div className="flex flex-col gap-1">
                          <Chip
                            color={
                              crew.assignedTruck.status === "Available"
                                ? "success"
                                : crew.assignedTruck.status === "Assigned"
                                  ? "warning"
                                  : "danger"
                            }
                            size="sm"
                            variant="flat"
                          >
                            {crew.assignedTruck.status}
                          </Chip>
                        </div>
                      ) : (
                        <Chip color="default" size="sm" variant="flat">
                          N/A
                        </Chip>
                      )}
                    </TableCell>
                    <TableCell>
                      {crew.assignedTruck ? (
                        <Button
                          color="danger"
                          size="sm"
                          variant="flat"
                          onPress={() => handleUnassignTruck(crew.id)}
                        >
                          Unassign
                        </Button>
                      ) : (
                        <Chip color="default" size="sm" variant="flat">
                          No Action
                        </Chip>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardBody>
        </Card>
      </main>
    </>
  );
};

export default AssignTruckToCrew;