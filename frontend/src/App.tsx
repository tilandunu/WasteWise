import { Routes, Route } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";
import SignInPage from "./pages/SignInPage";
import UserDetails from "./pages/Home";
import PaymentTest from "./components/PaymentTest";
import RegisterPremisesPage from "./pages/RegisterPremisesPage";
import AddZonePage from "./pages/AddZone";
import AddBinPage from "./pages/AddBin";
import AddTag from "./pages/AddTag";
import AssignTruck from "./pages/AssignTruck";
import CreateTruck from "./pages/createTruck";
import AssignRoute from "./pages/assignRoute";
import CreateRoute from "./pages/CreateRoute";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<SignInPage />} />
      <Route
        path="/home"
        element={
          <ProtectedRoute>
            <UserDetails />
          </ProtectedRoute>
        }
      />
      <Route path="/paymentTest" element={<PaymentTest />} />
      <Route path="/register-premises" element={<RegisterPremisesPage />} />
      <Route path="/add-zone" element={<AddZonePage />} />
      <Route path="/add-bin" element={<AddBinPage />} />
      <Route path="/add-tag" element={<AddTag />} />
      <Route path="/assign-truck" element={<AssignTruck />} />
      <Route path="/create-truck" element={<CreateTruck />} />
      <Route path="/assign-route" element={<AssignRoute />} />
      <Route path="/create-route" element={<CreateRoute />} />
    </Routes>
  );
}
