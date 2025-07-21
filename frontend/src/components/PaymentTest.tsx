import { useState, useEffect } from 'react';
import { loadStripe } from '@stripe/stripe-js';
import {
  Elements,
  CardElement,
  useStripe,
  useElements
} from '@stripe/react-stripe-js';

// Replace with your publishable key
const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY || 'pk_test_51RHOqvHQoMQTExGNrroYBPZTeRuB3RhRktcwiS3D9rjNCNzoiiKKSEHDdZOcmiPgBCB6L7AOOQgeBYR3NrZ0IpDA00a6jsybQS');

interface PaymentHistory {
  id: string;
  amount: number;
  status: string;
  created: string;
  refunded: boolean;
  refundAmount?: number;
}

const CheckoutForm = () => {
  const stripe = useStripe();
  const elements = useElements();
  const [amount, setAmount] = useState(10.00);
  const [isLoading, setIsLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [clientSecret, setClientSecret] = useState('');
  const [paymentHistory, setPaymentHistory] = useState<PaymentHistory[]>([]);
  const [isRefunding, setIsRefunding] = useState<string | null>(null);
  const [refundAmount, setRefundAmount] = useState<{ [key: string]: number }>({});

  // Create payment intent when component mounts or amount changes
  useEffect(() => {
    if (amount >= 0.50) {
      createPaymentIntent();
    }
  }, [amount]);

  // Load payment history on component mount
  useEffect(() => {
    loadPaymentHistory();
  }, []);

  const createPaymentIntent = async () => {
    try {
      const response = await fetch('http://localhost:5000/api/create-payment-intent', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          amount: amount,
          metadata: {
            product: 'Test Product',
          },
        }),
      });

      const data = await response.json();
      
      if (data.clientSecret) {
        setClientSecret(data.clientSecret);
        setMessage('');
      } else {
        setMessage(`Error: ${data.error}`);
      }
    } catch (error) {
      if (error instanceof Error) {
        setMessage(`Error: ${error.message}`);
      } else {
        setMessage('An unknown error occurred.');
      }
    }
  };

  const loadPaymentHistory = async () => {
    try {
      const response = await fetch('http://localhost:5000/api/payment-intents?limit=10');
      const data = await response.json();
      
      // Updated to handle the new backend response format
      if (Array.isArray(data)) {
        const history: PaymentHistory[] = data.map((payment: any) => ({
          id: payment.id,
          amount: payment.amount, // Already converted to dollars in backend
          status: payment.status,
          created: new Date(payment.created).toLocaleString(),
          refunded: payment.refunded || false,
          refundAmount: payment.refundAmount || undefined
        }));
        setPaymentHistory(history);
      } else {
        console.error('Unexpected response format:', data);
        setPaymentHistory([]);
      }
    } catch (error) {
      console.error('Error loading payment history:', error);
      setPaymentHistory([]);
    }
  };

  const handleSubmit = async (event: { preventDefault: () => void; }) => {
    event.preventDefault();

    if (!stripe || !elements) {
      return;
    }

    setIsLoading(true);
    setMessage('');

    const cardElement = elements.getElement(CardElement);
    if (!cardElement) {
      setMessage('Card details are not complete.');
      setIsLoading(false);
      return;
    }

    const { error, paymentIntent } = await stripe.confirmCardPayment(clientSecret, {
      payment_method: {
        card: cardElement,
        billing_details: {
          name: 'Test Customer',
        },
      },
    });

    if (error) {
      setMessage(`Payment failed: ${error.message}`);
    } else {
      setMessage(`Payment succeeded! ID: ${paymentIntent.id}`);
      // Reload payment history to show the new payment
      setTimeout(() => {
        loadPaymentHistory();
      }, 1000);
    }

    setIsLoading(false);
  };

  const handleRefund = async (paymentIntentId: string, refundAmountValue?: number) => {
    setIsRefunding(paymentIntentId);
    try {
      const response = await fetch(`http://localhost:5000/api/payment-intent/${paymentIntentId}/refund`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          amount: refundAmountValue || undefined // Send amount or undefined for full refund
        }),
      });

      const data = await response.json();
      
      if (data.id) {
        setMessage(`Refund successful! Refund ID: ${data.id}`);
        // Reload payment history to show the refund
        setTimeout(() => {
          loadPaymentHistory();
        }, 1000);
      } else {
        setMessage(`Refund failed: ${data.error}`);
      }
    } catch (error) {
      if (error instanceof Error) {
        setMessage(`Refund error: ${error.message}`);
      } else {
        setMessage('An unknown refund error occurred.');
      }
    }
    setIsRefunding(null);
  };

  const updateRefundAmount = (paymentId: string, value: number) => {
    setRefundAmount(prev => ({
      ...prev,
      [paymentId]: value
    }));
  };

  const cardStyle = {
    style: {
      base: {
        fontSize: '16px',
        color: '#424770',
        '::placeholder': {
          color: '#aab7c4',
        },
      },
      invalid: {
        color: '#9e2146',
      },
    },
  };

  return (
    <div className="max-w-4xl mx-auto bg-white rounded-lg shadow-md p-6">
      <h2 className="text-2xl font-bold mb-6 text-center">Stripe Payment Test with Refunds</h2>
      
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Payment Form */}
        <div className="space-y-4">
          <h3 className="text-lg font-semibold">Create Payment</h3>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Amount ($)
            </label>
            <input
              type="number"
              min="0.50"
              step="0.01"
              value={amount}
              onChange={(e) => setAmount(parseFloat(e.target.value) || 0)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Card Details
            </label>
            <div className="p-3 border border-gray-300 rounded-md">
              <CardElement options={cardStyle} />
            </div>
          </div>

          <button
            onClick={handleSubmit}
            disabled={!stripe || isLoading || !clientSecret}
            className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
          >
            {isLoading ? 'Processing...' : `Pay $${amount.toFixed(2)}`}
          </button>

          {message && (
            <div className={`mt-4 p-3 rounded-md ${
              message.includes('succeeded') || message.includes('Refund successful') 
                ? 'bg-green-100 text-green-700' 
                : 'bg-red-100 text-red-700'
            }`}>
              {message}
            </div>
          )}

          <div className="mt-6 text-sm text-gray-600">
            <h3 className="font-semibold mb-2">Test Card Numbers:</h3>
            <ul className="space-y-1">
              <li><strong>Success:</strong> 4242 4242 4242 4242</li>
              <li><strong>Decline:</strong> 4000 0000 0000 0002</li>
              <li><strong>3D Secure:</strong> 4000 0025 0000 3155</li>
            </ul>
            <p className="mt-2">Use any future expiry date, any 3-digit CVC, and any ZIP code.</p>
          </div>
        </div>

        {/* Payment History & Refunds */}
        <div className="space-y-4">
          <div className="flex justify-between items-center">
            <h3 className="text-lg font-semibold">Payment History</h3>
            <button
              onClick={loadPaymentHistory}
              className="px-3 py-1 text-sm bg-gray-200 text-gray-700 rounded hover:bg-gray-300"
            >
              Refresh
            </button>
          </div>

          <div className="space-y-3 max-h-96 overflow-y-auto">
            {paymentHistory.length === 0 ? (
              <p className="text-gray-500 text-center py-4">No payments found</p>
            ) : (
              paymentHistory.map((payment) => (
                <div key={payment.id} className="border rounded-lg p-4 space-y-2">
                  <div className="flex justify-between items-start">
                    <div>
                      <p className="font-mono text-sm text-gray-600">{payment.id}</p>
                      <p className="text-lg font-semibold">${payment.amount.toFixed(2)}</p>
                      <p className="text-sm text-gray-500">{payment.created}</p>
                    </div>
                    <div className="text-right">
                      <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                        payment.status === 'succeeded' 
                          ? 'bg-green-100 text-green-800'
                          : payment.status === 'canceled'
                          ? 'bg-red-100 text-red-800'
                          : 'bg-yellow-100 text-yellow-800'
                      }`}>
                        {payment.status}
                      </span>
                      {payment.refunded && (
                        <div className="mt-1">
                          <span className="px-2 py-1 rounded-full text-xs font-medium bg-orange-100 text-orange-800">
                            Refunded: ${payment.refundAmount?.toFixed(2) || '0.00'}
                          </span>
                        </div>
                      )}
                    </div>
                  </div>

                  {payment.status === 'succeeded' && !payment.refunded && (
                    <div className="pt-2 border-t space-y-2">
                      <div className="flex gap-2">
                        <input
                          type="number"
                          min="0.01"
                          max={payment.amount}
                          step="0.01"
                          placeholder={`Max: $${payment.amount.toFixed(2)}`}
                          value={refundAmount[payment.id] || ''}
                          onChange={(e) => updateRefundAmount(payment.id, parseFloat(e.target.value) || 0)}
                          className="flex-1 px-2 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-1 focus:ring-blue-500"
                        />
                        <button
                          onClick={() => handleRefund(payment.id, refundAmount[payment.id])}
                          disabled={isRefunding === payment.id || !refundAmount[payment.id]}
                          className="px-3 py-1 text-sm bg-orange-500 text-white rounded hover:bg-orange-600 disabled:bg-gray-400"
                        >
                          {isRefunding === payment.id ? 'Refunding...' : 'Partial Refund'}
                        </button>
                      </div>
                      <button
                        onClick={() => handleRefund(payment.id)}
                        disabled={isRefunding === payment.id}
                        className="w-full px-3 py-1 text-sm bg-red-500 text-white rounded hover:bg-red-600 disabled:bg-gray-400"
                      >
                        {isRefunding === payment.id ? 'Refunding...' : 'Full Refund'}
                      </button>
                    </div>
                  )}
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

const PaymentTest = () => {
  return (
    <div className="min-h-screen bg-gray-100 py-8">
      <Elements stripe={stripePromise}>
        <CheckoutForm />
      </Elements>
    </div>
  );
};

export default PaymentTest;