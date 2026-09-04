import React from 'react';
import { FlightSearchWidget } from '../components/FlightSearchWidget';
import { Plane, ShieldCheck, Clock, Award, Globe, Headphones, CreditCard, Sparkles } from 'lucide-react';

export const LandingPage: React.FC = () => {
  return (
    <div className="space-y-16 pb-16">
      
      {/* Hero Section */}
      <div className="relative bg-gradient-to-r from-orange-600 via-orange-500 to-amber-500 text-white pt-12 pb-24 px-4 sm:px-6 lg:px-8 overflow-hidden shadow-lg">
        <div className="absolute inset-0 opacity-10 bg-[radial-gradient(#ffffff_1px,transparent_1px)] [background-size:16px_16px]"></div>
        
        <div className="max-w-7xl mx-auto text-center space-y-6 relative z-10">
          <div className="inline-flex items-center space-x-2 bg-white/10 backdrop-blur-md border border-white/20 px-4 py-1.5 rounded-full text-xs font-black text-white uppercase tracking-widest">
            <Sparkles className="h-3.5 w-3.5" />
            <span>Next-Gen Smart Airline Booking & Airport Management Engine</span>
          </div>

          <h1 className="text-4xl sm:text-6xl font-black tracking-tight leading-tight max-w-4xl mx-auto">
            Experience Energetic Modern Travel With{' '}
            <span className="text-slate-900 bg-white/90 px-3 py-1 rounded-2xl shadow-md">
              SkyNova Airways
            </span>
          </h1>

          <p className="text-sm sm:text-base text-orange-100 max-w-2xl mx-auto leading-relaxed font-semibold">
            Real-time seat selection, dynamic fare computation engine, instant PNR generation, and automated electronic boarding pass verification.
          </p>

          {/* Search Widget Component */}
          <div className="pt-6">
            <FlightSearchWidget />
          </div>
        </div>
      </div>

      {/* Feature Highlights Grid */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center space-y-2 mb-12">
          <h2 className="text-2xl font-black text-slate-900">Why Fly SkyNova Airways?</h2>
          <p className="text-xs text-slate-500 font-medium">Engineered for absolute reliability, concurrency security, and passenger comfort.</p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm hover:shadow-md transition-all space-y-3">
            <div className="w-10 h-10 rounded-2xl bg-orange-100 text-orange-600 flex items-center justify-center font-bold">
              <Clock className="h-5 w-5" />
            </div>
            <h3 className="text-sm font-bold text-slate-900">Real-Time Seat Locking</h3>
            <p className="text-xs text-slate-500 leading-relaxed font-medium">
              Database pessimistic locking guarantees zero double-bookings during seat selection.
            </p>
          </div>

          <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm hover:shadow-md transition-all space-y-3">
            <div className="w-10 h-10 rounded-2xl bg-orange-100 text-orange-600 flex items-center justify-center font-bold">
              <CreditCard className="h-5 w-5" />
            </div>
            <h3 className="text-sm font-bold text-slate-900">Mock Payment Engine</h3>
            <p className="text-xs text-slate-500 leading-relaxed font-medium">
              100% offline runnable payment simulation. Zero API keys or credit card storage required.
            </p>
          </div>

          <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm hover:shadow-md transition-all space-y-3">
            <div className="w-10 h-10 rounded-2xl bg-orange-100 text-orange-600 flex items-center justify-center font-bold">
              <Award className="h-5 w-5" />
            </div>
            <h3 className="text-sm font-bold text-slate-900">Dynamic Pricing Engine</h3>
            <p className="text-xs text-slate-500 leading-relaxed font-medium">
              Automated cabin class multipliers, seat fees, airport taxes, and promotional coupon discounts.
            </p>
          </div>

          <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm hover:shadow-md transition-all space-y-3">
            <div className="w-10 h-10 rounded-2xl bg-orange-100 text-orange-600 flex items-center justify-center font-bold">
              <ShieldCheck className="h-5 w-5" />
            </div>
            <h3 className="text-sm font-bold text-slate-900">QR Boarding Pass Verification</h3>
            <p className="text-xs text-slate-500 leading-relaxed font-medium">
              Generated electronic boarding passes include cryptographically safe QR codes for instant verification.
            </p>
          </div>
        </div>
      </div>

    </div>
  );
};
