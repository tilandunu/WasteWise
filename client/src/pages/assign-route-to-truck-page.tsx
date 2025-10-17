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
  description?: string;
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

const AssignRouteToTruck: React.FC = () => {
  const [routes, setRoutes] = useState<Route[]>([]);
  const [trucks, setTrucks] = useState<Truck[]>([]);
  const [selectedRoute, setSelectedRoute] = useState<string>("");
  const [selectedTruck, setSelectedTruck] = useState<string>("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchRoutes();
    fetchTrucks();
  }, []);

  const fetchRoutes = async () => {
    try {
      const response = await backend_Path.get("/api/routes/active");
      setRoutes(response.data);
    } catch (error) {
      console.error("Error fetching routes:", error);
    }
  };

  const fetchTrucks = async () => {
    try {
      const response = await backend_Path.get("/api/trucks/getAll");
      setTrucks(response.data);
    } catch (error) {
      console.error("Error fetching trucks:", error);
    }
  };

  const handleAssignRoute = async () => {
    if (!selectedRoute || !selectedTruck) {
      alert("Please select both a route and a truck");
      return;
    }

    try {
      setLoading(true);
      await backend_Path.put(
        `/api/trucks/${selectedTruck}/assign-route/${selectedRoute}`,
      );
      
      // Refresh trucks list
      fetchTrucks();
      
      // Reset selections
      setSelectedRoute("");
      setSelectedTruck("");
      
      alert("Route assigned to truck successfully!");
    } catch (error) {
      console.error("Error assigning route to truck:", error);
      alert("Failed to assign route to truck");
    } finally {
      setLoading(false);
    }
  };

  const handleUnassignRoute = async (truckId: string) => {
    if (!confirm("Are you sure you want to unassign this route?")) return;

    try {
      await backend_Path.put(`/api/trucks/${truckId}/unassign-route`);
      fetchTrucks();
      alert("Route unassigned successfully!");
    } catch (error) {
      console.error("Error unassigning route:", error);
      alert("Failed to unassign route");
    }
  };

  return (
    <>
      <Header />
      
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="mb-6">
          <h1 className="text-3xl font-bold">Assign Route to Truck</h1>
          <p className="text-default-500 mt-1">Assign delivery routes to trucks for efficient operations</p>
        </div>

        <Divider className="mb-6" />

        {/* Assignment Form */}
        <Card className="mb-8">
          <CardBody>
            <h2 className="text-xl font-semibold mb-4">New Assignment</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
              <Select
                label="Select Route"
                placeholder="Choose a route"
                selectedKeys={selectedRoute ? [selectedRoute] : []}
                onSelectionChange={(keys) => setSelectedRoute(Array.from(keys)[0] as string)}
              >
                {routes.map((route) => (
                  <SelectItem key={route.id}>
                    {route.routeName} ({route.startLocation} → {route.endLocation})
                  </SelectItem>
                ))}
              </Select>

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
                    </SelectItem>
                  ))}
              </Select>

              <Button
                color="primary"
                onPress={handleAssignRoute}
                isLoading={loading}
                isDisabled={!selectedRoute || !selectedTruck}
              >
                Assign Route
              </Button>
            </div>
          </CardBody>
        </Card>

        {/* Current Assignments Table */}
        <Card>
          <CardBody>
            <h2 className="text-xl font-semibold mb-4">Current Route Assignments</h2>
            <Table aria-label="Route assignments table">
              <TableHeader>
                <TableColumn>TRUCK</TableColumn>
                <TableColumn>MODEL</TableColumn>
                <TableColumn>CAPACITY</TableColumn>
                <TableColumn>ASSIGNED ROUTE</TableColumn>
                <TableColumn>ROUTE DETAILS</TableColumn>
                <TableColumn>STATUS</TableColumn>
                <TableColumn>ACTIONS</TableColumn>
              </TableHeader>
              <TableBody>
                {trucks.map((truck) => (
                  <TableRow key={truck.id}>
                    <TableCell>
                      <div className="font-medium">{truck.registrationNumber}</div>
                    </TableCell>
                    <TableCell>{truck.model}</TableCell>
                    <TableCell>{truck.capacity ? `${truck.capacity}L` : "N/A"}</TableCell>
                    <TableCell>
                      {truck.assignedRoute ? (
                        <div>
                          <p className="font-medium">{truck.assignedRoute.routeName}</p>
                        </div>
                      ) : (
                        <Chip color="default" variant="flat" size="sm">
                          No Route
                        </Chip>
                      )}
                    </TableCell>
                    <TableCell>
                      {truck.assignedRoute ? (
                        <div className="text-sm">
                          <p>{truck.assignedRoute.startLocation} → {truck.assignedRoute.endLocation}</p>
                          {truck.assignedRoute.description && (
                            <p className="text-default-500">{truck.assignedRoute.description}</p>
                          )}
                        </div>
                      ) : (
                        "—"
                      )}
                    </TableCell>
                    <TableCell>
                      <div className="flex flex-col gap-1">
                        <Chip
                          color={
                            truck.status === "Available"
                              ? "success"
                              : truck.status === "Assigned"
                                ? "warning"
                                : "danger"
                          }
                          variant="flat"
                          size="sm"
                        >
                          {truck.status}
                        </Chip>
                      </div>
                    </TableCell>
                    <TableCell>
                      {truck.assignedRoute ? (
                        <Button
                          color="danger"
                          size="sm"
                          variant="flat"
                          onPress={() => handleUnassignRoute(truck.id)}
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

export default AssignRouteToTruck;