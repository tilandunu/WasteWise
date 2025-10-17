import React, { useEffect, useState } from "react";
import axios from "../config/axiosInstance";
import Header from "../components/header";
import {
  Card,
  CardHeader,
  CardBody,
} from "@heroui/card";
import { Divider } from "@heroui/divider";
import {
  Table,
  TableHeader,
  TableColumn,
  TableBody,
  TableRow,
  TableCell,
} from "@heroui/table";
import { Button } from "@heroui/button";
import { Chip } from "@heroui/chip";
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, useDisclosure } from "@heroui/modal";
import { Spinner } from "@heroui/spinner";
import { Switch, Input } from "@heroui/react";

interface LoggedUser {
  id: string;
  username: string;
  zone: { id: string; name: string };
  assignedTruck?: {
    id: string;
    registrationNumber: string;
    assignedRoute?: { id: string; routeName: string };
  };
}

interface Bin {
  id: string;
  binCode: string;
  type: string;
  status: string;
  tag: { tagId: string };
}

interface Tag {
  id: string;
  tagId: string;
  active: boolean;
  weight: number;
  fillLevel: number;
}

const CrewPortal: React.FC = () => {
  const [user, setUser] = useState<LoggedUser | null>(null);
  const [bins, setBins] = useState<Bin[]>([]);
  const [loading, setLoading] = useState(true);

  // Force fail switch
  const [forceFail, setForceFail] = useState(false);

  // Scan modal state
  const [currentBin, setCurrentBin] = useState<Bin | null>(null);
  const [tagInfo, setTagInfo] = useState<Tag | null>(null);
  const [manualTagId, setManualTagId] = useState("");
  const [scanning, setScanning] = useState(false);

  const scanModal = useDisclosure(); // spinner + success
  const scanFailModal = useDisclosure(); // fail modal

  useEffect(() => {
    const storedUser = localStorage.getItem("loggedUser");
    if (storedUser) setUser(JSON.parse(storedUser));

    const fetchBins = async () => {
      try {
        const binsRes = await axios.get<Bin[]>("/api/bins/assigned");
        setBins(binsRes.data);
      } catch (err) {
        console.error("Failed to fetch bins", err);
      } finally {
        setLoading(false);
      }
    };

    fetchBins();
  }, []);

  const handleScanClick = (bin: Bin) => {
    setCurrentBin(bin);
    setTagInfo(null);
    setManualTagId("");

    // Open spinner modal
    setScanning(true);
    scanModal.onOpen();

    setTimeout(async () => {
      setScanning(false);

      try {
        const tagIdToUse = forceFail ? "" : bin.tag.tagId;
        if (!tagIdToUse) throw new Error("Forced or missing Tag ID");

        const res = await axios.get<Tag>(`/api/bins/scan/${tagIdToUse}`);
        setTagInfo(res.data);
      } catch (err) {
        // Close spinner and open fail modal
        scanModal.onOpenChange();
        scanFailModal.onOpen();
      }
    }, 1500);
  };

  const handleManualScan = async () => {
    if (!manualTagId) return alert("Enter a Tag ID");

    try {
      const res = await axios.get<Tag>(`/api/bins/scan/${manualTagId}`);
      setTagInfo(res.data);
      scanFailModal.onOpenChange(); // close fail modal
      scanModal.onOpen(); // reopen main modal with tag info
    } catch (err) {
      alert("❌ Invalid Tag ID. Try again.");
    }
  };

  const handleMarkCollected = async () => {
    if (!currentBin) return;
    const tagIdToUse = tagInfo?.tagId;
    if (!tagIdToUse) return alert("Tag ID missing");

    try {
      await axios.post(`/api/bins/collect`, {
        binId: currentBin.id,
        tagId: tagIdToUse,
        crewId: user?.id,
      });
      alert(`✅ Bin ${currentBin.binCode} marked as collected.`);

      // Close modal and refresh bins
      scanModal.onOpenChange();
      const binsRes = await axios.get<Bin[]>("/api/bins/assigned");
      setBins(binsRes.data);
    } catch (err) {
      alert("❌ Failed to mark bin as collected");
      console.error(err);
    }
  };

  return (
    <>
      <Header />
      <div className="max-w-5xl mx-auto p-4 space-y-6">
        <h1 className="text-2xl font-bold mb-4">CrewPortal Dashboard</h1>

        {/* Force Fail Switch */}
        <div className="flex justify-end items-center mb-4 gap-2">
          <span className="text-sm text-default-500">Force Scan Fail:</span>
          <Switch isSelected={forceFail} onValueChange={setForceFail} color="danger" />
        </div>

        {/* Crew Info */}
        <Card shadow="sm" className="mb-6">
          <CardHeader>
            <h2 className="text-lg font-semibold">Crew Information</h2>
          </CardHeader>
          <Divider />
          <CardBody className="space-y-2">
            {user ? (
              <>
                <div>
                  Username: <Chip size="sm" color="primary">{user.username}</Chip>
                </div>
                <div>
                  Truck: <Chip size="sm">{user.assignedTruck?.registrationNumber || "N/A"}</Chip>
                </div>
                <div>
                  Route: <Chip size="sm">{user.assignedTruck?.assignedRoute?.routeName || "N/A"}</Chip>
                </div>
              </>
            ) : (
              <p>Loading user info...</p>
            )}
          </CardBody>
        </Card>

        {/* Nearby Bins Table */}
        <Card shadow="sm">
          <CardHeader>
            <h2 className="text-lg font-semibold">Nearby Bins</h2>
          </CardHeader>
          <Divider />
          <CardBody>
            {loading ? (
              <p>Loading bins...</p>
            ) : bins.length === 0 ? (
              <p>No nearby bins available.</p>
            ) : (
              <Table aria-label="Nearby Bins" removeWrapper>
                <TableHeader>
                  <TableColumn>Bin Code</TableColumn>
                  <TableColumn>Type</TableColumn>
                  <TableColumn>Status</TableColumn>
                  <TableColumn>Action</TableColumn>
                </TableHeader>
                <TableBody>
                  {bins.map((bin) => (
                    <TableRow key={bin.id}>
                      <TableCell>{bin.binCode}</TableCell>
                      <TableCell>{bin.type}</TableCell>
                      <TableCell>
                        <Chip size="sm" color={bin.status === "Assigned" ? "success" : "warning"}>
                          {bin.status}
                        </Chip>
                      </TableCell>
                      <TableCell>
                        <Button size="sm" color="primary" onPress={() => handleScanClick(bin)}>
                          Scan Tag
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            )}
          </CardBody>
        </Card>

        {/* Scan Modal (spinner + tag info) */}
        <Modal isOpen={scanModal.isOpen} onOpenChange={scanModal.onOpenChange}>
          <ModalContent>
            {(onClose) => (
              <>
                <ModalHeader>Bin Waste Info</ModalHeader>
                <ModalBody className="space-y-4">
                  {scanning ? (
                    <div className="flex flex-col items-center gap-2">
                      <Spinner size="lg" label="Scanning..." />
                      <p>Hold the device near the bin tag...</p>
                    </div>
                  ) : tagInfo ? (
                    <div className="space-y-2">
                      <p>Tag ID: <strong>{tagInfo.tagId}</strong></p>
                      <p>Weight: <strong>{tagInfo.weight} kg</strong></p>
                      <p>Fill Level: <strong>{tagInfo.fillLevel} %</strong></p>
                    </div>
                  ) : (
                    <p>Waiting for scan...</p>
                  )}
                </ModalBody>
                <ModalFooter>
                  <Button variant="light" onPress={onClose}>Cancel</Button>
                  <Button
                    color="success"
                    onPress={handleMarkCollected}
                    disabled={!tagInfo}
                  >
                    Mark Collected
                  </Button>
                </ModalFooter>
              </>
            )}
          </ModalContent>
        </Modal>

        {/* Scan Failed Modal */}
        <Modal isOpen={scanFailModal.isOpen} onOpenChange={scanFailModal.onOpenChange}>
          <ModalContent>
            {(onClose) => (
              <>
                <ModalHeader className="text-danger">Scan Failed</ModalHeader>
                <ModalBody className="space-y-2">
                  <p>Unable to scan the bin. Enter Tag ID manually:</p>
                  <Input
                    placeholder="Enter Tag ID"
                    value={manualTagId}
                    onChange={(e) => setManualTagId(e.target.value)}
                  />
                </ModalBody>
                <ModalFooter>
                  <Button variant="light" onPress={onClose}>Cancel</Button>
                  <Button color="primary" onPress={handleManualScan} disabled={!manualTagId}>
                    Scan Tag
                  </Button>
                </ModalFooter>
              </>
            )}
          </ModalContent>
        </Modal>
      </div>
    </>
  );
};

export default CrewPortal;
