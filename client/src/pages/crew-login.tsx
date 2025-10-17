import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../config/axiosInstance";
import { Card, CardHeader, CardBody } from "@heroui/card";
import { Input } from "@heroui/react";
import { Button } from "@heroui/button";
import { Divider } from "@heroui/divider";
import  Header from "../components/header";

const CrewSignInPage: React.FC = () => {
  const [formData, setFormData] = useState({ username: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setErrorMsg("");

    try {
      const res = await axios.post("/api/users/login", formData);
      const user = res.data;

      localStorage.setItem("loggedUser", JSON.stringify(user));

      if (user.assignedTruck != null) {
        navigate("/crew-portal");
      } else {
        navigate("/crew-portal");
      }
    } catch (err: any) {
      console.error("Login error:", err);
      setErrorMsg(err.response?.data || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Header />
      <div className="min-h-screen flex items-center justify-center bg-background px-4">
        <Card shadow="lg" className="w-full max-w-md p-6">
          <CardHeader className="text-center">
            <h2 className="text-2xl font-bold">Sign In to WasteWise as crewMember</h2>
          </CardHeader>

          <Divider className="my-4" />

          {errorMsg && (
            <p className="text-red-600 mb-4 text-center">{errorMsg}</p>
          )}

          <CardBody>
            <form onSubmit={handleSubmit} className="space-y-4">
              <Input
                label="Username"
                placeholder="Enter your username"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
              />

              <Input
                label="Password"
                type="password"
                placeholder="Enter your password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
              />

              <Button
                type="submit"
                color="primary"
                size="lg"
                className="w-full"
                isLoading={loading}
              >
                {loading ? "Signing in..." : "Sign In as crewMember"}
              </Button>
            </form>
          </CardBody>
        </Card>
      </div>
    </>
  );
};

export default CrewSignInPage;
