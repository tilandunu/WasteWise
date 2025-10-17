import React, { useEffect, useState } from 'react';
import axios from '../config/axiosInstance';
import Header from '../components/header';
import { Card, CardHeader, CardBody } from '@heroui/card';
import { Button } from '@heroui/button';
import { Input } from '@heroui/react';

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
  const [applyAmount, setApplyAmount] = useState<number>(0);
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [mockUser, setMockUser] = useState<string>('');
  const [paymentMethod, setPaymentMethod] = useState<'CASH' | 'CARD'>('CASH');
  const [cardNumber, setCardNumber] = useState('');
  const [cardExpiry, setCardExpiry] = useState('');
  const [cardCvv, setCardCvv] = useState('');

  useEffect(() => {
    const stored = localStorage.getItem('loggedUser');
    if (stored) {
      const u = JSON.parse(stored);
      setUserId(u.id);
      fetchSummary(u.id);
    }
  }, []);

  const fetchSummary = async (uid: string) => {
    try {
      const res = await axios.get(`/api/billing/${uid}`);
      setBalance(res.data.balance ?? 0);
      setCredits(res.data.credits ?? 0);
      setPayments(res.data.payments ?? []);
    } catch (err) {
      console.error(err);
      setError('Failed to load billing summary');
    }
  };

  const handlePay = async () => {
    if (!userId || amount <= 0) {
      setError('Enter a valid amount to pay');
      return;
    }
    setPaying(true);
    setError(null);
    setMessage(null);
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
        if (!cardNumber || cardNumber.length < 12) {
          setError('Enter a valid card number');
          setPaying(false);
          return;
        }
        setMessage('Processing card payment...');
        await new Promise((r) => setTimeout(r, 1200));
        const masked = cardNumber ? `**** **** **** ${cardNumber.slice(-4)}` : 'CARD';
        await axios.post(`/api/billing/${userId}/pay`, {
          method: 'CARD',
          amount,
          details: `Card ${masked}`,
        });
        setMessage('Card payment processed (mock).');
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
    <div className='max-w-4xl mx-auto p-6'>
      <Header />
      <Card>
        <CardHeader className='text-lg font-semibold'>
          Billing & Rewards
        </CardHeader>
        <CardBody>
          {message && (
            <div className='p-2 mb-4 bg-success-100 text-success rounded'>
              {message}
            </div>
          )}
          {error && (
            <div className='p-2 mb-4 bg-danger-100 text-danger rounded'>
              {error}
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
          <div className='mb-4 mt-2 p-2 border rounded'>
            <h4 className='font-medium'>Admin: Generate Monthly Invoice</h4>
            <div className='flex gap-2 items-center mt-2'>
              <Input placeholder='Amount (LKR)' value={String(1000)} readOnly />
              <Button onPress={async () => {
                if (!confirm('Generate monthly invoice for all users with amount 1000 LKR?')) return;
                try {
                  await axios.post('/api/billing/generate-monthly?amount=1000');
                  alert('Invoices generated');
                  // refresh visible summary if a user is selected
                  if (userId) await fetchSummary(userId);
                } catch (e) {
                  console.error(e);
                  alert('Failed to generate invoices');
                }
              }}>Generate 1000 LKR</Button>
            </div>
          </div>
          <div className='grid grid-cols-1 md:grid-cols-3 gap-4'>
            <div className='p-4 border rounded'>
              <h3 className='text-sm text-default-500'>Balance (LKR)</h3>
              <p className='text-2xl font-bold'>{balance}</p>
            </div>
            <div className='p-4 border rounded'>
              <h3 className='text-sm text-default-500'>Credits (LKR)</h3>
              <p className='text-2xl font-bold'>{credits}</p>
            </div>
            <div className='p-4 border rounded'>
              <h3 className='text-sm text-default-500'>Actions</h3>
              <div className='space-y-2 mt-2'>
                <div>
                  <Input
                    value={String(amount || '')}
                    onChange={(e: any) =>
                      setAmount(Number(e.target.value || 0))
                    }
                    placeholder='Amount to pay'
                    type='number'
                  />
                  <div className='mt-2'>
                    <div className='flex items-center gap-4 mb-2'>
                      <label className={`cursor-pointer ${paymentMethod === 'CASH' ? 'font-semibold' : ''}`}>
                        <input type='radio' name='pm' checked={paymentMethod === 'CASH'} onChange={() => setPaymentMethod('CASH')} /> Cash
                      </label>
                      <label className={`cursor-pointer ${paymentMethod === 'CARD' ? 'font-semibold' : ''}`}>
                        <input type='radio' name='pm' checked={paymentMethod === 'CARD'} onChange={() => setPaymentMethod('CARD')} /> Card
                      </label>
                    </div>

                    {paymentMethod === 'CARD' && (
                      <div className='space-y-2 mb-2'>
                        <Input value={cardNumber} onChange={(e: any) => setCardNumber(e.target.value)} placeholder='Card number' />
                        <div className='flex gap-2'>
                          <Input value={cardExpiry} onChange={(e: any) => setCardExpiry(e.target.value)} placeholder='MM/YY' />
                          <Input value={cardCvv} onChange={(e: any) => setCardCvv(e.target.value)} placeholder='CVV' />
                        </div>
                      </div>
                    )}

                    <Button className='mt-2' onPress={handlePay} isLoading={paying}>
                      Pay Now
                    </Button>
                  </div>
                </div>

                <div>
                  <Input
                    value={String(applyAmount || '')}
                    onChange={(e: any) =>
                      setApplyAmount(Number(e.target.value || 0))
                    }
                    placeholder='Credits to apply'
                    type='number'
                  />
                  <Button className='mt-2' onPress={handleApply}>
                    Apply Credits
                  </Button>
                </div>
              </div>
            </div>
          </div>

          <div className='mt-6'>
            <h4 className='font-semibold'>Payment History</h4>
            <ul className='mt-2 space-y-1'>
              {payments.map((p) => (
                <li key={p.id} className='flex justify-between'>
                  <span>
                    {p.method} {p.amount} LKR —{' '}
                    {new Date(p.timestamp).toLocaleString()}
                  </span>
                  <span className='text-sm text-default-500'>{p.status}</span>
                </li>
              ))}
            </ul>
          </div>
        </CardBody>
      </Card>
    </div>
  );
};

export default BillingPage;
