import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FlightSearchQuery, CabinClassType } from '../types';
import { PlaneTakeoff, PlaneLanding, Calendar, Users, Award, Search } from 'lucide-react';

const AIRPORTS = [
  { iata: 'HYD', city: 'Hyderabad', name: 'Rajiv Gandhi Intl (HYD)' },
  { iata: 'VTZ', city: 'Visakhapatnam', name: 'Visakhapatnam Intl (VTZ)' },
  { iata: 'VGA', city: 'Vijayawada', name: 'Gannavaram / Vijayawada (VGA)' },
  { iata: 'BLR', city: 'Bengaluru', name: 'Kempegowda Intl / Bangalore (BLR)' },
  { iata: 'JFK', city: 'New York', name: 'John F. Kennedy Intl (JFK)' },
  { iata: 'LHR', city: 'London', name: 'London Heathrow (LHR)' },
  { iata: 'CDG', city: 'Paris', name: 'Charles de Gaulle (CDG)' },
  { iata: 'DXB', city: 'Dubai', name: 'Dubai International (DXB)' },
  { iata: 'SIN', city: 'Singapore', name: 'Singapore Changi (SIN)' },
  { iata: 'HND', city: 'Tokyo', name: 'Tokyo Haneda (HND)' },
  { iata: 'DEL', city: 'New Delhi', name: 'Indira Gandhi Intl (DEL)' },
  { iata: 'SFO', city: 'San Francisco', name: 'San Francisco Intl (SFO)' },
  { iata: 'FRA', city: 'Frankfurt', name: 'Frankfurt Airport (FRA)' },
];

export const FlightSearchWidget: React.FC = () => {
  const navigate = useNavigate();

  const [tripType, setTripType] = useState<'ONE_WAY' | 'ROUND_TRIP'>('ONE_WAY');
  const [origin, setOrigin] = useState('HYD');
  const [destination, setDestination] = useState('VTZ');
  const [departureDate, setDepartureDate] = useState(() => new Date().toISOString().split('T')[0]);
  const [returnDate, setReturnDate] = useState('');
  const [passengerCount, setPassengerCount] = useState(1);
  const [cabinClass, setCabinClass] = useState<CabinClassType>('ECONOMY');

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    const query: FlightSearchQuery = {
      originAirportIata: origin,
      destinationAirportIata: destination,
      departureDate,
      returnDate: tripType === 'ROUND_TRIP' ? returnDate : undefined,
      tripType,
      passengerCount,
      cabinClass,
    };

    const params = new URLSearchParams({
      from: query.originAirportIata,
      to: query.destinationAirportIata,
      date: query.departureDate,
      type: query.tripType,
      pax: query.passengerCount.toString(),
      cabin: query.cabinClass,
    });

    navigate(`/search?${params.toString()}`);
  };

  return (
    <div className="bg-white rounded-3xl shadow-2xl p-6 sm:p-8 border border-slate-200/80 max-w-5xl mx-auto">
      
      {/* Header controls */}
      <div className="flex flex-wrap items-center justify-between gap-4 mb-6 pb-4 border-b border-slate-100">
        
        {/* Trip Type */}
        <div className="flex bg-slate-100 p-1 rounded-xl">
          <button
            type="button"
            onClick={() => setTripType('ONE_WAY')}
            className={`px-4 py-2 text-xs font-bold rounded-lg transition-all ${
              tripType === 'ONE_WAY'
                ? 'bg-orange-500 text-white shadow-sm font-extrabold'
                : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            One Way
          </button>
          <button
            type="button"
            onClick={() => setTripType('ROUND_TRIP')}
            className={`px-4 py-2 text-xs font-bold rounded-lg transition-all ${
              tripType === 'ROUND_TRIP'
                ? 'bg-orange-500 text-white shadow-sm font-extrabold'
                : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Round Trip
          </button>
        </div>

        {/* Quick Route Selector Shortcut */}
        <div className="flex items-center space-x-2">
          <button
            type="button"
            onClick={() => { setOrigin('HYD'); setDestination('VTZ'); }}
            className="text-[11px] font-extrabold bg-orange-50 hover:bg-orange-100 text-orange-700 border border-orange-200 px-2.5 py-1 rounded-lg transition-colors"
          >
            HYD ➔ VTZ
          </button>
          <button
            type="button"
            onClick={() => { setOrigin('VGA'); setDestination('BLR'); }}
            className="text-[11px] font-extrabold bg-amber-50 hover:bg-amber-100 text-amber-900 border border-amber-300 px-2.5 py-1 rounded-lg transition-colors"
          >
            VGA ➔ BLR
          </button>
        </div>

        {/* Cabin Class */}
        <div className="flex items-center space-x-2">
          <Award className="h-4 w-4 text-orange-600" />
          <select
            value={cabinClass}
            onChange={(e) => setCabinClass(e.target.value as CabinClassType)}
            className="text-xs font-semibold bg-slate-50 border border-slate-200 rounded-lg px-3 py-2 text-slate-700 focus:ring-2 focus:ring-orange-500 outline-none"
          >
            <option value="ECONOMY">Economy Class</option>
            <option value="PREMIUM_ECONOMY">Premium Economy</option>
            <option value="BUSINESS">Business Class</option>
            <option value="FIRST_CLASS">First Class</option>
          </select>
        </div>
      </div>

      <form onSubmit={handleSearch} className="space-y-6">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          
          {/* Origin */}
          <div className="bg-slate-50 p-3 rounded-xl border border-slate-200 hover:border-orange-400 transition-colors">
            <label className="text-[10px] font-bold uppercase tracking-wider text-slate-400 block mb-1 flex items-center gap-1">
              <PlaneTakeoff className="h-3.5 w-3.5 text-orange-600" /> From Airport
            </label>
            <select
              value={origin}
              onChange={(e) => setOrigin(e.target.value)}
              className="w-full bg-transparent text-sm font-bold text-slate-900 focus:outline-none cursor-pointer"
            >
              {AIRPORTS.map((a) => (
                <option key={a.iata} value={a.iata}>
                  {a.city} ({a.iata})
                </option>
              ))}
            </select>
          </div>

          {/* Destination */}
          <div className="bg-slate-50 p-3 rounded-xl border border-slate-200 hover:border-orange-400 transition-colors">
            <label className="text-[10px] font-bold uppercase tracking-wider text-slate-400 block mb-1 flex items-center gap-1">
              <PlaneLanding className="h-3.5 w-3.5 text-orange-600" /> To Airport
            </label>
            <select
              value={destination}
              onChange={(e) => setDestination(e.target.value)}
              className="w-full bg-transparent text-sm font-bold text-slate-900 focus:outline-none cursor-pointer"
            >
              {AIRPORTS.map((a) => (
                <option key={a.iata} value={a.iata}>
                  {a.city} ({a.iata})
                </option>
              ))}
            </select>
          </div>

          {/* Dates */}
          <div className="bg-slate-50 p-3 rounded-xl border border-slate-200 hover:border-orange-400 transition-colors">
            <label className="text-[10px] font-bold uppercase tracking-wider text-slate-400 block mb-1 flex items-center gap-1">
              <Calendar className="h-3.5 w-3.5 text-orange-600" /> Departure Date
            </label>
            <input
              type="date"
              value={departureDate}
              min={new Date().toISOString().split('T')[0]}
              onChange={(e) => setDepartureDate(e.target.value)}
              className="w-full bg-transparent text-xs font-bold text-slate-900 focus:outline-none"
              required
            />
          </div>

          {/* Passengers */}
          <div className="bg-slate-50 p-3 rounded-xl border border-slate-200 hover:border-orange-400 transition-colors flex items-center justify-between">
            <div>
              <label className="text-[10px] font-bold uppercase tracking-wider text-slate-400 block flex items-center gap-1">
                <Users className="h-3.5 w-3.5 text-orange-600" /> Passengers
              </label>
              <span className="text-sm font-extrabold text-slate-900">{passengerCount} Passenger(s)</span>
            </div>
            <div className="flex items-center space-x-1">
              <button
                type="button"
                onClick={() => setPassengerCount(Math.max(1, passengerCount - 1))}
                className="w-6 h-6 rounded-full bg-slate-200 text-slate-700 text-xs font-bold hover:bg-slate-300"
              >
                -
              </button>
              <button
                type="button"
                onClick={() => setPassengerCount(Math.min(9, passengerCount + 1))}
                className="w-6 h-6 rounded-full bg-slate-200 text-slate-700 text-xs font-bold hover:bg-slate-300"
              >
                +
              </button>
            </div>
          </div>

        </div>

        {/* Submit */}
        <button
          type="submit"
          className="w-full bg-orange-500 hover:bg-orange-600 text-white font-black text-sm py-4 rounded-2xl shadow-lg shadow-orange-500/25 flex items-center justify-center space-x-2 transition-all hover:scale-[1.01]"
        >
          <Search className="h-5 w-5" />
          <span>SEARCH SKYNOVA FLIGHTS</span>
        </button>

      </form>

    </div>
  );
};
