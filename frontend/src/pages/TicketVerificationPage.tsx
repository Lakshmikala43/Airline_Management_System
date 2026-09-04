import React, { useState } from 'react';
import { TicketVerification } from '../types';
import { api } from '../services/api';
import { ShieldCheck, Search, CheckCircle2, XCircle } from 'lucide-react';
import { format } from 'date-fns';

export const TicketVerificationPage: React.FC = () => {
  const [ticketNumber, setTicketNumber] = useState('');
  const [result, setResult] = useState<TicketVerification | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleVerify = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!ticketNumber.trim()) return;

    setIsLoading(true);
    setResult(null);
    try {
      const res = await api.get<TicketVerification>(`/tickets/verify/${ticketNumber.trim().toUpperCase()}`);
      setResult(res.data);
    } catch (err) {
      setResult({
        isValid: false,
        ticketNumber: ticketNumber.trim(),
        verificationMessage: 'Ticket validation endpoint unverified or ticket number invalid.',
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12 space-y-8">
      
      <div className="text-center space-y-2">
        <div className="w-12 h-12 bg-emerald-600 rounded-2xl text-white flex items-center justify-center mx-auto shadow-lg shadow-emerald-600/30">
          <ShieldCheck className="h-6 w-6" />
        </div>
        <h1 className="text-3xl font-black text-slate-900">E-Ticket Authenticity Verification</h1>
        <p className="text-xs text-slate-500">Verify the authenticity of any SkyNova Airways issued electronic ticket</p>
      </div>

      <div className="bg-white rounded-3xl shadow-xl border border-slate-200 p-8 max-w-lg mx-auto">
        <form onSubmit={handleVerify} className="space-y-4">
          <div>
            <label className="text-xs font-bold text-slate-700 block mb-1">Enter Ticket Number</label>
            <div className="relative">
              <Search className="h-4 w-4 text-slate-400 absolute left-3.5 top-3.5" />
              <input
                type="text"
                value={ticketNumber}
                onChange={(e) => setTicketNumber(e.target.value.toUpperCase())}
                placeholder="e.g. TK-K7P4M2-01"
                required
                className="w-full text-sm font-mono font-bold uppercase bg-slate-50 border border-slate-200 rounded-xl py-3 pl-10 pr-3 focus:ring-2 focus:ring-emerald-500 outline-none"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full bg-emerald-600 hover:bg-emerald-500 text-white font-extrabold text-xs py-3.5 rounded-xl shadow-md transition-all"
          >
            {isLoading ? 'VERIFYING WITH REGISTRY...' : 'VERIFY TICKET'}
          </button>
        </form>
      </div>

      {result && (
        <div className={`p-6 rounded-3xl border shadow-lg max-w-xl mx-auto space-y-4 ${
          result.isValid ? 'bg-emerald-50/80 border-emerald-200 text-emerald-950' : 'bg-rose-50/80 border-rose-200 text-rose-950'
        }`}>
          <div className="flex items-center space-x-3">
            {result.isValid ? (
              <CheckCircle2 className="h-8 w-8 text-emerald-600 shrink-0" />
            ) : (
              <XCircle className="h-8 w-8 text-rose-600 shrink-0" />
            )}
            <div>
              <h3 className="text-base font-extrabold">{result.isValid ? 'Authentic SkyNova E-Ticket' : 'Invalid Ticket Number'}</h3>
              <p className="text-xs font-medium opacity-80">{result.verificationMessage}</p>
            </div>
          </div>

          {result.isValid && (
            <div className="grid grid-cols-2 gap-4 text-xs pt-3 border-t border-emerald-200/60 font-mono">
              <div>
                <span className="text-[10px] uppercase text-emerald-700 block font-bold">Ticket Number</span>
                <span className="font-extrabold">{result.ticketNumber}</span>
              </div>
              <div>
                <span className="text-[10px] uppercase text-emerald-700 block font-bold">PNR Reference</span>
                <span className="font-extrabold">{result.pnr}</span>
              </div>
              <div>
                <span className="text-[10px] uppercase text-emerald-700 block font-bold">Passenger</span>
                <span className="font-extrabold">{result.passengerName}</span>
              </div>
              <div>
                <span className="text-[10px] uppercase text-emerald-700 block font-bold">Flight</span>
                <span className="font-extrabold">{result.flightNumber} ({result.originAirport} → {result.destinationAirport})</span>
              </div>
            </div>
          )}
        </div>
      )}

    </div>
  );
};
