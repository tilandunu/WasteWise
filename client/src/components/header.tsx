import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Navbar as HeroUINavbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
} from "@heroui/navbar";
import { Button } from "@heroui/button";
import { Link } from "@heroui/link";
import { ThemeSwitch } from "@/components/theme-switch"; // optional
import { Logo } from "@/components/icons"; // optional
import { User } from "@heroui/react";

interface BackendUser {
  id: string;
  username: string;
  address?: string;
  contactNumber?: string;
  assignedTruck?: {
    id: string;
    registrationNumber: string;
    model: string;
    assignedRoute?: { id: string; routeName: string; active: boolean };
    status: string;
    available: boolean;
  };
}

const Header: React.FC = () => {
  const [backendUser, setBackendUser] = useState<BackendUser | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUser = localStorage.getItem("loggedUser");
    if (storedUser) setBackendUser(JSON.parse(storedUser));
  }, []);

  const handleSignOut = () => {
    localStorage.removeItem("loggedUser");
    setBackendUser(null);
    navigate("/login");
  };

  return (
    <HeroUINavbar
      isBordered
      maxWidth="xl"
      position="sticky"
      className="bg-background"
    >
      {/* Brand Section */}
      <NavbarBrand className="flex items-center gap-2">
        <Link href="/" color="foreground" className="flex items-center gap-1">
          <Logo className="h-15 w-15 text-primary" />
          <p className="font-bold text-inherit text-[1.7rem]">WasteWise</p>
        </Link>
      </NavbarBrand>

      {/* Right Side */}
      <NavbarContent justify="end" className="flex items-center gap-4">
        {backendUser ? (
          <>
            {/* Route Management Button */}
            <NavbarItem>
              <Button
                as={Link}
                href="/route-management"
                color="primary"
                variant="flat"
                size="sm"
              >
                Route Management
              </Button>
            </NavbarItem>

            {/* User Info */}
            <NavbarItem className="flex flex-col text-right leading-tight">
              <User
                avatarProps={{
                  src: "https://i.pravatar.cc/150?u=a04258114e29026702d",
                }}
                description={backendUser.contactNumber || "N/A"}
                name={backendUser.username}
              />
            </NavbarItem>

            {/* Sign Out */}
            <NavbarItem>
              <Button
                color="danger"
                variant="flat"
                size="sm"
                onPress={handleSignOut}
              >
                Sign Out
              </Button>
            </NavbarItem>
          </>
        ) : (
          <>
            <NavbarItem>
              <span className="text-sm text-default-500">Not logged in</span>
            </NavbarItem>
            <NavbarItem>
              <Button
                as={Link}
                href="/login"
                color="primary"
                variant="flat"
                size="sm"
              >
                Login
              </Button>
            </NavbarItem>
            <NavbarItem>
              <Button
                as={Link}
                href="/register"
                color="success"
                variant="flat"
                size="sm"
              >
                Register
              </Button>
            </NavbarItem>
          </>
        )}

        {/* Optional theme switcher */}
        <NavbarItem>
          <ThemeSwitch />
        </NavbarItem>
      </NavbarContent>
    </HeroUINavbar>
  );
};

export default Header;
