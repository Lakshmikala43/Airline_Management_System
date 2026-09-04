import React, { useState } from 'react';
import { CreditCard, ShieldAlert, Lock, CheckCircle2 } from 'lucide-react';

interface MockPaymentFormProps {
  amount: number;
  onPay: (paymentDetails: { paymentMethod: string; cardHolderName: string; cardNumber: string }) => void;
  isProcessing: boolean;
}

export const MockPaymentForm: React.FC<MockPaymentFormProps> = ({
  amount,
  onPay,
  isProcessing,
}) => {
  const [cardHolderName, setCardHolderName] = useState('John Traveler');
  const [cardNumber, setCardNumber] = useState('4242 4242 4242 4242');
  const [expiry, setExpiry] = useState('12/28');
  const [cvv, setCvv] = useState('123');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onPay({
      paymentMethod: 'MOCK_CARD',
      cardHolderName,
      cardNumber,
    });
  };

  const autofillDemo = () => {
    setCardHolderName('Valued Traveler');
    setCardNumber('4242 4242 4242 4242');
    setExpiry('12/28');
    setCvv('789');
  };

  return (
    <div className="bg-white rounded-2xl border border-slate-200 p-6 sm:p-8 shadow-xl max-w-xl mx-auto space-y-6">
      
      {/* Demo Warning Header */}
      <div className="bg-amber-50 border border-amber-200 p-4 rounded-xl flex items-start space-x-3">
        <ShieldAlert className="h-5 w-5 text-amber-600 shrink-0 mt-0.5" />
        <div>
          <h4 className="text-xs font-bold text-amber-900 uppercase tracking-wider">Demo Payment Environment</h4>
          <p className="text-xs text-amber-700 mt-0.5">
            This is a simulated payment gateway. No real money or actual credit cards are charged.
          </p>
        </div>
      </div>

      <div className="flex items-center justify-between border-b border-slate-100 pb-4">
        <h3 className="text-lg font-extrabold text-slate-900 flex items-center space-x-2">
          <CreditCard className="h-5 w-5 text-sky-600" />
          <span>Payment Simulation</span>
        </h3>
        <button
          type="button"
          onClick={autofillDemo}
          className="text-xs font-bold text-sky-600 bg-sky-50 px-3 py-1.5 rounded-lg border border-sky-200 hover:bg-sky-100 transition-colors"
        >
          Auto-fill Demo Card
        </button>
      </div>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="text-xs font-bold text-slate-700 block mb-1">Cardholder Name</label>
          <input
            type="text"
            value={cardHolderName}
            onChange={(e) => setCardHolderName(e.target.value)}
            required
            className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-lg p-3 focus:ring-2 focus:ring-sky-500 outline-none"
          />
        </div>

        <div>
          <label className="text-xs font-bold text-slate-700 block mb-1">Test Credit Card Number</label>
          <input
            type="text"
            value={cardNumber}
            onChange={(e) => setCardNumber(e.target.value)}
            required
            className="w-full text-xs font-mono font-bold bg-slate-50 border border-slate-200 rounded-lg p-3 focus:ring-2 focus:ring-sky-500 outline-none"
          />
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="text-xs font-bold text-slate-700 block mb-1">Expiry Date</label>
            <input
              type="text"
              value={expiry}
              onChange={(e) => setExpiry(e.target.value)}
              placeholder="MM/YY"
              required
              className="w-full text-xs font-mono font-semibold bg-slate-50 border border-slate-200 rounded-lg p-3 focus:ring-2 focus:ring-sky-500 outline-none"
            />
          </div>
          <div>
            <label className="text-xs font-bold text-slate-700 block mb-1">CVV (Demo Only)</label>
            <input
              type="password"
              value={cvv}
              onChange={(e) => setCvv(e.target.value)}
              maxLength={4}
              required
              className="w-full text-xs font-mono font-semibold bg-slate-50 border border-slate-200 rounded-lg p-3 focus:ring-2 focus:ring-sky-500 outline-none"
            />
          </div>
        </div>

        <button
          type="submit"
          disabled={isProcessing}
          className="w-full bg-emerald-600 hover:bg-emerald-500 text-white font-extrabold text-sm py-4 rounded-xl shadow-lg shadow-emerald-600/30 flex items-center justify-center space-x-2 transition-all hover:scale-[1.01]"
        >
          <Lock className="h-4 w-4" />
          <span>{isProcessing ? 'AUTHORIZING MOCK PAYMENT...' : `CONFIRM & PAY $${amount.toFixed(2)}`}</span>
        </button>
      </form>

    </div>
  );
};
