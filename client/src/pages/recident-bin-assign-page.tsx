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
import { Input, useDisclosure } from "@heroui/react";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
} from "@heroui/modal";
import { Switch } from "@heroui/switch";
import Header from "../components/header";
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

  // Validation states per user
  const [validationStates, setValidationStates] = useState<
    Record<string, { validated: boolean; validating: boolean; failed: boolean }>
  >({});

  const [assigningUserId, setAssigningUserId] = useState<string | null>(null);
  const [selectedBinId, setSelectedBinId] = useState("");
  const [selectedTagId, setSelectedTagId] = useState("");
  const [forceFail, setForceFail] = useState(false);

  const [binSearch, setBinSearch] = useState("");
  const [tagSearch, setTagSearch] = useState("");

  const { isOpen, onOpen, onOpenChange } = useDisclosure(); // assign modal
  const {
    isOpen: msgOpen,
    onOpen: openMsgModal,
    onOpenChange: onMsgModalChange,
  } = useDisclosure(); // validation modal
  const {
    isOpen: successOpen,
    onOpen: openSuccessModal,
    onOpenChange: onSuccessModalChange,
  } = useDisclosure(); // assign success modal

  const [validationMessage, setValidationMessage] = useState("");
  const [validationSuccess, setValidationSuccess] = useState(false);

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

  const handleValidate = (userId: string) => {
    setValidationStates((prev) => ({
      ...prev,
      [userId]: { validated: false, validating: true, failed: false },
    }));

    setTimeout(() => {
      if (forceFail) {
        // Validation failed
        setValidationStates((prev) => ({
          ...prev,
          [userId]: { validated: false, validating: false, failed: true },
        }));
        setValidationSuccess(false);
        setValidationMessage("❌ Zone or address validation failed.");
      } else {
        // Validation passed
        setValidationStates((prev) => ({
          ...prev,
          [userId]: { validated: true, validating: false, failed: false },
        }));
        setValidationSuccess(true);
        setValidationMessage(
          "✅ Validation successful! You can assign a bin now."
        );
      }
      openMsgModal();
    }, 1000);
  };

  const handleOverride = (userId: string) => {
    setValidationStates((prev) => ({
      ...prev,
      [userId]: { validated: true, validating: false, failed: false },
    }));
    setValidationSuccess(true);
    setValidationMessage(
      "⚠️ Validation overridden manually. Proceed with caution."
    );
    openMsgModal();
  };

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

      setAssigningUserId(null);
      setSelectedBinId("");
      setSelectedTagId("");
      onOpenChange();

      // ✅ Show success modal
      openSuccessModal();
    } catch (err) {
      console.error(err);
      setValidationSuccess(false);
      setValidationMessage("❌ Failed to assign bin. Please try again.");
      openMsgModal();
    }
  };

  // Filter bins & tags based on search
  const filteredBins = bins.filter((b) =>
    b.binCode.toLowerCase().includes(binSearch.toLowerCase())
  );
  const filteredTags = tags.filter((t) =>
    t.tagId.toLowerCase().includes(tagSearch.toLowerCase())
  );

  if (loading)
    return (
      <div className="flex justify-center mt-20">
        <Spinner size="lg" label="Loading users..." />
      </div>
    );

  if (error)
    return <p className="text-center mt-8 text-danger font-medium">{error}</p>;

  return (
    <div className="max-w-6xl mx-auto p-6">
      <Header />

      <Card shadow="sm">
        <CardHeader className="flex flex-col items-center text-center">
          <h2 className="text-2xl font-semibold">Residents Without Bin</h2>
          <p className="text-default-500 text-sm">
            Validate resident address and zone before assigning bins
          </p>
        </CardHeader>
        <Divider />
        <CardBody>
          {/* Mock Validation Toggle */}
          <div className="flex justify-end mb-4 items-center gap-2">
            <span className="text-sm text-default-500">
              Force validation fail:
            </span>
            <Switch
              isSelected={forceFail}
              onValueChange={setForceFail}
              color="danger"
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
                    validating: false,
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
                        >
                          {user.activated ? "Active" : "Inactive"}
                        </Chip>
                      </TableCell>
                      <TableCell>
                        {user.activated ? (
                          <span className="text-default-500 font-medium">
                            Already assigned
                          </span>
                        ) : !state.validated ? (
                          <Button
                            size="sm"
                            color="secondary"
                            variant="flat"
                            isLoading={state.validating}
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
                              onOpen();
                            }}
                          >
                            Assign Bin
                          </Button>
                        )}

                        {state.failed &&
                          !state.validating &&
                          !user.activated && (
                            <Button
                              size="sm"
                              color="danger"
                              variant="bordered"
                              className="mt-2"
                              onPress={() => handleOverride(user.id)}
                            >
                              Force Override
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


      {/* Assign Modal */}
      <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="text-lg font-semibold">
                Assign Bin & Tag
              </ModalHeader>
              <ModalBody className="space-y-6">
                {/* Bin Search + List */}
                <div>
                  <Input
                    label="Search Bins"
                    placeholder="Search by type..."
                    value={binSearch}
                    onChange={(e) => setBinSearch(e.target.value)}
                  />
                  <div className="max-h-40 overflow-y-auto rounded mt-1">
                    {filteredBins.filter((b) => !b.assignedUser).length ===
                    0 ? (
                      <p className="text-center text-sm text-default-500 p-2">
                        No bins found
                      </p>
                    ) : (
                      filteredBins
                        .filter((b) => !b.assignedUser) // exclude assigned bins
                        .map((b) => (
                          <div
                            key={b.id}
                            className={`p-2 cursor-pointer hover:bg-default-100 ${
                              selectedBinId === b.id
                                ? "bg-primary-100 font-semibold"
                                : ""
                            }`}
                            onClick={() => setSelectedBinId(b.id)}
                          >
                            {b.binCode} - {b.type} - {b.status}
                          </div>
                        ))
                    )}
                  </div>
                </div>

                {/* Tag Search + List */}
                <div>
                  <Input
                    label="Search Tags"
                    placeholder="Search by tag ID..."
                    variant="underlined"
                    color="primary"
                    className="mb-2"
                    value={tagSearch}
                    onChange={(e) => setTagSearch(e.target.value)}
                  />
                  <div className="max-h-40 overflow-y-auto  rounded mt-1">
                    {filteredTags.length === 0 ? (
                      <p className="text-center text-sm text-default-500 p-2">
                        No tags found
                      </p>
                    ) : (
                      filteredTags.map((t) => (
                        <div
                          key={t.id}
                          className={`p-2 cursor-pointer hover:bg-default-100 ${
                            selectedTagId === t.id
                              ? "bg-primary-100 font-semibold"
                              : ""
                          }`}
                          onClick={() => setSelectedTagId(t.id)}
                        >
                          {t.tagId}
                        </div>
                      ))
                    )}
                  </div>
                </div>
              </ModalBody>

              <ModalFooter>
                <Button variant="light" onPress={onClose}>
                  Cancel
                </Button>
                <Button
                  color="success"
                  onPress={handleAssign}
                  disabled={!selectedBinId || !selectedTagId} // require selection
                >
                  Confirm Assign
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>

      {/* Validation Result Modal */}
      <Modal isOpen={msgOpen} onOpenChange={onMsgModalChange}>
        <ModalContent>
          <ModalHeader
            className={`font-semibold ${
              validationSuccess ? "text-success" : "text-danger"
            }`}
          >
            {validationSuccess ? "Validation Success" : "Validation Failed"}
          </ModalHeader>
          <ModalBody>
            <p>{validationMessage}</p>
          </ModalBody>
          <ModalFooter>
            <Button color="primary" onPress={onMsgModalChange}>
              OK
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

      {/* ✅ Assign Success Modal */}
      <Modal isOpen={successOpen} onOpenChange={onSuccessModalChange}>
        <ModalContent>
          <ModalHeader className="font-semibold text-success">
            ✅ Assignment Successful
          </ModalHeader>
          <ModalBody>
            <p>
              The bin and tag have been successfully assigned to the resident.
            </p>
          </ModalBody>
          <ModalFooter>
            <Button color="primary" onPress={onSuccessModalChange}>
              OK
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </div>
  );
};

export default AllUsersPage;
