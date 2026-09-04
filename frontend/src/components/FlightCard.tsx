import React from 'react';
import { Flight } from '../types';
import { Plane, Clock, ArrowRight, CheckCircle2 } from 'lucide-react';
import { format } from 'date-fns';

interface FlightCardProps {
  flight: Flight;
  onSelect: (flight: Flight) => void;
}

export const FlightCard: React.FC<FlightCardProps> = ({ flight, onSelect }) => {
  const depDate = new Date(flight.departureTime);
  const arrDate = new Date(flight.arrivalTime);

  const hours = Math.floor(flight.durationMinutes / 60);
  const mins = flight.durationMinutes % 60;

  return (
    <div className="bg-white rounded-3xl shadow-md border border-slate-200/80 hover:border-orange-500 hover:shadow-xl transition-all duration-300 p-6 flex flex-col md:flex-row items-center justify-between gap-6">
      
      {/* Airline Info */}
      <div className="flex items-center space-x-4 min-w-[200px]">
        <div className="w-12 h-12 rounded-2xl bg-orange-500 text-white font-black text-sm shadow-md flex items-center justify-center">
          {flight.airlineCode}
        </div>
        <div>
          <h4 className="font-extrabold text-slate-900 text-sm">{flight.airlineName}</h4>
          <p className="text-xs font-mono text-slate-400">{flight.flightNumber} • {flight.aircraftModel}</p>
          <span className="inline-flex items-center px-2 py-0.5 rounded text-[10px] font-bold bg-emerald-50 text-emerald-700 border border-emerald-200 mt-1">
            <CheckCircle2 className="h-3 w-3 mr-1" /> On Time
          </span>
        </div>
      </div>

      {/* Flight Time Details */}
      <div className="flex-1 flex items-center justify-center space-x-6 sm:space-x-10 text-center">
        
        {/* Departure */}
        <div>
          <span className="text-2xl font-black text-slate-900 block">{format(depDate, 'HH:mm')}</span>
          <span className="text-xs font-bold text-slate-700 block">{flight.originAirportCode}</span>
          <span className="text-[11px] text-slate-400 block">{flight.originCity}</span>
        </div>

        {/* Duration Vector */}
        <div className="flex flex-col items-center min-w-[120px]">
          <span className="text-[10px] font-semibold text-slate-400 flex items-center gap-1 mb-1">
            <Clock className="h-3 w-3 text-orange-600" /> {hours}h {mins}m
          </span>
          <div className="w-full flex items-center">
            <div className="h-0.5 bg-slate-300 flex-1"></div>
            <Plane className="h-4 w-4 text-orange-600 transform rotate-90 mx-1" />
            <div className="h-0.5 bg-slate-300 flex-1"></div>
          </div>
          <span className="text-[10px] font-bold text-orange-600 uppercase mt-1">Non-stop</span>
        </div>

        {/* Arrival */}
        <div>
          <span className="text-2xl font-black text-slate-900 block">{format(arrDate, 'HH:mm')}</span>
          <span className="text-xs font-bold text-slate-700 block">{flight.destinationAirportCode}</span>
          <span className="text-[11px] text-slate-400 block">{flight.destinationCity}</span>
        </div>

      </div>

      {/* Pricing & Booking */}
      <div className="flex flex-col items-end justify-center min-w-[160px] border-t md:border-t-0 md:border-l border-slate-100 pt-4 md:pt-0 md:pl-6 w-full md:w-auto">
        <span className="text-[10px] uppercase font-bold tracking-wider text-slate-400 block">Starting From</span>
        <div className="flex items-baseline space-x-1">
          <span className="text-2xl font-black text-orange-600">${flight.calculatedFare}</span>
          <span className="text-xs text-slate-400 font-medium">USD</span>
        </div>
        <span className="text-[10px] text-slate-400 block mb-3">{flight.availableSeats} seats left</span>

        <button
          onClick={() => onSelect(flight)}
          className="w-full md:w-auto bg-orange-500 hover:bg-orange-600 text-white font-extrabold text-xs px-5 py-2.5 rounded-xl shadow-md shadow-orange-500/20 flex items-center justify-center space-x-1.5 transition-all hover:scale-105"
        >
          <span>SELECT FLIGHT</span>
          <ArrowRight className="h-3.5 w-3.5" />
        </button>
      </div>

    </div>
  );
};
