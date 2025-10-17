import React, { useEffect, useState } from 'react';
import axios from '../config/axiosInstance';
import Header from '../components/header';
import { Card, CardHeader, CardBody } from '@heroui/card';
import { Button } from '@heroui/button';
import { Input } from '@heroui/react';
import { Spinner } from '@heroui/spinner';

interface PaymentRecord {
  id: string;
  transactionId: string;
  amount: number;
  method: string;
  status: string;
  timestamp: string;
  note?: string;
}

const BillingPage: React.FC = () => {
  const [userId, setUserId] = useState<string | null>(null);
  const [balance, setBalance] = useState<number>(0);
  const [credits, setCredits] = useState<number>(0);
  const [payments, setPayments] = useState<PaymentRecord[]>([]);
  const [amount, setAmount] = useState<number>(0);
  const [paying, setPaying] = useState(false);
  const [loading, setLoading] = useState(false);
  const [applyAmount, setApplyAmount] = useState<number>(0);
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [mockUser, setMockUser] = useState<string>('');
  const [paymentMethod, setPaymentMethod] = useState<'CASH' | 'CARD'>('CASH');
  const [cardNumber, setCardNumber] = useState('');
  const [cardExpiry, setCardExpiry] = useState('');
  const [cardCvv, setCardCvv] = useState('');
  const [cardModalOpen, setCardModalOpen] = useState(false);

  useEffect(() => {
    const stored = localStorage.getItem('loggedUser');
    if (stored) {
      const u = JSON.parse(stored);
      setUserId(u.id);
      fetchSummary(u.id);
    }
  }, []);

  const fetchSummary = async (uid: string) => {
    setLoading(true);
    setError(null);
    try {
      const res = await axios.get(`/api/billing/${uid}`);
      setBalance(res.data.balance ?? 0);
      setCredits(res.data.credits ?? 0);
      setPayments(res.data.payments ?? []);
    } catch (err) {
      console.error(err);
      setError('Failed to load billing summary');
    } finally {
      setLoading(false);
    }
  };

  const handlePay = async () => {
    setMessage(null);
    setError(null);
    if (!userId) {
      setError('No user selected');
      return;
    }
    if (amount <= 0) {
      setError('Enter a valid amount to pay');
      return;
    }
    setPaying(true);
    try {
      if (paymentMethod === 'CASH') {
        await axios.post(`/api/billing/${userId}/pay`, {
          method: 'CASH',
          amount,
          details: 'Paid in cash',
        });
        setMessage('Cash payment recorded.');
      } else {
        // Basic client-side validation for card
        if (!cardNumber || cardNumber.replace(/\s+/g, '').length < 12) {
          setError('Enter a valid card number');
          setPaying(false);
          return;
        }
        setMessage('Processing card payment...');
        // show modal if not already open so user sees card preview
        setCardModalOpen(true);
        await new Promise((r) => setTimeout(r, 900));
        const masked = cardNumber
          ? `**** **** **** ${cardNumber.slice(-4)}`
          : 'CARD';
        await axios.post(`/api/billing/${userId}/pay`, {
          method: 'CARD',
          amount,
          details: `Card ${masked}`,
        });
        setMessage('Card payment processed (mock).');
        setCardModalOpen(false);
      }

      // refresh
      await fetchSummary(userId);
      setAmount(0);
      setCardNumber('');
      setCardExpiry('');
      setCardCvv('');
      setError(null);
    } catch (err) {
      console.error(err);
      setError('Payment failed. See console for details.');
    } finally {
      setPaying(false);
    }
  };

  const handleApply = async () => {
    if (!userId || applyAmount <= 0) {
      setError('Enter a valid credit amount to apply');
      return;
    }
    try {
      await axios.post(`/api/billing/${userId}/apply-credits`, {
        creditsToApply: applyAmount,
      });
      await fetchSummary(userId);
      setApplyAmount(0);
      setMessage('Credits applied successfully');
      setError(null);
    } catch (err) {
      console.error(err);
      setError('Failed to apply credits');
    }
  };

  const handleUseMockUser = async () => {
    if (!mockUser) return;
    setUserId(mockUser);
    await fetchSummary(mockUser);
  };

  const downloadReceipt = (p: PaymentRecord) => {
    const lines = [] as string[];
    lines.push('Receipt');
    lines.push('---------');
    lines.push(`Transaction ID: ${p.transactionId}`);
    lines.push(`Method: ${p.method}`);
    lines.push(`Amount: ${p.amount} LKR`);
    lines.push(`Status: ${p.status}`);
    lines.push(`Time: ${new Date(p.timestamp).toLocaleString()}`);
    if (p.note) lines.push(`Note: ${p.note}`);

    const blob = new Blob([lines.join('\n')], {
      type: 'text/plain;charset=utf-8',
    });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `receipt-${p.transactionId}.txt`;
    document.body.appendChild(a);
    a.click();
    a.remove();
    URL.revokeObjectURL(url);
  };

  return (
    <div className='max-w-full mx-auto p-6'>
      <Header />
      <Card className='mt-10 p-10'>
        <CardHeader>Billing & Rewards</CardHeader>
        <CardBody>
          {/* simple toasts */}
          {message && (
            <div className='mb-4 bg-success-100 text-success rounded-full px-6 py-4'>
              {message}
            </div>
          )}
          {error && (
            <div
              className='p-2 mb-4 bg-danger-100 text-danger rounded'
              role='alert'
            >
              {error}
            </div>
          )}

          {loading && (
            <div className='flex items-center gap-2 mb-4'>
              <Spinner size='sm' />
              <span className='text-sm text-default-500'>
                Loading billing summary...
              </span>
            </div>
          )}

          {!userId && (
            <div className='mb-4'>
              <p className='text-sm text-default-500'>
                No user logged in — you can enter a user id to preview the
                billing page:
              </p>
              <div className='flex gap-2 mt-2'>
                <Input
                  value={mockUser}
                  onChange={(e: any) => setMockUser(e.target.value)}
                  placeholder='User id (e.g. 63ab...)'
                />
                <Button onPress={handleUseMockUser}>Use</Button>
              </div>
            </div>
          )}
          {/* Admin: generate monthly invoices (visible to simplicity) */}
          <div className='mb-4 mt-2 p-6 shadow-medium rounded-3xl bg-default-50'>
            <div className='flex items-center justify-between'>
              <h4 className='font-medium'>Admin: Generate Monthly Invoice</h4>
              <small className='text-default-500'>Admin only</small>
            </div>
            <div className='flex gap-2 items-center mt-3'>
              <Input placeholder='Amount (LKR)' value={String(1000)} readOnly />
              <Button
                onPress={async () => {
                  if (
                    !confirm(
                      'Generate monthly invoice for all users with amount 1000 LKR?'
                    )
                  )
                    return;
                  try {
                    await axios.post(
                      '/api/billing/generate-monthly?amount=1000'
                    );
                    setMessage('Monthly invoices generated for all users.');
                    // refresh visible summary if a user is selected
                    if (userId) await fetchSummary(userId);
                  } catch (e) {
                    console.error(e);
                    setError('Failed to generate invoices');
                  }
                }}
                className='px-10'
              >
                Generate 1000 LKR
              </Button>
            </div>
            <p className='text-sm text-default-500 mt-2'>
              This creates a pending invoice entry for every user. Payments
              remain manual.
            </p>
          </div>
          <div className='grid grid-cols-1 md:grid-cols-3 gap-3'>
            <div className='p-6 shadow-medium rounded-2xl'>
              <h3 className='text-sm text-default-500'>Balance (LKR)</h3>
              <p className='text-2xl font-bold'>{balance.toLocaleString()}</p>
              <p className='text-sm text-default-400 mt-1'>
                Outstanding amount
              </p>
            </div>
            <div className='p-6 shadow-medium rounded-2xl'>
              <h3 className='text-sm text-default-500'>Credits (LKR)</h3>
              <p className='text-2xl font-bold'>{credits.toLocaleString()}</p>
              <p className='text-sm text-default-400 mt-1'>
                Recyclable credits available
              </p>
            </div>
            <div className='p-6 shadow-medium rounded-2xl'>
              <h3 className='text-sm text-default-500'>Actions</h3>
              <div className='space-y-8 mt-3'>
                <div className='flex flex-col'>
                  <Input
                    value={String(amount || '')}
                    onChange={(e: any) =>
                      setAmount(Number(e.target.value || 0))
                    }
                    placeholder='Amount to pay'
                    type='number'
                    aria-label='Amount to pay'
                    className='border border-default-300 rounded-md w-full mb-2'
                  />
                  <div className='mt-2'>
                    <div className='flex items-center gap-4 mb-2'>
                      <label
                        className={`cursor-pointer ${paymentMethod === 'CASH' ? 'font-semibold' : ''}`}
                      >
                        <input
                          type='radio'
                          name='pm'
                          checked={paymentMethod === 'CASH'}
                          onChange={() => setPaymentMethod('CASH')}
                        />{' '}
                        Cash
                      </label>
                      <label
                        className={`cursor-pointer ${paymentMethod === 'CARD' ? 'font-semibold' : ''}`}
                      >
                        <input
                          type='radio'
                          name='pm'
                          checked={paymentMethod === 'CARD'}
                          onChange={() => setPaymentMethod('CARD')}
                        />{' '}
                        Card
                      </label>
                    </div>

                    {paymentMethod === 'CARD' && (
                      <div className='space-y-2 mb-2'>
                        <div className='flex gap-2'>
                          <Input
                            value={cardNumber}
                            onChange={(e: any) => setCardNumber(e.target.value)}
                            placeholder='Card number (xxxx xxxx xxxx 1234)'
                          />
                        </div>
                        <div className='flex gap-2'>
                          <Input
                            value={cardExpiry}
                            onChange={(e: any) => setCardExpiry(e.target.value)}
                            placeholder='MM/YY'
                          />
                          <Input
                            value={cardCvv}
                            onChange={(e: any) => setCardCvv(e.target.value)}
                            placeholder='CVV'
                          />
                        </div>
                        <div className='text-sm text-default-500'>
                          Card data is not stored. This is a mock payment flow.
                        </div>
                      </div>
                    )}

                    <Button
                      className='mt-2 w-full'
                      onPress={handlePay}
                      isLoading={paying}
                    >
                      Pay Now
                    </Button>
                  </div>
                </div>

                <div className='flex flex-col'>
                  <Input
                    value={String(applyAmount || '')}
                    onChange={(e: any) =>
                      setApplyAmount(Number(e.target.value || 0))
                    }
                    placeholder='Credits to apply'
                    type='number'
                    className='border border-default-300 rounded-md w-full'
                  />
                  <Button className='mt-2' onPress={handleApply}>
                    Apply Credits
                  </Button>
                </div>
              </div>
            </div>
          </div>

          <div className='mt-20'>
            <div className='flex items-center justify-between'>
              <h4 className='font-semibold'>Payment History</h4>
              <small className='text-sm text-default-500'>
                {payments.length} records
              </small>
            </div>

            <div className='mt-10 border-default-300 rounded'>
              {/* fixed height scrollable table */}
              <div className='overflow-y-auto max-h-[260px]'>
                <table className='w-full text-sm'>
                  <thead className='py-10'>
                    <tr className='text-white bg-red-800 sticky top-0 z-50 '>
                      <th className='px-3 py-2 text-center w-1/4'>Date</th>
                      <th className='px-3 py-2 text-center w-1/6'>Method</th>
                      <th className='px-3 py-2 text-center w-1/6'>
                        Amount (LKR)
                      </th>
                      <th className='px-3 py-2 text-center w-1/6'>Status</th>
                      <th className='px-3 py-2 text-center w-1/6'>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {payments.length === 0 && (
                      <tr>
                        <td
                          colSpan={5}
                          className='px-3 py-6 text-center text-default-500'
                        >
                          No payment records
                        </td>
                      </tr>
                    )}
                    {payments.map((p) => (
                      <tr key={p.id} className='border-t border-default-300'>
                        <td className='px-3 py-3 align-top text-center '>
                          {new Date(p.timestamp).toLocaleString()}
                        </td>
                        <td className='px-3 py-3 align-top text-center '>
                          {p.method}
                        </td>
                        <td className='px-3 py-3 align-top text-center font-medium '>
                          {p.amount.toLocaleString()}
                        </td>
                        <td className='px-3 py-3 align-top text-center '>
                          {p.status}
                        </td>
                        <td className='px-3 py-3 align-top text-center'>
                          <Button size='sm' onPress={() => downloadReceipt(p)}>
                            Download
                          </Button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </CardBody>
      </Card>
    </div>
  );
};

export default BillingPage;
