import React from "react";
import { Button } from "@heroui/button";
import { Card, CardHeader, CardBody } from "@heroui/card";
import { Link } from "@heroui/link";
import { Divider } from "@heroui/react";
import Header from "../components/header";

const RouteManagement: React.FC = () => {
  return (
    <>
      {/* Header */}
      <Header />

      {/* Page Content */}
      <main className="max-w-5xl mx-auto px-4 py-10">
        <section className="text-center mb-8">
          <h1 className="text-4xl font-bold tracking-tight mb-4">
            Route <span className="text-primary">Management</span>
          </h1>
          <p className="text-default-500 max-w-2xl mx-auto">
            Manage routes, assign routes to trucks, and assign trucks to crew members all in one place.
          </p>
        </section>

        <Divider className="my-8" />

        {/* Management Options */}
        <section className="grid gap-6 sm:grid-cols-1 lg:grid-cols-3">
          {/* Routes Management */}
          <Card shadow="md" isPressable className="hover:shadow-lg transition-shadow">
            <CardHeader className="pb-3">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center">
                  <svg
                    className="w-6 h-6 text-primary"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 01.553-.894L9 2l6 3 6-3v15l-6 3-6-3z"
                    />
                  </svg>
                </div>
                <div>
                  <h3 className="text-xl font-semibold">Routes</h3>
                  <p className="text-sm text-default-500">Add, view, edit & delete routes</p>
                </div>
              </div>
            </CardHeader>
            <CardBody className="pt-0">
              <p className="text-default-600 mb-4">
                Create new routes, view all existing routes, and manage route details including updates and deletions.
              </p>
              <Button
                as={Link}
                href="/routes"
                color="primary"
                variant="solid"
                className="w-full"
              >
                Manage Routes
              </Button>
            </CardBody>
          </Card>

          {/* Assign Route to Truck */}
          <Card shadow="md" isPressable className="hover:shadow-lg transition-shadow">
            <CardHeader className="pb-3">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 bg-secondary/10 rounded-lg flex items-center justify-center">
                  <svg
                    className="w-6 h-6 text-secondary"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4"
                    />
                  </svg>
                </div>
                <div>
                  <h3 className="text-xl font-semibold">Route to Truck</h3>
                  <p className="text-sm text-default-500">Assign routes to trucks</p>
                </div>
              </div>
            </CardHeader>
            <CardBody className="pt-0">
              <p className="text-default-600 mb-4">
                Assign specific routes to trucks for efficient route management and delivery planning.
              </p>
              <Button
                as={Link}
                href="/assign-route-to-truck"
                color="secondary"
                variant="solid"
                className="w-full"
              >
                Assign Route to Truck
              </Button>
            </CardBody>
          </Card>

          {/* Assign Truck to Crew */}
          <Card shadow="md" isPressable className="hover:shadow-lg transition-shadow">
            <CardHeader className="pb-3">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 bg-success/10 rounded-lg flex items-center justify-center">
                  <svg
                    className="w-6 h-6 text-success"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"
                    />
                  </svg>
                </div>
                <div>
                  <h3 className="text-xl font-semibold">Truck to Crew</h3>
                  <p className="text-sm text-default-500">Assign trucks to crew members</p>
                </div>
              </div>
            </CardHeader>
            <CardBody className="pt-0">
              <p className="text-default-600 mb-4">
                Assign trucks to crew members for proper resource allocation and team management.
              </p>
              <Button
                as={Link}
                href="/assign-truck-to-crew"
                color="success"
                variant="solid"
                className="w-full"
              >
                Assign Truck to Crew
              </Button>
            </CardBody>
          </Card>
        </section>
      </main>
    </>
  );
};

export default RouteManagement;