import { Routes, Route } from "react-router-dom";
import Login from "./pages/login";
import Signup from "./pages/register";
import Home from "./pages/Home.tsx";
// import AddZonePage from "./pages/AddZone";
// import AddBinPage from "./pages/AddBin";
// import AddTag from "./pages/AddTag";

export default function App() {
  return (
    <Routes>
      <Route path="/home" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Signup />} />
      {/* <Route path="/add-zone" element={<AddZonePage />} />
      <Route path="/add-bin" element={<AddBinPage />} />
      <Route path="/add-tag" element={<AddTag />} /> */}
    </Routes>
  );
}
