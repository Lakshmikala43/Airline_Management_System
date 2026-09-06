import React, { useState, useEffect } from 'react';
import { Booking } from '../types';
import { api } from '../services/api';
import { TicketCard } from '../components/TicketCard';
import { Search, Ticket, Calendar, CheckCircle2, AlertCircle, RefreshCw } from 'lucide-react';

export const BookingLookupPage: React.FC = () => {
  const [pnr, setPnr] = useState('');
  const [singleBooking, setSingleBooking] = useState<Booking | null>(null);
  const [myBookings, setMyBookings] = useState<Booking[]>([]);
  const [selectedBooking, setSelectedBooking] = useState<Booking | null>(null);
  
  const [isLoading, setIsLoading] = useState(false);
  const [isListLoading, setIsListLoading] = useState(true);
  const [error, setError] = useState('');
  const [cancelResult, setCancelResult] = useState<any | null>(null);
  const [checkInMsg, setCheckInMsg] = useState<string | null>(null);

  const fetchMyBookings = async () => {
    setIsListLoading(true);
    try {
      const res = await api.get<Booking[]>('/bookings/my-bookings');
      setMyBookings(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setIsListLoading(false);
    }
  };

  useEffect(() => {
    fetchMyBookings();
  }, []);

  const handleLookup = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!pnr.trim()) return;

    setIsLoading(true);
    setError('');
    setSingleBooking(null);

    try {
      const res = await api.get<Booking>(`/bookings/pnr/${pnr.trim().toUpperCase()}`);
      setSingleBooking(res.data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Booking not found for the given PNR reference.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = async (pnrToCancel: string) => {
    if (!window.confirm(`Are you sure you want to cancel booking PNR ${pnrToCancel}?`)) return;
    try {
      const res = await api.post(`/bookings/${pnrToCancel}/cancel`);
      setCancelResult(res.data);
      fetchMyBookings();
      if (singleBooking?.pnr === pnrToCancel) {
        setSingleBooking(null);
      }
    } catch (err: any) {
      alert(err.response?.data?.message || 'Cancellation failed.');
    }
  };

  const handleCheckIn = (pnrToCheckIn: string) => {
    const updated = myBookings.map((b) => {
      if (b.pnr === pnrToCheckIn) {
        return { ...b, isCheckedIn: true, boardingTime: new Date(Date.now() + 82800000).toISOString() };
      }
      return b;
    });
    setMyBookings(updated);
    localStorage.setItem('skynova_mock_bookings', JSON.stringify(updated));
    setCheckInMsg(`CHECK-IN SUCCESSFUL! Boarding Pass & Seat Assigned for PNR ${pnrToCheckIn}.`);
  };

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      
      {/* Header */}
      <div className="text-center space-y-2">
        <div className="w-12 h-12 bg-orange-500 rounded-2xl text-white flex items-center justify-center mx-auto shadow-lg shadow-orange-500/30">
          <Ticket className="h-6 w-6" />
        </div>
        <h1 className="text-3xl font-black text-slate-900">Manage Your Booking</h1>
        <p className="text-xs text-slate-500 font-medium">Retrieve e-tickets, perform web check-in, check flight status, or request cancellations using your 6-character PNR</p>
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

      {/* My Flight Bookings & Boarding Passes Section */}
      <div className="bg-white rounded-3xl shadow-lg border border-slate-200 p-6 space-y-6">
        <h2 className="text-lg font-black text-slate-900 flex items-center space-x-2">
          <Calendar className="h-5 w-5 text-orange-500" />
          <span>My Flight Bookings & Boarding Passes</span>
        </h2>

        {isListLoading ? (
          <div className="text-center py-12 text-xs font-bold text-slate-500">Loading Bookings...</div>
        ) : myBookings.length === 0 ? (
          <div className="text-center py-12 text-xs text-slate-400 font-medium">No active bookings found. Search flights to create a reservation!</div>
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
                {myBookings.map((b) => (
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
