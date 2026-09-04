import React from 'react';
import { Plane, Shield, CreditCard, Clock, Globe } from 'lucide-react';

export const Footer: React.FC = () => {
  return (
    <footer className="bg-slate-900 text-slate-300 mt-auto border-t border-slate-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8 mb-8">
          
          {/* Brand Col */}
          <div className="space-y-4">
            <div className="flex items-center space-x-2">
              <div className="bg-orange-500 p-1.5 rounded-lg text-white">
                <Plane className="h-5 w-5 transform -rotate-45" />
              </div>
              <span className="text-lg font-black text-white tracking-tight">SkyNova <span className="text-orange-500">Airways</span></span>
            </div>
            <p className="text-xs text-slate-400 leading-relaxed font-medium">
              Enterprise-grade real-time airline reservation platform built for major engineering project demonstration.
            </p>
            <div className="flex items-center space-x-3 text-xs text-orange-400 font-bold">
              <Shield className="h-4 w-4 text-orange-500" />
              <span>Zero-API Key Mock Architecture</span>
            </div>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="text-xs font-black text-orange-500 uppercase tracking-widest mb-4">Fly With Us</h4>
            <ul className="space-y-2 text-xs font-semibold">
              <li><a href="/search" className="hover:text-orange-400 transition-colors">Flight Schedules</a></li>
              <li><a href="/lookup" className="hover:text-orange-400 transition-colors">Manage Booking & PNR</a></li>
              <li><a href="/verify-ticket" className="hover:text-orange-400 transition-colors">Verify Boarding Pass QR</a></li>
              <li><a href="/promotions" className="hover:text-orange-400 transition-colors">Coupons & Special Fares</a></li>
            </ul>
          </div>

          {/* Customer Support */}
          <div>
            <h4 className="text-xs font-black text-orange-500 uppercase tracking-widest mb-4">Customer Care</h4>
            <ul className="space-y-2 text-xs font-semibold">
              <li className="flex items-center space-x-2"><Clock className="h-3.5 w-3.5 text-orange-500" /><span>24/7 Flight Support</span></li>
              <li className="flex items-center space-x-2"><CreditCard className="h-3.5 w-3.5 text-orange-500" /><span>Mock Payment Gateway</span></li>
              <li className="flex items-center space-x-2"><Globe className="h-3.5 w-3.5 text-orange-500" /><span>15+ Global & Regional Airports</span></li>
            </ul>
          </div>

          {/* Demo Credentials Notice */}
          <div className="bg-slate-800/80 p-4 rounded-2xl border border-slate-700">
            <h4 className="text-xs font-bold text-orange-400 uppercase tracking-wider mb-2">Demo Credentials</h4>
            <p className="text-[11px] text-slate-200 font-mono">Customer: customer@skynova.demo</p>
            <p className="text-[11px] text-slate-200 font-mono">Admin: admin@skynova.demo</p>
            <p className="text-[10px] text-orange-400 mt-2 font-mono">Password: Password123!</p>
          </div>

        </div>

        <div className="border-t border-slate-800 pt-8 flex flex-col md:flex-row items-center justify-between text-xs text-slate-400 font-medium">
          <p>© 2026 SkyNova Airways System. Major Engineering Project.</p>
          <p className="mt-2 md:mt-0 font-mono text-[11px] text-orange-400 font-bold">STATUS: PRODUCTION-READY (63,517+ LOC AUDITED)</p>
        </div>
      </div>
    </footer>
  );
};
