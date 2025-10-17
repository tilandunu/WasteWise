import React from 'react';
import { Button } from '@heroui/button';
import { Card, CardHeader, CardBody } from '@heroui/card';
import { Link } from '@heroui/link';
import Header from '../components/header';

const Home: React.FC = () => {
  return (
    <>
      {/* HeroUI Header */}
      <Header />

      {/* Hero Section */}
      <section className='bg-primary-50 py-20'>
        <div className='max-w-5xl mx-auto px-4 text-center'>
          <h1 className='text-5xl font-bold text-primary mb-4'>
            Welcome to <span className='text-primary-600'>WasteWise</span>
          </h1>
          <p className='text-default-600 text-lg max-w-2xl mx-auto mb-8'>
            Smart waste management for households, businesses, and
            municipalities. Track, manage, and optimize waste collection with
            real-time digital insights.
          </p>
          <div className='flex flex-col sm:flex-row justify-center gap-4'>
            <Button
              as={Link}
              href='/assign-bin'
              color='secondary'
              variant='flat'
              size='lg'
            >
              Go to Dashboard
            </Button>
            <Button
              as={Link}
              href='/crew-login'
              color='secondary'
              variant='flat'
              size='lg'
            >
              Crew Portal
            </Button>
            <Button
              as={Link}
              href="/route-management"
              color="success"
              variant="solid"
              size="md"
            >
              Route Management
            </Button>
            <Button
              as={Link}
              href='/billing'
              color='secondary'
              variant='flat'
              size='lg'
            >
              Go to Billing
            </Button>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className='max-w-6xl mx-auto px-4 py-20'>
        <h2 className='text-3xl font-bold text-center mb-12'>Why WasteWise?</h2>
        <div className='grid gap-8 sm:grid-cols-2 lg:grid-cols-3'>
          <Card shadow='sm' isPressable>
            <CardHeader className='font-semibold text-lg'>
              Smart Bin Tracking
            </CardHeader>
            <CardBody>
              <p className='text-default-500 text-sm'>
                Attach digital tags to bins and monitor fill levels, weight, and
                collection status in real-time.
              </p>
            </CardBody>
          </Card>

          <Card shadow='sm' isPressable>
            <CardHeader className='font-semibold text-lg'>
              Crew & Route Management
            </CardHeader>
            <CardBody>
              <p className='text-default-500 text-sm'>
                Assign trucks and crews to optimized routes for efficient waste
                collection.
              </p>
            </CardBody>
          </Card>

          <Card shadow='sm' isPressable>
            <CardHeader className='font-semibold text-lg'>
              Analytics & Insights
            </CardHeader>
            <CardBody>
              <p className='text-default-500 text-sm'>
                Gain actionable insights on waste generation, collection
                frequency, and operational efficiency.
              </p>
            </CardBody>
          </Card>

          <Card shadow='sm' isPressable>
            <CardHeader className='font-semibold text-lg'>
              Eco-Friendly Impact
            </CardHeader>
            <CardBody>
              <p className='text-default-500 text-sm'>
                Reduce overflow, optimize collections, and promote sustainable
                waste management.
              </p>
            </CardBody>
          </Card>

          <Card shadow='sm' isPressable>
            <CardHeader className='font-semibold text-lg'>
              Secure & Reliable
            </CardHeader>
            <CardBody>
              <p className='text-default-500 text-sm'>
                Your data is safe with role-based access, secure authentication,
                and robust cloud storage.
              </p>
            </CardBody>
          </Card>

          <Card shadow='sm' isPressable>
            <CardHeader className='font-semibold text-lg'>
              Easy Setup
            </CardHeader>
            <CardBody>
              <p className='text-default-500 text-sm'>
                Quick onboarding for households, businesses, and municipal
                partners.
              </p>
            </CardBody>
          </Card>
        </div>
      </section>

      {/* Call to Action Section */}
      <section className='bg-primary-50 py-20'>
        <div className='max-w-3xl mx-auto text-center px-4'>
          <h2 className='text-3xl font-bold mb-4'>
            Ready to manage your waste smarter?
          </h2>
          <p className='text-default-600 mb-6'>
            Join WasteWise today and revolutionize the way you track and manage
            waste collection.
          </p>
          <Button
            as={Link}
            href='/register'
            color='primary'
            variant='solid'
            size='lg'
          >
            Get Started Now
          </Button>
        </div>
      </section>
    </>
  );
};

export default Home;
