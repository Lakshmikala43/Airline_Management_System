import React from 'react';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';
import { User, Mail, Phone, ShieldCheck, Ticket, Calendar, CheckCircle2, ArrowRight } from 'lucide-react';

export const CustomerDashboardPage: React.FC = () => {
  const { user } = useAuth();

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      
      {/* User Banner */}
      <div className="bg-gradient-to-r from-orange-600 via-orange-500 to-amber-500 text-white rounded-3xl p-8 shadow-xl flex flex-col md:flex-row items-center justify-between gap-6 border border-orange-400">
        <div className="space-y-1 text-center md:text-left">
          <span className="text-[10px] uppercase font-black tracking-widest text-orange-100 bg-white/10 px-3 py-1 rounded-full border border-white/20">
            Registered Passenger Profile
          </span>
          <h1 className="text-3xl font-black tracking-tight pt-1">Welcome Back, {user?.firstName} {user?.lastName}!</h1>
          <p className="text-xs text-orange-100 font-medium">Manage your personal customer profile and access instant flight booking lookups.</p>
        </div>

        <Link
          to="/lookup"
          className="bg-white text-orange-600 hover:bg-orange-50 font-black text-xs px-5 py-3 rounded-2xl shadow-lg flex items-center space-x-2 transition-all hover:scale-105 shrink-0"
        >
          <Ticket className="h-4 w-4 text-orange-500" />
          <span>Manage Bookings & Boarding Passes</span>
          <ArrowRight className="h-4 w-4" />
        </Link>
      </div>

      {/* User Profile Info Card */}
      <div className="bg-white rounded-3xl shadow-lg border border-slate-200 p-8 space-y-6">
        <div className="flex items-center justify-between border-b border-slate-100 pb-4">
          <h2 className="text-lg font-black text-slate-900 flex items-center space-x-2">
            <User className="h-5 w-5 text-orange-500" />
            <span>Passenger Account Information</span>
          </h2>
          <span className="inline-flex items-center space-x-1 px-3 py-1 rounded-full text-xs font-bold bg-emerald-50 text-emerald-700 border border-emerald-200">
            <CheckCircle2 className="h-3.5 w-3.5" />
            <span>Account Verified</span>
          </span>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 text-xs">
          <div className="bg-slate-50 p-4 rounded-2xl border border-slate-200 space-y-1">
            <span className="text-slate-400 font-bold uppercase text-[10px] tracking-wider block">Full Registered Name</span>
            <span className="text-sm font-black text-slate-900 block">{user?.firstName} {user?.lastName}</span>
          </div>

          <div className="bg-slate-50 p-4 rounded-2xl border border-slate-200 space-y-1">
            <span className="text-slate-400 font-bold uppercase text-[10px] tracking-wider block flex items-center gap-1">
              <Mail className="h-3.5 w-3.5 text-orange-500" /> Email Address
            </span>
            <span className="text-sm font-extrabold text-slate-900 block">{user?.email || 'customer@skynova.demo'}</span>
          </div>

          <div className="bg-slate-50 p-4 rounded-2xl border border-slate-200 space-y-1">
            <span className="text-slate-400 font-bold uppercase text-[10px] tracking-wider block flex items-center gap-1">
              <Phone className="h-3.5 w-3.5 text-orange-500" /> Registered Phone
            </span>
            <span className="text-sm font-extrabold text-slate-900 block">{user?.phoneNumber || '+91 9876543210'}</span>
          </div>

          <div className="bg-slate-50 p-4 rounded-2xl border border-slate-200 space-y-1">
            <span className="text-slate-400 font-bold uppercase text-[10px] tracking-wider block flex items-center gap-1">
              <ShieldCheck className="h-3.5 w-3.5 text-orange-500" /> Passport / Government ID
            </span>
            <span className="text-sm font-mono font-extrabold text-orange-600 block">{user?.passportNumber || 'Z9876543'}</span>
          </div>
        </div>
      </div>

    </div>
  );
};
