import React from "react";
import { Button } from "@heroui/button";
import { Card, CardHeader, CardBody, CardFooter } from "@heroui/card";
import { Link } from "@heroui/link";
import { Divider } from "@heroui/divider";
import Header from "../components/header";

const Home: React.FC = () => {
  return (
    <>
      {/* HeroUI Header */}
      <Header />

      {/* Page Content */}
      <main className="max-w-5xl mx-auto px-4 py-10 text-center">
        <section className="flex flex-col items-center justify-center gap-6">
          <h1 className="text-4xl font-bold tracking-tight">
            Welcome to <span className="text-primary">My App</span>
          </h1>
          <p className="text-default-500 max-w-md">
            Manage your premises, users, and account settings all in one place.
            Built with HeroUI and Vite for a fast, modern experience.
          </p>

          <div className="flex gap-4 mt-4">
            <Button
              as={Link}
              href="/register-premises"
              color="primary"
              variant="solid"
              size="md"
            >
              Register Premises
            </Button>
            <Button
              as={Link}
              href="/assign-bin"
              color="secondary"
              variant="flat"
              size="md"
            >
              Go to Dashboard
            </Button>
          </div>
        </section>

        <Divider className="my-10" />

        <section className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {/* Example Feature Cards */}
          <Card shadow="sm" isPressable>
            <CardHeader className="font-semibold text-lg">
              Quick Registration
            </CardHeader>
            <CardBody>
              <p className="text-default-500 text-sm">
                Easily register new premises and manage existing ones from a
                single dashboard.
              </p>
            </CardBody>
            <CardFooter>
              <Link href="/assign-bin" color="primary">
                Learn more →
              </Link>
            </CardFooter>
          </Card>

          <Card shadow="sm" isPressable>
            <CardHeader className="font-semibold text-lg">
              User Management
            </CardHeader>
            <CardBody>
              <p className="text-default-500 text-sm">
                Keep track of your users, update details, and manage permissions
                seamlessly.
              </p>
            </CardBody>
            <CardFooter>
              <Link href="/users" color="primary">
                View users →
              </Link>
            </CardFooter>
          </Card>

          <Card shadow="sm" isPressable>
            <CardHeader className="font-semibold text-lg">Secure Access</CardHeader>
            <CardBody>
              <p className="text-default-500 text-sm">
                Built with modern authentication to keep your data safe and
                accessible only to authorized users.
              </p>
            </CardBody>
            <CardFooter>
              <Link href="/login" color="primary">
                Sign in →
              </Link>
            </CardFooter>
          </Card>
        </section>
      </main>
    </>
  );
};

export default Home;
