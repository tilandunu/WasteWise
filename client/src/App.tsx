import { Route, Routes } from "react-router-dom";
import RecidenRegister from "./pages/recident-register-page";
import RecidentLogin from "./pages/recident-login-page";
import Home from "./pages/home-page";
import RecidentBinAssign from "./pages/recident-bin-assign-page";
import CrewPortal from "./pages/crew-portal";
import RouteManagement from "./pages/route-management-page";
import RoutesPage from "./pages/routes-page";
import AssignRouteToTruck from "./pages/assign-route-to-truck-page";
import AssignTruckToCrew from "./pages/assign-truck-to-crew-page";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/register" element={<RecidenRegister />} />
      <Route path="/assign-bin" element={<RecidentBinAssign />} />
      <Route path="/login" element={<RecidentLogin />} />
      <Route path="/crew-portal" element={<CrewPortal />} />
      <Route path="/route-management" element={<RouteManagement />} />
      <Route path="/routes" element={<RoutesPage />} />
      <Route path="/assign-route-to-truck" element={<AssignRouteToTruck />} />
      <Route path="/assign-truck-to-crew" element={<AssignTruckToCrew />} />
    </Routes>
  );
}

export default App;
