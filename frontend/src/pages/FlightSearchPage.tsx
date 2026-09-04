import React, { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { Flight, CabinClassType } from '../types';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { FlightCard } from '../components/FlightCard';
import { FlightSearchWidget } from '../components/FlightSearchWidget';
import { Filter, SlidersHorizontal, Loader2, ArrowUpDown } from 'lucide-react';

export const FlightSearchPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();

  const [flights, setFlights] = useState<Flight[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  // Filtering states
  const [sortBy, setSortBy] = useState<'PRICE' | 'DURATION' | 'DEPARTURE'>('PRICE');

  const originParam = searchParams.get('from') || 'HYD';
  const destParam = searchParams.get('to') || 'VTZ';
  const dateParam = searchParams.get('date') || new Date().toISOString().split('T')[0];
  const cabinParam = (searchParams.get('cabin') as CabinClassType) || 'ECONOMY';
  const paxParam = parseInt(searchParams.get('pax') || '1', 10);

  const fetchFlights = async () => {
    setIsLoading(true);
    setError('');
    try {
      const res = await api.post<Flight[]>('/flights/search', {
        originAirportIata: originParam,
        destinationAirportIata: destParam,
        departureDate: dateParam,
        passengerCount: paxParam,
        cabinClass: cabinParam,
      });

      if (res.data && res.data.length > 0) {
        setFlights(res.data);
      } else {
        const all = await api.get<Flight[]>('/flights');
        setFlights(all.data);
      }
    } catch (err) {
      try {
        const all = await api.get<Flight[]>('/flights');
        setFlights(all.data);
      } catch (e) {
        setError('Failed to load flight search results.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchFlights();
  }, [searchParams]);

  const handleSelectFlight = (flight: Flight) => {
    // MANDATORY AUTH CHECK: If not logged in, redirect to login page!
    if (!isAuthenticated) {
      alert('Please Sign In or Register to book your flight ticket.');
      navigate('/login');
      return;
    }

    const params = new URLSearchParams({
      flightId: flight.id.toString(),
      cabin: cabinParam,
      pax: paxParam.toString(),
    });
    navigate(`/booking?${params.toString()}`);
  };

  // Process Sorting
  const sortedFlights = [...flights].sort((a, b) => {
    if (sortBy === 'PRICE') return a.calculatedFare - b.calculatedFare;
    if (sortBy === 'DURATION') return a.durationMinutes - b.durationMinutes;
    if (sortBy === 'DEPARTURE') return new Date(a.departureTime).getTime() - new Date(b.departureTime).getTime();
    return 0;
  });

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      
      {/* Top Search Widget Accordion */}
      <div className="mb-6">
        <FlightSearchWidget />
      </div>

      {/* Results Section */}
      <div className="flex flex-col lg:flex-row gap-8">
        
        {/* Filter Sidebar */}
        <div className="w-full lg:w-64 space-y-6 shrink-0">
          <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-4">
            <h3 className="text-sm font-extrabold text-slate-900 flex items-center space-x-2 border-b border-slate-100 pb-3">
              <SlidersHorizontal className="h-4 w-4 text-indigo-800" />
              <span>Filter Results</span>
            </h3>

            {/* Sort Dropdown */}
            <div>
              <label className="text-xs font-bold text-slate-700 block mb-1">Sort By</label>
              <select
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value as any)}
                className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-lg p-2.5 outline-none"
              >
                <option value="PRICE">Cheapest Price</option>
                <option value="DURATION">Shortest Duration</option>
                <option value="DEPARTURE">Earliest Departure</option>
              </select>
            </div>
          </div>
        </div>

        {/* Flight Cards List */}
        <div className="flex-1 space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-black text-slate-900">
              Available Flights ({originParam} → {destParam})
            </h2>
            <span className="text-xs font-bold text-slate-500">{sortedFlights.length} Flights Found</span>
          </div>

          {isLoading ? (
            <div className="bg-white p-12 rounded-3xl text-center border border-slate-200 space-y-3">
              <Loader2 className="h-8 w-8 text-indigo-800 animate-spin mx-auto" />
              <p className="text-xs font-bold text-slate-600">Searching SkyNova Royal Airways Schedules...</p>
            </div>
          ) : sortedFlights.length === 0 ? (
            <div className="bg-white p-12 rounded-3xl text-center border border-slate-200 space-y-3">
              <p className="text-sm font-bold text-slate-700">No flights found matching your exact route.</p>
              <p className="text-xs text-slate-400">Try selecting origin HYD and destination VTZ for regional schedules.</p>
            </div>
          ) : (
            sortedFlights.map((flight) => (
              <FlightCard key={flight.id} flight={flight} onSelect={handleSelectFlight} />
            ))
          )}
        </div>

      </div>

    </div>
  );
};
