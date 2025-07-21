import { Routes, Route } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";
import SignInPage from "./pages/SignInPage";
import UserDetails from "./pages/Home";
import PaymentTest from "./components/PaymentTest";


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
      </Routes>
    
  );
}
