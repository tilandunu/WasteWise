import { Route, Routes } from "react-router-dom";
import RecidenRegister from "./pages/recident-register-page";
import RecidentLogin from "./pages/recident-login-page";
import Home from "./pages/home-page";
import RecidentBinAssign  from "./pages/recident-bin-assign-page";



function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/register" element={<RecidenRegister />} />
      <Route path="/assign-bin" element={<RecidentBinAssign />} />
      <Route path="/login" element={<RecidentLogin />} />
    </Routes>
  );
}

export default App;
