import { Routes, Route } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";
import SignInPage from "./pages/SignInPage";
import UserDetails from "./pages/Home";
import PaymentTest from "./components/PaymentTest";
import RegisterPremisesPage from "./pages/RegisterPremisesPage";
import AddZonePage from "./pages/AddZone";
import AddBinPage from "./pages/AddBin";
import AddTag from "./pages/AddTag";

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
    </Routes>
  );
}
