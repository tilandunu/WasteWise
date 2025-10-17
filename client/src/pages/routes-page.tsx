import React, { useEffect, useState } from "react";
import { Button } from "@heroui/button";
import { Card, CardBody } from "@heroui/card";
import { Input } from "@heroui/input";
import { Select, SelectItem } from "@heroui/select";
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, useDisclosure } from "@heroui/modal";
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
  distance?: number;
  estimatedDuration?: number;
  active: boolean;
}

interface Zone {
  id: string;
  zoneName: string;
  description?: string;
}

const Routes: React.FC = () => {
  const [routes, setRoutes] = useState<Route[]>([]);
  const [zones, setZones] = useState<Zone[]>([]);
  const [loading, setLoading] = useState(false);
  const [editingRoute, setEditingRoute] = useState<Route | null>(null);
  
  const { isOpen: isAddOpen, onOpen: onAddOpen, onClose: onAddClose } = useDisclosure();
  const { isOpen: isEditOpen, onOpen: onEditOpen, onClose: onEditClose } = useDisclosure();
  
  const [formData, setFormData] = useState({
    routeName: "",
    description: "",
    startLocation: "",
    endLocation: "",
    distance: "",
    estimatedDuration: "",
    active: true
  });

  useEffect(() => {
    fetchRoutes();
    fetchZones();
  }, []);

  const fetchRoutes = async () => {
    try {
      setLoading(true);
      const response = await backend_Path.get("/api/routes/getAll");
      console.log(response.data);
      setRoutes(response.data);
    } catch (error) {
      console.error("Error fetching routes:", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchZones = async () => {
    try {
      const response = await backend_Path.get("/api/zones/all");
      setZones(response.data);
    } catch (error) {
      console.error("Error fetching zones:", error);
    }
  };

  const handleAddRoute = async () => {
    try {
      const routeData = {
        routeName: formData.routeName,
        description: formData.description,
        startLocation: formData.startLocation,
        endLocation: formData.endLocation,
        distance: formData.distance ? parseFloat(formData.distance) : null,
        estimatedDuration: formData.estimatedDuration ? parseInt(formData.estimatedDuration) : null,
        active: formData.active,
      };
      
      await backend_Path.post("/api/routes/create", routeData);
      fetchRoutes();
      onAddClose();
      resetForm();
    } catch (error) {
      console.error("Error adding route:", error);
    }
  };

  const handleEditRoute = async () => {
    if (!editingRoute) return;
    
    try {
      const routeData = {
        routeName: formData.routeName,
        description: formData.description,
        startLocation: formData.startLocation,
        endLocation: formData.endLocation,
        distance: formData.distance ? parseFloat(formData.distance) : null,
        estimatedDuration: formData.estimatedDuration ? parseInt(formData.estimatedDuration) : null,
        active: formData.active,
      };
      
      await backend_Path.put(`/api/routes/${editingRoute.id}`, routeData);
      fetchRoutes();
      onEditClose();
      resetForm();
      setEditingRoute(null);
    } catch (error) {
      console.error("Error updating route:", error);
    }
  };

  const handleDeleteRoute = async (routeId: string) => {
    if (confirm("Are you sure you want to delete this route?")) {
      try {
        await backend_Path.delete(`/api/routes/${routeId}`);
        fetchRoutes();
      } catch (error) {
        console.error("Error deleting route:", error);
      }
    }
  };

  const openEditModal = (route: Route) => {
    setEditingRoute(route);
    setFormData({
      routeName: route.routeName,
      description: route.description || "",
      startLocation: route.startLocation,
      endLocation: route.endLocation,
      distance: route.distance?.toString() || "",
      estimatedDuration: route.estimatedDuration?.toString() || "",
      active: route.active
    });
    onEditOpen();
  };

  const resetForm = () => {
    setFormData({
      routeName: "",
      description: "",
      startLocation: "",
      endLocation: "",
      distance: "",
      estimatedDuration: "",
      active: true
    });
  };

  const handleInputChange = (field: string, value: string | boolean) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <>
      <Header />
      
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-6">
          <div>
            <h1 className="text-3xl font-bold">Routes Management</h1>
            <p className="text-default-500 mt-1">Manage all your delivery routes</p>
          </div>
          <Button color="primary" onPress={onAddOpen}>
            Add New Route
          </Button>
        </div>

        <Divider className="mb-6" />

        {/* Routes Table */}
        <Card>
          <CardBody>
            <Table aria-label="Routes table">
              <TableHeader>
                <TableColumn>ROUTE NAME</TableColumn>
                <TableColumn>START LOCATION</TableColumn>
                <TableColumn>END LOCATION</TableColumn>
                <TableColumn>DISTANCE</TableColumn>
                <TableColumn>DURATION</TableColumn>
                <TableColumn>STATUS</TableColumn>
                <TableColumn>ACTIONS</TableColumn>
              </TableHeader>
              <TableBody>
                {routes.map((route) => (
                  <TableRow key={route.id}>
                    <TableCell>
                      <div>
                        <p className="font-medium">{route.routeName}</p>
                        {route.description && (
                          <p className="text-sm text-default-500">{route.description}</p>
                        )}
                      </div>
                    </TableCell>
                    <TableCell>{route.startLocation}</TableCell>
                    <TableCell>{route.endLocation}</TableCell>
                    <TableCell>{route.distance ? `${route.distance} km` : "N/A"}</TableCell>
                    <TableCell>{route.estimatedDuration ? `${route.estimatedDuration} min` : "N/A"}</TableCell>
                    <TableCell>
                      <Chip
                        color={route.active ? "success" : "danger"}
                        variant="flat"
                        size="sm"
                      >
                        {route.active ? "Active" : "Inactive"}
                      </Chip>
                    </TableCell>
                    <TableCell>
                      <div className="flex gap-2">
                        <Button
                          size="sm"
                          color="primary"
                          variant="flat"
                          onPress={() => openEditModal(route)}
                        >
                          Edit
                        </Button>
                        <Button
                          size="sm"
                          color="danger"
                          variant="flat"
                          onPress={() => handleDeleteRoute(route.id)}
                        >
                          Delete
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardBody>
        </Card>

        {/* Add Route Modal */}
        <Modal isOpen={isAddOpen} onClose={onAddClose} size="2xl">
          <ModalContent>
            <ModalHeader>Add New Route</ModalHeader>
            <ModalBody>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <Input
                  label="Route Name"
                  placeholder="Enter route name"
                  value={formData.routeName}
                  onChange={(e) => handleInputChange("routeName", e.target.value)}
                />
                <div className="md:col-span-2">
                  <Input
                    label="Description"
                    placeholder="Enter route description (optional)"
                    value={formData.description}
                    onChange={(e) => handleInputChange("description", e.target.value)}
                  />
                </div>
                <Input
                  label="Start Location"
                  placeholder="Enter start location"
                  value={formData.startLocation}
                  onChange={(e) => handleInputChange("startLocation", e.target.value)}
                />
                <Input
                  label="End Location"
                  placeholder="Enter end location"
                  value={formData.endLocation}
                  onChange={(e) => handleInputChange("endLocation", e.target.value)}
                />
                <Input
                  label="Distance (km)"
                  placeholder="Enter distance in km"
                  type="number"
                  value={formData.distance}
                  onChange={(e) => handleInputChange("distance", e.target.value)}
                />
                <Input
                  label="Estimated Duration (minutes)"
                  placeholder="Enter duration in minutes"
                  type="number"
                  value={formData.estimatedDuration}
                  onChange={(e) => handleInputChange("estimatedDuration", e.target.value)}
                />
              </div>
            </ModalBody>
            <ModalFooter>
              <Button color="danger" variant="light" onPress={onAddClose}>
                Cancel
              </Button>
              <Button color="primary" onPress={handleAddRoute}>
                Add Route
              </Button>
            </ModalFooter>
          </ModalContent>
        </Modal>

        {/* Edit Route Modal */}
        <Modal isOpen={isEditOpen} onClose={onEditClose} size="2xl">
          <ModalContent>
            <ModalHeader>Edit Route</ModalHeader>
            <ModalBody>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <Input
                  label="Route Name"
                  placeholder="Enter route name"
                  value={formData.routeName}
                  onChange={(e) => handleInputChange("routeName", e.target.value)}
                />
                <div className="md:col-span-2">
                  <Input
                    label="Description"
                    placeholder="Enter route description (optional)"
                    value={formData.description}
                    onChange={(e) => handleInputChange("description", e.target.value)}
                  />
                </div>
                <Input
                  label="Start Location"
                  placeholder="Enter start location"
                  value={formData.startLocation}
                  onChange={(e) => handleInputChange("startLocation", e.target.value)}
                />
                <Input
                  label="End Location"
                  placeholder="Enter end location"
                  value={formData.endLocation}
                  onChange={(e) => handleInputChange("endLocation", e.target.value)}
                />
                <Input
                  label="Distance (km)"
                  placeholder="Enter distance in km"
                  type="number"
                  value={formData.distance}
                  onChange={(e) => handleInputChange("distance", e.target.value)}
                />
                <Input
                  label="Estimated Duration (minutes)"
                  placeholder="Enter duration in minutes"
                  type="number"
                  value={formData.estimatedDuration}
                  onChange={(e) => handleInputChange("estimatedDuration", e.target.value)}
                />
                <Select
                  label="Status"
                  placeholder="Select status"
                  selectedKeys={[formData.active.toString()]}
                  onSelectionChange={(keys) => {
                    const value = Array.from(keys)[0] as string;
                    handleInputChange("active", value === "true");
                  }}
                >
                  <SelectItem key="true">Active</SelectItem>
                  <SelectItem key="false">Inactive</SelectItem>
                </Select>
              </div>
            </ModalBody>
            <ModalFooter>
              <Button color="danger" variant="light" onPress={onEditClose}>
                Cancel
              </Button>
              <Button color="primary" onPress={handleEditRoute}>
                Update Route
              </Button>
            </ModalFooter>
          </ModalContent>
        </Modal>
      </main>
    </>
  );
};

export default Routes;