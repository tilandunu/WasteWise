import React, { useEffect, useState } from "react";
import axios from "../config/axiosInstance";
import { Card, CardHeader, CardBody } from "@heroui/card";
import {
  Table,
  TableHeader,
  TableColumn,
  TableBody,
  TableRow,
  TableCell,
} from "@heroui/table";
import { Button } from "@heroui/button";
import { Spinner } from "@heroui/spinner";
import { Divider } from "@heroui/divider";
import { Chip } from "@heroui/chip";
import { Input } from "@heroui/react";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
} from "@heroui/modal";
import { Switch } from "@heroui/switch";
import Header from "../components/header";
import { Image } from "@heroui/react";
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
  binCode: string;
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

  const [validationStates, setValidationStates] = useState<
    Record<string, { validated: boolean; failed: boolean }>
  >({});

  const [assigningUserId, setAssigningUserId] = useState<string | null>(null);
  const [selectedBinId, setSelectedBinId] = useState("");
  const [selectedTagId, setSelectedTagId] = useState("");
  const [forceFail, setForceFail] = useState(false);

  const [binSearch, setBinSearch] = useState("");
  const [tagSearch, setTagSearch] = useState("");

  // 🧩 Explicit modal states (reliable)
  const [validateLoading, setValidateLoading] = useState(false);
  const [overrideLoading, setOverrideLoading] = useState(false);
  const [resultOpen, setResultOpen] = useState(false);
  const [assignOpen, setAssignOpen] = useState(false);

  const [resultMessage, setResultMessage] = useState("");
  const [resultSuccess, setResultSuccess] = useState(false);
  const [resultImage, setResultImage] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await axios.get<Resident[]>("/api/users/all");
        setUsers(res.data.filter((user) => !user.bin));

        const binsRes = await axios.get<Bin[]>("/api/bins/unassigned");
        setBins(binsRes.data);

        const tagsRes = await axios.get<Tag[]>("/api/tags/all");
        setTags(tagsRes.data.filter((t) => t.active));
      } catch (err) {
        console.error(err);
        setError("Failed to fetch data");
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  // ✅ Always close all modals before opening a new one
  const closeAllModals = () => {
    setValidateLoading(false);
    setOverrideLoading(false);
    setResultOpen(false);
    setAssignOpen(false);
  };

  // ✅ Validation flow
  const handleValidate = (userId: string) => {
    closeAllModals();
    setValidateLoading(true); // show spinner

    setTimeout(() => {
      setValidateLoading(false); // stop spinner first

      if (forceFail) {
        setValidationStates((prev) => ({
          ...prev,
          [userId]: { validated: false, failed: true },
        }));
        setResultSuccess(false);
        setResultMessage(
          "❌ Zone validation failed. Try again or override manually."
        );
        setResultImage(
          "https://via.placeholder.com/300x180.png?text=Validation+Failed"
        );
      } else {
        setValidationStates((prev) => ({
          ...prev,
          [userId]: { validated: true, failed: false },
        }));
        setResultSuccess(true);
        setResultMessage("✅ Zone validated successfully!");
        setResultImage(
          "https://via.placeholder.com/300x180.png?text=Validation+Success"
        );
      }

      setTimeout(() => setResultOpen(true), 200);
    }, 3000);
  };

  // ✅ Override flow
  const handleOverride = (userId: string) => {
    closeAllModals();
    setOverrideLoading(true);

    setTimeout(() => {
      setOverrideLoading(false); // stop spinner
      setValidationStates((prev) => ({
        ...prev,
        [userId]: { validated: true, failed: false },
      }));
      setResultSuccess(true);
      setResultMessage(
        "⚠️ Validation manually overridden. Proceed with caution."
      );
      setResultImage(
        "https://via.placeholder.com/300x180.png?text=Manual+Override"
      );

      setTimeout(() => setResultOpen(true), 200);
    }, 3000);
  };

  // ✅ Assign logic unchanged
  const handleAssign = async () => {
    if (!assigningUserId || !selectedBinId || !selectedTagId) return;
    try {
      await axios.post("/api/officer/assign-tag", null, {
        params: { binId: selectedBinId, tagId: selectedTagId },
      });
      await axios.post("/api/officer/assign-bin", null, {
        params: { binId: selectedBinId, residentId: assigningUserId },
      });

      const res = await axios.get<Resident[]>("/api/users/all");
      setUsers(res.data.filter((user) => !user.bin));

      closeAllModals();
      setResultSuccess(true);
      setResultMessage("✅ Bin and tag successfully assigned.");
      setResultImage(
        "https://via.placeholder.com/300x180.png?text=Assign+Success"
      );
      setResultOpen(true);
    } catch (err) {
      console.error(err);
      closeAllModals();
      setResultSuccess(false);
      setResultMessage("❌ Failed to assign bin. Please try again.");
      setResultImage(
        "https://via.placeholder.com/300x180.png?text=Assign+Error"
      );
      setResultOpen(true);
    }
  };

  const filteredBins = bins.filter((b) =>
    b.binCode.toLowerCase().includes(binSearch.toLowerCase())
  );
  const filteredTags = tags.filter((t) =>
    t.tagId.toLowerCase().includes(tagSearch.toLowerCase())
  );

  if (loading)
    return (
      <>
        <Header />
        <div className="flex justify-center mt-20">
          <Spinner size="lg" label="Loading users..." />
        </div>
      </>
    );

  if (error)
    return <p className="text-center mt-8 text-danger font-medium">{error}</p>;

  return (
    <div className="max-w-6xl mx-auto p-6">
      <Header />

      <Card shadow="sm">
        <CardHeader className="flex flex-col items-center text-center">
          <h2 className="text-2xl font-semibold">Smart Bin Assignment</h2>
          <p className="text-default-500 text-sm">
            Validate resident address and zone before assigning bins
          </p>
        </CardHeader>
        <Divider />
        <CardBody>
          <div className="flex justify-end mb-4 items-center gap-2">
            <span className="text-sm text-default-500">
              Force validation fail:
            </span>
            <Switch
              isSelected={forceFail}
              onValueChange={setForceFail}
              color="danger"
              size="sm"
            />
          </div>

          {users.length === 0 ? (
            <p className="text-center text-default-600">
              ✅ All residents have been assigned bins.
            </p>
          ) : (
            <Table
              aria-label="Residents without bin"
              className="mt-4"
              removeWrapper
            >
              <TableHeader>
                <TableColumn>Username</TableColumn>
                <TableColumn>Address</TableColumn>
                <TableColumn>Contact</TableColumn>
                <TableColumn>Zone</TableColumn>
                <TableColumn>Premises</TableColumn>
                <TableColumn>Status</TableColumn>
                <TableColumn>Actions</TableColumn>
              </TableHeader>
              <TableBody>
                {users.map((user) => {
                  const state = validationStates[user.id] || {
                    validated: false,
                    failed: false,
                  };
                  return (
                    <TableRow key={user.id}>
                      <TableCell>{user.username}</TableCell>
                      <TableCell>{user.address}</TableCell>
                      <TableCell>{user.contactNumber}</TableCell>
                      <TableCell>{user.zone?.name || "N/A"}</TableCell>
                      <TableCell>{user.premisesType}</TableCell>
                      <TableCell>
                        <Chip
                          color={user.activated ? "success" : "warning"}
                          size="sm"
                          variant="dot"
                        >
                          {user.activated ? "Active" : "Inactive"}
                        </Chip>
                      </TableCell>
                      <TableCell>
                        {user.activated ? (
                          <span className="text-default-500 font-medium">
                            Assigned
                          </span>
                        ) : !state.validated ? (
                          <Button
                            size="sm"
                            color="primary"
                            variant="flat"
                            onPress={() => {
                              setAssigningUserId(user.id);
                              handleValidate(user.id);
                            }}
                          >
                            Validate
                          </Button>
                        ) : (
                          <Button
                            size="sm"
                            color="primary"
                            variant="flat"
                            onPress={() => {
                              setAssigningUserId(user.id);
                              setAssignOpen(true);
                            }}
                          >
                            Assign Bin
                          </Button>
                        )}

                        {state.failed && !user.activated && (
                          <Button
                            size="sm"
                            color="danger"
                            className="mt-2 ml-2"
                            onPress={() => handleOverride(user.id)}
                          >
                            Override
                          </Button>
                        )}
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          )}
        </CardBody>
      </Card>

      {/* 🌀 Validation Spinner Modal */}
      <Modal isOpen={validateLoading} onOpenChange={setValidateLoading}>
        <ModalContent>
          <ModalHeader className="font-semibold text-primary">
            Validating Zone...
          </ModalHeader>
          <ModalBody className="flex flex-col items-center justify-center">
            <Image
              src="../../assets/images/vali_map.gif"
              alt="Validating"
              width={300}
            />
            <Spinner size="lg" color="primary" variant="wave"/>
          </ModalBody>
        </ModalContent>
      </Modal>

      {/* 🌀 Override Spinner Modal */}
      <Modal isOpen={overrideLoading} onOpenChange={setOverrideLoading}>
        <ModalContent>
          <ModalHeader className="font-semibold text-warning">
            Applying Manual Override...
          </ModalHeader>
          <ModalBody className="flex flex-col items-center justify-center space-y-4">
            <Spinner size="lg" color="warning" />
            <Image
              src="../../assets/images/override.gif"
              alt="Validating"
              width={300}
            />
            <p className="text-center text-default-500">
              Please wait while manual override is being applied.
            </p>
          </ModalBody>
        </ModalContent>
      </Modal>

      {/* ✅ Result Modal */}
      <Modal isOpen={resultOpen} onOpenChange={setResultOpen}>
        <ModalContent>
          <ModalHeader
            className={`font-semibold ${
              resultSuccess ? "text-success" : "text-danger"
            }`}
          >
            {resultSuccess ? "Success" : "Error"}
          </ModalHeader>
          <ModalBody className="flex flex-col items-center space-y-4">
            <p className="text-center">{resultMessage}</p>
          </ModalBody>
          <ModalFooter>
            <Button
              color="primary"
              onPress={() => setResultOpen(false)}
              size="sm"
            >
              OK
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

      {/* Assign Modal */}
      <Modal isOpen={assignOpen} onOpenChange={setAssignOpen}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="text-lg font-semibold">
                Assign Bin & Tag
              </ModalHeader>
              <ModalBody className="space-y-6">
                <Input
                  label="Search Bins"
                  variant="bordered"
                  value={binSearch}
                  onChange={(e) => setBinSearch(e.target.value)}
                />
                <div className="max-h-40 overflow-y-auto">
                  {filteredBins.map((b) => (
                    <div
                      key={b.id}
                      className={`p-2 cursor-pointer hover:bg-default-100 ${
                        selectedBinId === b.id ? "bg-primary-100" : ""
                      }`}
                      onClick={() => setSelectedBinId(b.id)}
                    >
                      {b.binCode} - {b.type}
                    </div>
                  ))}
                </div>

                <Input
                  label="Search Tags"
                  variant="bordered"
                  value={tagSearch}
                  onChange={(e) => setTagSearch(e.target.value)}
                />
                <div className="max-h-40 overflow-y-auto">
                  {filteredTags.map((t) => (
                    <div
                      key={t.id}
                      className={`p-2 cursor-pointer hover:bg-default-100 ${
                        selectedTagId === t.id ? "bg-primary-100" : ""
                      }`}
                      onClick={() => setSelectedTagId(t.id)}
                    >
                      {t.tagId}
                    </div>
                  ))}
                </div>
              </ModalBody>
              <ModalFooter>
                <Button
                  variant="light"
                  onPress={onClose}
                  color="danger"
                  size="sm"
                >
                  Cancel
                </Button>
                <Button
                  color="primary"
                  onPress={handleAssign}
                  disabled={!selectedBinId || !selectedTagId}
                  size="sm"
                >
                  Confirm
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </div>
  );
};

export default AllUsersPage;
