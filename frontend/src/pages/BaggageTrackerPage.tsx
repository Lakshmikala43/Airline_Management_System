import React, { useState } from 'react';
import { api } from '../services/api';
import { Luggage, Search, CheckCircle2, Clock, AlertCircle } from 'lucide-react';

export const BaggageTrackerPage: React.FC = () => {
  const [tagNumber, setTagNumber] = useState('');
  const [baggageData, setBaggageData] = useState<any | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleTrack = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!tagNumber.trim()) return;

    setIsLoading(true);
    setError('');
    setBaggageData(null);

    try {
      const res = await api.get(`/baggage/track/${tagNumber.trim().toUpperCase()}`);
      setBaggageData(res.data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Baggage tag not found in tracking registry.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-12 space-y-8">
      <div className="text-center space-y-2">
        <div className="w-12 h-12 bg-sky-600 rounded-2xl text-white flex items-center justify-center mx-auto shadow-lg shadow-sky-600/30">
          <Luggage className="h-6 w-6" />
        </div>
        <h1 className="text-3xl font-black text-slate-900">Live Baggage Tracker</h1>
        <p className="text-xs text-slate-500">Track your checked luggage status and carousel location in real time</p>
      </div>

      <div className="bg-white rounded-3xl shadow-xl border border-slate-200 p-8 max-w-lg mx-auto">
        <form onSubmit={handleTrack} className="space-y-4">
          <div>
            <label className="text-xs font-bold text-slate-700 block mb-1">Enter Baggage Tag Number</label>
            <div className="relative">
              <Search className="h-4 w-4 text-slate-400 absolute left-3.5 top-3.5" />
              <input
                type="text"
                value={tagNumber}
                onChange={(e) => setTagNumber(e.target.value.toUpperCase())}
                placeholder="e.g. BAG-K7P4M2-01"
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
            {isLoading ? 'LOCATING LUGGAGE...' : 'TRACK BAGGAGE'}
          </button>
        </form>

        {error && (
          <div className="mt-4 bg-rose-50 border border-rose-200 p-3 rounded-xl flex items-center space-x-2 text-xs font-semibold text-rose-700">
            <AlertCircle className="h-4 w-4 shrink-0" />
            <span>{error}</span>
          </div>
        )}
      </div>

      {baggageData && (
        <div className="bg-white rounded-3xl border border-slate-200 shadow-xl p-8 space-y-6 max-w-xl mx-auto">
          <div className="flex items-center justify-between border-b border-slate-100 pb-4">
            <div>
              <span className="text-[10px] uppercase font-bold text-slate-400 block">Baggage Tag</span>
              <span className="text-xl font-black font-mono text-sky-600">{baggageData.tagNumber}</span>
            </div>
            <div className="text-right">
              <span className="text-[10px] uppercase font-bold text-slate-400 block">Location</span>
              <span className="text-sm font-extrabold text-slate-900">{baggageData.currentCarousel}</span>
            </div>
          </div>

          <div className="space-y-4">
            <h3 className="text-xs font-extrabold text-slate-900 uppercase tracking-wider">Scan Event Telemetry</h3>
            <div className="space-y-3">
              {baggageData.scanHistory.map((scan: any, idx: number) => (
                <div key={idx} className="flex items-start space-x-3 text-xs bg-slate-50 p-3 rounded-xl border border-slate-100">
                  <CheckCircle2 className="h-4 w-4 text-emerald-600 mt-0.5 shrink-0" />
                  <div>
                    <span className="font-bold text-slate-900 block">{scan.status}</span>
                    <span className="text-slate-500 block">{scan.location}</span>
                    <span className="text-[10px] text-slate-400 font-mono block">{new Date(scan.timestamp).toLocaleTimeString()}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
