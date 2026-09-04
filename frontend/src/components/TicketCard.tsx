import React from 'react';
import { Booking } from '../types';
import { Plane, QRCode, CheckCircle2, Download, Printer, ShieldCheck } from 'lucide-react';
import { format } from 'date-fns';

interface TicketCardProps {
  booking: Booking;
}

export const TicketCard: React.FC<TicketCardProps> = ({ booking }) => {
  const flight = booking.flight;
  const depDate = new Date(flight.departureTime);

  const handlePrint = () => {
    window.print();
  };

  return (
    <div className="max-w-3xl mx-auto space-y-6">
      
      {/* Boarding Pass Container */}
      <div className="bg-white rounded-3xl shadow-2xl border border-slate-200 overflow-hidden relative print:shadow-none print:border-none">
        
        {/* Header Ribbon */}
        <div className="bg-gradient-to-r from-sky-900 to-indigo-900 text-white p-6 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="bg-sky-600 p-2 rounded-xl text-white">
              <Plane className="h-6 w-6 transform -rotate-45" />
            </div>
            <div>
              <h2 className="text-xl font-black tracking-tight">SkyNova Airways</h2>
              <p className="text-xs text-sky-300 font-mono">OFFICIAL ELECTRONIC BOARDING PASS</p>
            </div>
          </div>
          <div className="text-right">
            <span className="text-xs uppercase text-slate-300 font-bold block">PNR Reference</span>
            <span className="text-2xl font-black font-mono tracking-widest text-sky-400">{booking.pnr}</span>
          </div>
        </div>

        {/* Flight Main Content */}
        <div className="p-6 sm:p-8 space-y-6">
          
          {/* Origin -> Destination Banner */}
          <div className="flex items-center justify-between border-b border-slate-100 pb-6">
            <div>
              <span className="text-3xl font-black text-slate-900 block">{flight.originAirportCode}</span>
              <span className="text-xs font-bold text-slate-500 block">{flight.originCity}</span>
            </div>

            <div className="flex flex-col items-center">
              <span className="text-xs font-mono font-bold text-sky-600">{flight.flightNumber}</span>
              <div className="flex items-center space-x-2 my-1">
                <div className="w-12 h-0.5 bg-slate-300"></div>
                <Plane className="h-5 w-5 text-sky-600 transform rotate-90" />
                <div className="w-12 h-0.5 bg-slate-300"></div>
              </div>
              <span className="text-[10px] uppercase font-bold text-slate-400">{booking.cabinClass}</span>
            </div>

            <div className="text-right">
              <span className="text-3xl font-black text-slate-900 block">{flight.destinationAirportCode}</span>
              <span className="text-xs font-bold text-slate-500 block">{flight.destinationCity}</span>
            </div>
          </div>

          {/* Passenger & Flight Specs Grid */}
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-6 text-xs bg-slate-50 p-4 rounded-2xl border border-slate-100">
            <div>
              <span className="text-[10px] uppercase font-bold text-slate-400 block">Passenger Name</span>
              <span className="font-extrabold text-slate-900 block text-sm">{booking.customerName}</span>
            </div>
            <div>
              <span className="text-[10px] uppercase font-bold text-slate-400 block">Departure Date & Time</span>
              <span className="font-bold text-slate-900 block">{format(depDate, 'MMM dd, yyyy')}</span>
              <span className="font-black text-sky-700">{format(depDate, 'HH:mm')}</span>
            </div>
            <div>
              <span className="text-[10px] uppercase font-bold text-slate-400 block">Gate / Terminal</span>
              <span className="font-extrabold text-slate-900 block">{flight.gateNumber || 'Gate 4B'} / {flight.terminal || 'T3'}</span>
            </div>
            <div>
              <span className="text-[10px] uppercase font-bold text-slate-400 block">Assigned Seat(s)</span>
              <span className="font-black text-emerald-700 text-sm block">
                {booking.passengers.map((p) => p.seatNumber).join(', ')}
              </span>
            </div>
          </div>

          {/* Ticket Verification & QR Code */}
          <div className="flex flex-col sm:flex-row items-center justify-between gap-6 pt-4 border-t border-dashed border-slate-200">
            <div className="space-y-2">
              <div className="flex items-center space-x-2 text-emerald-600 font-bold text-xs">
                <CheckCircle2 className="h-4 w-4" />
                <span>CONFIRMED & ISSUED</span>
              </div>
              <p className="text-xs font-mono text-slate-500">Ticket #: TK-{booking.pnr}-01</p>
              <div className="flex items-center space-x-1 text-[11px] text-slate-400">
                <ShieldCheck className="h-3.5 w-3.5 text-sky-600" />
                <span>Cryptographically Verified SkyNova QR Payload</span>
              </div>
            </div>

            {/* QR Code Placeholder Graphic */}
            <div className="flex flex-col items-center bg-slate-50 p-3 rounded-xl border border-slate-200">
              <div className="w-24 h-24 bg-slate-900 rounded-lg p-2 flex items-center justify-center text-white">
                <div className="grid grid-cols-5 gap-1 w-full h-full">
                  <div className="bg-white col-span-2 row-span-2"></div>
                  <div className="bg-white col-span-1"></div>
                  <div className="bg-white col-span-2 row-span-2"></div>
                  <div className="bg-white col-span-3"></div>
                  <div className="bg-white col-span-2"></div>
                </div>
              </div>
              <span className="text-[9px] font-mono font-bold text-slate-400 mt-1">SCAN FOR BOARDING</span>
            </div>
          </div>

        </div>

      </div>

      {/* Action Buttons */}
      <div className="flex items-center justify-center space-x-4 print:hidden">
        <button
          onClick={handlePrint}
          className="bg-slate-900 hover:bg-slate-800 text-white font-extrabold text-xs px-5 py-3 rounded-xl shadow-md flex items-center space-x-2 transition-all"
        >
          <Printer className="h-4 w-4" />
          <span>PRINT E-TICKET</span>
        </button>
      </div>

    </div>
  );
};
