import React, { useState } from 'react';
import { Booking } from '../types';
import { api } from '../services/api';
import { TicketCard } from '../components/TicketCard';
import { Search, Ticket, AlertCircle } from 'lucide-react';

export const BookingLookupPage: React.FC = () => {
  const [pnr, setPnr] = useState('');
  const [booking, setBooking] = useState<Booking | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleLookup = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!pnr.trim()) return;

    setIsLoading(true);
    setError('');
    setBooking(null);

    try {
      const res = await api.get<Booking>(`/bookings/pnr/${pnr.trim().toUpperCase()}`);
      setBooking(res.data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Booking not found for the given PNR reference.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12 space-y-8">
      
      <div className="text-center space-y-2">
        <div className="w-12 h-12 bg-sky-600 rounded-2xl text-white flex items-center justify-center mx-auto shadow-lg shadow-sky-600/30">
          <Ticket className="h-6 w-6" />
        </div>
        <h1 className="text-3xl font-black text-slate-900">Manage Your Booking</h1>
        <p className="text-xs text-slate-500">Retrieve e-tickets, check flight status, or request cancellations using your PNR</p>
      </div>

      {/* Lookup Card */}
      <div className="bg-white rounded-3xl shadow-xl border border-slate-200 p-8 max-w-xl mx-auto">
        <form onSubmit={handleLookup} className="space-y-4">
          <div>
            <label className="text-xs font-bold text-slate-700 block mb-1">Enter 6-Character PNR Reference</label>
            <div className="relative">
              <Search className="h-4 w-4 text-slate-400 absolute left-3.5 top-3.5" />
              <input
                type="text"
                value={pnr}
                onChange={(e) => setPnr(e.target.value.toUpperCase())}
                placeholder="e.g. K7P4M2"
                maxLength={6}
                required
                className="w-full text-sm font-mono font-bold uppercase bg-slate-50 border border-slate-200 rounded-xl py-3 pl-10 pr-3 focus:ring-2 focus:ring-sky-500 outline-none"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full bg-sky-600 hover:bg-sky-500 text-white font-extrabold text-xs py-3.5 rounded-xl shadow-md transition-all"
          >
            {isLoading ? 'LOOKING UP BOOKING...' : 'RETRIEVE BOOKING'}
          </button>
        </form>

        {error && (
          <div className="mt-4 bg-rose-50 border border-rose-200 p-3 rounded-xl flex items-center space-x-2 text-xs font-semibold text-rose-700">
            <AlertCircle className="h-4 w-4 shrink-0" />
            <span>{error}</span>
          </div>
        )}
      </div>

      {/* Booking Display */}
      {booking && (
        <div className="space-y-6">
          <TicketCard booking={booking} />
        </div>
      )}

    </div>
  );
};
