import React, { useEffect, useState, useMemo } from "react";
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
  active: boolean;
  zone?: Zone;
}

interface Zone {
  id: string;
  name: string;
  description?: string;
}

const Routes: React.FC = () => {
  const [routes, setRoutes] = useState<Route[]>([]);
  const [zones, setZones] = useState<Zone[]>([]);
  const [loading, setLoading] = useState(false);
  const [editingRoute, setEditingRoute] = useState<Route | null>(null);
  const [selectedZoneId, setSelectedZoneId] = useState<string>("");
  
  const { isOpen: isAddOpen, onOpen: onAddOpen, onClose: onAddClose } = useDisclosure();
  const { isOpen: isEditOpen, onOpen: onEditOpen, onClose: onEditClose } = useDisclosure();
  
  const [formData, setFormData] = useState({
    routeName: "",
    zoneId: "",
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
        active: formData.active,
        zone: formData.zoneId ? { id: formData.zoneId } : null
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
        active: formData.active,
        zone: formData.zoneId ? { id: formData.zoneId } : null
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

  const openEditModal = async (route: Route) => {
    // ensure zones are loaded so the Select has options
    if (zones.length === 0) await fetchZones();
    setEditingRoute(route);
    setFormData({
      routeName: route.routeName,
      zoneId: route.zone?.id || "",
      active: route.active
    });
    onEditOpen();
  };

  const openAddModal = async () => {
    if (zones.length === 0) await fetchZones();
    onAddOpen();
  };

  const resetForm = () => {
    setFormData({
      routeName: "",
      zoneId: "",
      active: true
    });
  };

  const handleInputChange = (field: string, value: string | boolean) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const displayedRoutes = useMemo(() => {
    if (!selectedZoneId) return routes;
    return routes.filter((r) => r.zone?.id === selectedZoneId);
  }, [routes, selectedZoneId]);

  return (
    <>
      <Header />
      
      <main className="max-w-7xl mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-6">
          <div>
            <h1 className="text-3xl font-bold">Routes Management</h1>
            <p className="text-default-500 mt-1">Manage all your delivery routes</p>
          </div>
          <div className="flex items-center gap-3">
            <div>
              <label
                htmlFor="zone-filter"
                className="block text-sm text-default-500 mb-1"
              >
                Filter Zone
              </label>
              <select
                id="zone-filter"
                className="rounded border px-2 py-1"
                value={selectedZoneId || "__all"}
                onChange={(e) => setSelectedZoneId(e.target.value === "__all" ? "" : e.target.value)}
              >
                <option value="__all">All Zones</option>
                {zones.map((zone) => (
                  <option key={zone.id} value={zone.id}>{zone.name}</option>
                ))}
              </select>
            </div>

            <Button color="primary" onPress={openAddModal}>
              Add New Route
            </Button>
          </div>
        </div>

        <Divider className="mb-6" />

        {/* Routes Table */}
        <Card>
          <CardBody>
            {loading ? (
              <div className="flex justify-center items-center py-8">
                <p>Loading routes...</p>
              </div>
            ) : (
              <Table aria-label="Routes table">
                <TableHeader>
                  <TableColumn>ROUTE NAME</TableColumn>
                  <TableColumn>ZONE</TableColumn>
                  <TableColumn>STATUS</TableColumn>
                  <TableColumn>ACTIONS</TableColumn>
                </TableHeader>
                <TableBody>
                  {displayedRoutes.map((route) => (
                  <TableRow key={route.id}>
                    <TableCell>
                      <p className="font-medium">{route.routeName}</p>
                    </TableCell>
                    <TableCell>{route.zone?.name || "No Zone"}</TableCell>
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
              )}
          </CardBody>
        </Card>

        {/* Add Route Modal */}
        <Modal isOpen={isAddOpen} onClose={onAddClose} size="lg">
          <ModalContent>
            <ModalHeader>Add New Route</ModalHeader>
            <ModalBody>
              <div className="grid grid-cols-1 gap-4">
                <Input
                  label="Route Name"
                  placeholder="Enter route name"
                  value={formData.routeName}
                  onChange={(e) => handleInputChange("routeName", e.target.value)}
                />
                {zones.length === 0 ? (
                  <p className="text-default-500">No zones available. Please create zones first.</p>
                ) : (
                  <Select
                    label="Zone"
                    placeholder="Select a zone (optional)"
                    selectedKeys={formData.zoneId ? [formData.zoneId] : []}
                    onSelectionChange={(keys) => {
                      const selectedKey = Array.from(keys)[0] as string;
                      handleInputChange("zoneId", selectedKey || "");
                    }}
                  >
                    {zones.map((zone) => (
                      <SelectItem key={zone.id}>
                        {zone.name}
                      </SelectItem>
                    ))}
                  </Select>
                )}
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
        <Modal isOpen={isEditOpen} onClose={onEditClose} size="lg">
          <ModalContent>
            <ModalHeader>Edit Route</ModalHeader>
            <ModalBody>
              <div className="grid grid-cols-1 gap-4">
                <Input
                  label="Route Name"
                  placeholder="Enter route name"
                  value={formData.routeName}
                  onChange={(e) => handleInputChange("routeName", e.target.value)}
                />
                {zones.length === 0 ? (
                  <p className="text-default-500">No zones available. Please create zones first.</p>
                ) : (
                  <Select
                    label="Zone"
                    placeholder="Select a zone (optional)"
                    selectedKeys={formData.zoneId ? [formData.zoneId] : []}
                    onSelectionChange={(keys) => {
                      const selectedKey = Array.from(keys)[0] as string;
                      
                      handleInputChange("zoneId", selectedKey || "");
                    }}
                  >
                    {zones.map((zone) => (
                      <SelectItem key={zone.id}>{zone.name}</SelectItem>
                    ))}
                  </Select>
                )}
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