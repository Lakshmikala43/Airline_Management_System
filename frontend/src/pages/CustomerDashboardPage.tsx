import React, { useState, useEffect } from 'react';
import { Booking } from '../types';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { TicketCard } from '../components/TicketCard';
import { Plane, Calendar, CreditCard, AlertCircle, XCircle, CheckCircle2, RefreshCw, Luggage, ShieldCheck } from 'lucide-react';
import { format } from 'date-fns';

export const CustomerDashboardPage: React.FC = () => {
  const { user } = useAuth();

  const [bookings, setBookings] = useState<Booking[]>([]);
  const [selectedBooking, setSelectedBooking] = useState<Booking | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [cancelResult, setCancelResult] = useState<any | null>(null);
  const [checkInMsg, setCheckInMsg] = useState<string | null>(null);

  const fetchBookings = async () => {
    setIsLoading(true);
    try {
      const res = await api.get<Booking[]>('/bookings/my-bookings');
      setBookings(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchBookings();
  }, []);

  const handleCancel = async (pnr: string) => {
    if (!window.confirm(`Are you sure you want to cancel booking PNR ${pnr}?`)) return;
    try {
      const res = await api.post(`/bookings/${pnr}/cancel`);
      setCancelResult(res.data);
      fetchBookings();
    } catch (err: any) {
      alert(err.response?.data?.message || 'Cancellation failed.');
    }
  };

  const handleCheckIn = (pnr: string) => {
    const updated = bookings.map((b) => {
      if (b.pnr === pnr) {
        return { ...b, isCheckedIn: true, boardingTime: new Date(Date.now() + 82800000).toISOString() };
      }
      return b;
    });
    setBookings(updated);
    localStorage.setItem('skynova_mock_bookings', JSON.stringify(updated));
    setCheckInMsg(`CHECK-IN SUCCESSFUL! Boarding Pass & Seat Assigned for PNR ${pnr}.`);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      
      {/* User Header */}
      <div className="bg-gradient-to-r from-orange-600 via-orange-500 to-amber-500 text-white rounded-3xl p-8 shadow-xl flex flex-col md:flex-row items-center justify-between gap-6 border border-orange-400">
        <div>
          <h1 className="text-3xl font-black tracking-tight">Welcome Back, {user?.firstName || 'Passenger'}!</h1>
          <p className="text-xs text-orange-100 font-medium mt-1">Passenger Booking Portal — Manage flight reservations, perform web check-in, and retrieve electronic boarding passes.</p>
        </div>
        <div className="flex space-x-4">
          <div className="bg-white/10 backdrop-blur-md px-4 py-2.5 rounded-xl border border-white/20 text-center">
            <span className="text-[10px] uppercase font-bold text-orange-100 block">Total Bookings</span>
            <span className="text-xl font-black text-white">{bookings.length}</span>
          </div>
          <div className="bg-white/10 backdrop-blur-md px-4 py-2.5 rounded-xl border border-white/20 text-center">
            <span className="text-[10px] uppercase font-bold text-orange-100 block">Confirmed Trips</span>
            <span className="text-xl font-black text-white">
              {bookings.filter((b) => b.status === 'CONFIRMED').length}
            </span>
          </div>
        </div>
      </div>

      {checkInMsg && (
        <div className="bg-emerald-50 border border-emerald-200 p-4 rounded-2xl flex items-center justify-between text-xs font-bold text-emerald-900 shadow-sm">
          <div className="flex items-center space-x-2">
            <CheckCircle2 className="h-5 w-5 text-emerald-600 shrink-0" />
            <span>{checkInMsg}</span>
          </div>
          <button onClick={() => setCheckInMsg(null)} className="text-emerald-700 font-bold underline">Dismiss</button>
        </div>
      )}

      {cancelResult && (
        <div className="bg-amber-50 border border-amber-200 p-4 rounded-2xl flex items-center justify-between text-xs font-bold text-amber-900 shadow-sm">
          <div className="flex items-center space-x-2">
            <AlertCircle className="h-5 w-5 text-amber-600 shrink-0" />
            <span>
              Booking PNR {cancelResult.pnr} Cancelled. Original: ${cancelResult.originalAmount} | Fee: ${cancelResult.cancellationFee} | Refund: ${cancelResult.refundAmount}
            </span>
          </div>
          <button onClick={() => setCancelResult(null)} className="text-amber-700 font-bold underline">Dismiss</button>
        </div>
      )}

      {/* Bookings Table */}
      <div className="bg-white rounded-3xl shadow-lg border border-slate-200 p-6 space-y-6">
        <h2 className="text-lg font-black text-slate-900 flex items-center space-x-2">
          <Calendar className="h-5 w-5 text-orange-500" />
          <span>My Flight Bookings & Boarding Passes</span>
        </h2>

        {isLoading ? (
          <div className="text-center py-12 text-xs font-bold text-slate-500">Loading Bookings...</div>
        ) : bookings.length === 0 ? (
          <div className="text-center py-12 text-xs text-slate-400 font-medium">No bookings found. Try searching for flights to book!</div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-50 text-slate-500 uppercase font-bold border-b border-slate-200">
                <tr>
                  <th className="p-3">PNR</th>
                  <th className="p-3">Flight #</th>
                  <th className="p-3">Route</th>
                  <th className="p-3">Seat</th>
                  <th className="p-3">Total Fare</th>
                  <th className="p-3">Check-in Status</th>
                  <th className="p-3 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 font-medium">
                {bookings.map((b) => (
                  <tr key={b.id} className="hover:bg-slate-50/80 transition-colors">
                    <td className="p-3 font-mono font-extrabold text-orange-600">{b.pnr}</td>
                    <td className="p-3 font-bold text-slate-900">{b.flight.flightNumber}</td>
                    <td className="p-3">{b.flight.originAirportCode} → {b.flight.destinationAirportCode}</td>
                    <td className="p-3 font-extrabold text-orange-600">
                      {b.passengers?.map((p) => p.seatNumber).join(', ') || '3A'}
                    </td>
                    <td className="p-3 font-bold text-slate-900">${b.totalAmount}</td>
                    <td className="p-3">
                      {b.isCheckedIn ? (
                        <span className="inline-flex items-center space-x-1 px-2.5 py-1 rounded-full text-[10px] font-bold bg-emerald-100 text-emerald-800">
                          <CheckCircle2 className="h-3 w-3" />
                          <span>Checked-In</span>
                        </span>
                      ) : b.status === 'CONFIRMED' ? (
                        <button
                          onClick={() => handleCheckIn(b.pnr)}
                          className="px-2.5 py-1 rounded-lg text-[10px] font-extrabold bg-orange-500 hover:bg-orange-600 text-white shadow-sm transition-all"
                        >
                          Web Check-In
                        </button>
                      ) : (
                        <span className="px-2.5 py-1 rounded-full text-[10px] font-bold bg-slate-100 text-slate-700">
                          {b.status}
                        </span>
                      )}
                    </td>
                    <td className="p-3 text-right space-x-2">
                      <button
                        onClick={() => setSelectedBooking(b)}
                        className="text-xs font-bold text-orange-600 hover:text-orange-700 underline"
                      >
                        Boarding Pass
                      </button>
                      {b.status === 'CONFIRMED' && (
                        <button
                          onClick={() => handleCancel(b.pnr)}
                          className="text-xs font-bold text-rose-600 hover:text-rose-800 underline"
                        >
                          Cancel
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Ticket & Boarding Pass Viewer Modal */}
      {selectedBooking && (
        <div className="fixed inset-0 bg-slate-950/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl max-w-3xl w-full max-h-[90vh] overflow-y-auto p-6 relative shadow-2xl">
            <button
              onClick={() => setSelectedBooking(null)}
              className="absolute top-4 right-4 text-slate-400 hover:text-slate-700 font-extrabold text-sm"
            >
              ✕ Close
            </button>
            <TicketCard booking={selectedBooking} />
          </div>
        </div>
      )}

    </div>
  );
};
