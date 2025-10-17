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

interface BackendUser {
  id: string;
  username: string;
  address?: string;
  contactNumber?: string;
}

const Header: React.FC = () => {
  const [backendUser, setBackendUser] = useState<BackendUser | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUser = localStorage.getItem("loggedUser");
    if (storedUser) {
      setBackendUser(JSON.parse(storedUser));
    }
  }, []);

  const handleSignOut = () => {
    localStorage.removeItem("backendUser");
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
          <Logo className="h-5 w-5 text-primary" />
          <p className="font-bold text-inherit text-lg">My App</p>
        </Link>
      </NavbarBrand>

      {/* Right Side */}
      <NavbarContent justify="end" className="flex items-center gap-4">
        {backendUser ? (
          <>
            {/* User Info */}
            <NavbarItem className="flex flex-col text-right leading-tight">
              <span className="text-sm font-medium">
                {backendUser.username}
              </span>
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
