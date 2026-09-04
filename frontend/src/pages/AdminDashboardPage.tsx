import React, { useState, useEffect } from 'react';
import { AnalyticsDashboard, Flight } from '../types';
import { api } from '../services/api';
import {
  AreaChart, Area, BarChart, Bar, PieChart, Pie, Cell, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer
} from 'recharts';
import {
  LayoutDashboard, DollarSign, Plane, Users, AlertTriangle, Download, Plus, CheckCircle2, X,
  Building2, DoorClosed, AlertCircle, Luggage, UserCheck, ShieldCheck, Wrench
} from 'lucide-react';

export const AdminDashboardPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'analytics' | 'airports' | 'fleet' | 'flights' | 'crew' | 'baggage'>('analytics');
  const [analytics, setAnalytics] = useState<AnalyticsDashboard | null>(null);
  const [flights, setFlights] = useState<Flight[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  // Add Flight Modal State
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [flightNumber, setFlightNumber] = useState('AI-101');
  const [originCode, setOriginCode] = useState('VGA');
  const [originCity, setOriginCity] = useState('Vijayawada');
  const [destCode, setDestCode] = useState('HYD');
  const [destCity, setDestCity] = useState('Hyderabad');
  const [aircraftModel, setAircraftModel] = useState('Airbus A320');
  const [basePrice, setBasePrice] = useState('120');
  const [assignedGate, setAssignedGate] = useState('Gate 5');

  // Conflict Warning Banner State
  const [gateConflictError, setGateConflictError] = useState<string | null>(null);

  // Runway maintenance states
  const [runway1Status, setRunway1Status] = useState<'AVAILABLE' | 'UNDER_MAINTENANCE'>('AVAILABLE');
  const [runway2Status, setRunway2Status] = useState<'AVAILABLE' | 'UNDER_MAINTENANCE'>('UNDER_MAINTENANCE');

  const fetchData = async () => {
    setIsLoading(true);
    try {
      const [analyticsRes, flightsRes] = await Promise.all([
        api.get<AnalyticsDashboard>('/reports/dashboard'),
        api.get<Flight[]>('/flights')
      ]);
      setAnalytics(analyticsRes.data);
      setFlights(flightsRes.data);
    } catch (err) {
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleExportCsv = () => {
    try {
      const csvRows = [
        ['Flight Number', 'Airline', 'Route', 'Base Fare', 'Aircraft', 'Gate', 'Status', 'Passengers'],
        ...flights.map((f) => [
          f.flightNumber,
          f.airlineName,
          `${f.originAirportCode}-${f.destinationAirportCode}`,
          `$${f.basePrice}`,
          f.aircraftModel,
          f.gateNumber || 'Gate 5',
          f.status,
          `${180 - f.availableSeats}/180`
        ]),
        ['', '', '', '', '', '', '', ''],
        ['SYSTEM TELEMETRY SUMMARY', '', '', '', '', '', '', ''],
        ['Total Revenue', `$${analytics?.totalRevenue || 124500}`, '', '', '', '', '', ''],
        ['Total Bookings', `${analytics?.totalBookings || 142}`, '', '', '', '', '', ''],
        ['Total Passengers Processed', `${analytics?.totalPassengers || 8450}`, '', '', '', '', '', '']
      ];

      const csvString = csvRows.map((r) => r.map((cell) => `"${cell}"`).join(',')).join('\n');
      const blob = new Blob([csvString], { type: 'text/csv;charset=utf-8;' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.setAttribute('href', url);
      link.setAttribute('download', `skynova_airport_operations_report_${new Date().toISOString().split('T')[0]}.csv`);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (err) {
      alert('Failed to generate CSV export file');
    }
  };

  const handleStatusChange = async (flightId: number, status: string) => {
    try {
      await api.patch(`/flights/${flightId}/status`, { status });
      fetchData();
    } catch (err) {
      alert('Failed to update status');
    }
  };

  const handleAssignGateWithConflictCheck = (flightId: number, gate: string) => {
    setGateConflictError(null);

    // Business Rule Check: Is another active flight already assigned to this gate at the same time?
    const conflict = flights.find(
      (f) => f.id !== flightId && f.gateNumber === gate && (f.status === 'SCHEDULED' || f.status === 'BOARDING')
    );

    if (conflict) {
      setGateConflictError(
        `❌ GATE CONFLICT: Gate ${gate} is already assigned to active Flight ${conflict.flightNumber} (${conflict.originAirportCode} ➔ ${conflict.destinationAirportCode})!`
      );
      return;
    }

    const updated = flights.map((f) => (f.id === flightId ? { ...f, gateNumber: gate } : f));
    setFlights(updated);
    localStorage.setItem('skynova_mock_flights', JSON.stringify(updated));
    alert(`✅ Gate ${gate} successfully assigned to Flight ID ${flightId}!`);
  };

  const handleAddFlightSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setGateConflictError(null);

    // Check gate conflict during creation
    const conflict = flights.find(
      (f) => f.gateNumber === assignedGate && (f.status === 'SCHEDULED' || f.status === 'BOARDING')
    );

    if (conflict) {
      setGateConflictError(
        `❌ GATE CONFLICT: Gate ${assignedGate} is currently occupied by Flight ${conflict.flightNumber}! Please select a different gate.`
      );
      return;
    }

    const newFlight: Flight = {
      id: Date.now(),
      flightNumber,
      airlineName: 'Air India',
      airlineCode: 'AI',
      originAirportCode: originCode,
      originAirportName: `${originCity} Airport`,
      originCity,
      destinationAirportCode: destCode,
      destinationAirportName: `${destCity} Airport`,
      destinationCity: destCity,
      aircraftModel,
      departureTime: new Date(Date.now() + 86400000).toISOString(),
      arrivalTime: new Date(Date.now() + 86400000 + 4200000).toISOString(),
      durationMinutes: 70,
      basePrice: parseFloat(basePrice),
      calculatedFare: parseFloat(basePrice) * 1.12,
      taxAmount: parseFloat(basePrice) * 0.12,
      status: 'SCHEDULED',
      gateNumber: assignedGate,
      terminal: 'T1',
      availableSeats: 180,
      delayMinutes: 0,
      assignedCrew: {
        pilot: 'Captain Ravi',
        coPilot: 'First Officer Kumar',
        cabinCrew: ['Ananya', 'Priya', 'Rahul', 'Sneha'],
      },
    };

    try {
      const updated = [newFlight, ...flights];
      setFlights(updated);
      localStorage.setItem('skynova_mock_flights', JSON.stringify(updated));
      setIsAddModalOpen(false);
      alert(`Flight ${flightNumber} (${originCode} ➔ ${destCode}) created and assigned to Gate ${assignedGate}!`);
    } catch (err) {
      alert('Failed to add new flight schedule');
    }
  };

  if (isLoading || !analytics) {
    return <div className="text-center py-24 text-xs font-bold text-slate-500">Loading Airport & Airline Control Portal...</div>;
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      
      {/* Portal Header */}
      <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4 border-b border-slate-200 pb-6">
        <div>
          <h1 className="text-3xl font-black text-slate-900 flex items-center space-x-3">
            <Building2 className="h-8 w-8 text-indigo-900" />
            <span>Airline & Airport Administration Control Center</span>
          </h1>
          <p className="text-xs text-slate-500 mt-1 font-medium">Manage Airports, Airlines, Aircraft, Gates, Runways, Crew Rosters, Baggage Telemetry, and Flight Schedules</p>
        </div>

        <div className="flex space-x-3">
          <button
            onClick={() => setIsAddModalOpen(true)}
            className="bg-indigo-950 hover:bg-indigo-900 text-amber-300 font-black text-xs px-5 py-3 rounded-xl shadow-md flex items-center space-x-2 transition-all hover:scale-105"
          >
            <Plus className="h-4 w-4" />
            <span>CREATE FLIGHT</span>
          </button>
          <button
            onClick={handleExportCsv}
            className="bg-amber-500 hover:bg-amber-400 text-indigo-950 font-black text-xs px-5 py-3 rounded-xl shadow-md flex items-center space-x-2 transition-all hover:scale-105"
          >
            <Download className="h-4 w-4" />
            <span>EXPORT CSV REPORT</span>
          </button>
        </div>
      </div>

      {/* Conflict Error Banner */}
      {gateConflictError && (
        <div className="bg-rose-50 border border-rose-200 p-4 rounded-2xl flex items-center justify-between text-xs font-bold text-rose-800 shadow-md">
          <div className="flex items-center space-x-2">
            <AlertTriangle className="h-5 w-5 text-rose-600 shrink-0" />
            <span>{gateConflictError}</span>
          </div>
          <button onClick={() => setGateConflictError(null)} className="text-rose-700 font-bold underline">Dismiss</button>
        </div>
      )}

      {/* Navigation Tabs */}
      <div className="flex flex-wrap bg-slate-200/80 p-1.5 rounded-2xl gap-2 text-xs font-extrabold">
        <button
          onClick={() => setActiveTab('analytics')}
          className={`px-4 py-2.5 rounded-xl transition-all ${activeTab === 'analytics' ? 'bg-indigo-950 text-amber-300 shadow-md' : 'text-slate-700 hover:text-slate-900'}`}
        >
          📊 Operations Telemetry
        </button>
        <button
          onClick={() => setActiveTab('airports')}
          className={`px-4 py-2.5 rounded-xl transition-all ${activeTab === 'airports' ? 'bg-indigo-950 text-amber-300 shadow-md' : 'text-slate-700 hover:text-slate-900'}`}
        >
          🏢 Airports, Gates & Runways
        </button>
        <button
          onClick={() => setActiveTab('fleet')}
          className={`px-4 py-2.5 rounded-xl transition-all ${activeTab === 'fleet' ? 'bg-indigo-950 text-amber-300 shadow-md' : 'text-slate-700 hover:text-slate-900'}`}
        >
          ✈️ Airlines & Aircraft Fleet
        </button>
        <button
          onClick={() => setActiveTab('flights')}
          className={`px-4 py-2.5 rounded-xl transition-all ${activeTab === 'flights' ? 'bg-indigo-950 text-amber-300 shadow-md' : 'text-slate-700 hover:text-slate-900'}`}
        >
          🛫 Flight Operations & Gates
        </button>
        <button
          onClick={() => setActiveTab('crew')}
          className={`px-4 py-2.5 rounded-xl transition-all ${activeTab === 'crew' ? 'bg-indigo-950 text-amber-300 shadow-md' : 'text-slate-700 hover:text-slate-900'}`}
        >
          👨‍✈️ Crew Assignments
        </button>
        <button
          onClick={() => setActiveTab('baggage')}
          className={`px-4 py-2.5 rounded-xl transition-all ${activeTab === 'baggage' ? 'bg-indigo-950 text-amber-300 shadow-md' : 'text-slate-700 hover:text-slate-900'}`}
        >
          🧳 Baggage Control
        </button>
      </div>

      {/* TAB 1: Analytics & Reports */}
      {activeTab === 'analytics' && (
        <div className="space-y-8">
          {/* KPI Dashboard Cards */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-2">
              <span className="text-xs font-bold text-slate-400 uppercase tracking-wider block">Flights Today</span>
              <div className="flex items-baseline space-x-2">
                <span className="text-3xl font-black text-slate-900">{flights.length + 79}</span>
                <span className="text-xs font-bold text-emerald-700">85 Total</span>
              </div>
            </div>

            <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-2">
              <span className="text-xs font-bold text-slate-400 uppercase tracking-wider block">Passengers Processed</span>
              <div className="flex items-baseline space-x-2">
                <span className="text-3xl font-black text-indigo-900">8,450</span>
                <span className="text-xs font-bold text-slate-400">Pax Today</span>
              </div>
            </div>

            <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-2">
              <span className="text-xs font-bold text-slate-400 uppercase tracking-wider block">Delayed / Cancelled</span>
              <div className="flex items-baseline space-x-2">
                <span className="text-3xl font-black text-amber-600">4</span>
                <span className="text-xs font-bold text-rose-600">/ 1 Cancelled</span>
              </div>
            </div>

            <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-2">
              <span className="text-xs font-bold text-slate-400 uppercase tracking-wider block">Gate Occupancy</span>
              <div className="flex items-baseline space-x-2">
                <span className="text-3xl font-black text-indigo-950">18 / 30</span>
                <span className="text-xs font-bold text-slate-400">12 Available</span>
              </div>
            </div>
          </div>

          {/* Recharts Analytics Charts */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-lg space-y-4">
              <h3 className="text-sm font-black text-slate-900">7-Day Revenue Trend ($)</h3>
              <div className="h-64">
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={analytics.revenueTrend}>
                    <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
                    <XAxis dataKey="date" stroke="#94a3b8" fontSize={10} />
                    <YAxis stroke="#94a3b8" fontSize={10} />
                    <Tooltip />
                    <Area type="monotone" dataKey="value" stroke="#312e81" fill="#e0e7ff" strokeWidth={3} />
                  </AreaChart>
                </ResponsiveContainer>
              </div>
            </div>

            <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-lg space-y-4">
              <h3 className="text-sm font-black text-slate-900">Top Performing Flight Routes</h3>
              <div className="h-64">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={analytics.popularRoutes}>
                    <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
                    <XAxis dataKey="category" stroke="#94a3b8" fontSize={10} />
                    <YAxis stroke="#94a3b8" fontSize={10} />
                    <Tooltip />
                    <Bar dataKey="count" fill="#4f46e5" radius={[8, 8, 0, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* TAB 2: Airports, Gates & Runways */}
      {activeTab === 'airports' && (
        <div className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            
            {/* Gate Status Grid */}
            <div className="bg-white rounded-3xl border border-slate-200 p-6 shadow-lg space-y-4">
              <h3 className="text-base font-black text-slate-900 flex items-center space-x-2">
                <DoorClosed className="h-5 w-5 text-indigo-900" />
                <span>Airport Gates Real-Time Occupancy</span>
              </h3>
              <div className="grid grid-cols-3 sm:grid-cols-4 gap-3 text-center">
                {['Gate 1', 'Gate 2', 'Gate 3', 'Gate 4', 'Gate 5', 'Gate 6', 'Gate 7', 'Gate 8A'].map((g, idx) => {
                  const isOcc = idx % 2 === 0;
                  return (
                    <div key={g} className={`p-3 rounded-xl border ${isOcc ? 'bg-amber-50 border-amber-300 text-amber-900' : 'bg-indigo-50 border-indigo-300 text-indigo-900'}`}>
                      <span className="font-extrabold text-xs block">{g}</span>
                      <span className="text-[10px] font-bold block">{isOcc ? 'OCCUPIED' : 'AVAILABLE'}</span>
                    </div>
                  );
                })}
              </div>
            </div>

            {/* Runway Maintenance Status */}
            <div className="bg-white rounded-3xl border border-slate-200 p-6 shadow-lg space-y-4">
              <h3 className="text-base font-black text-slate-900 flex items-center space-x-2">
                <Wrench className="h-5 w-5 text-amber-600" />
                <span>Runway Operational Status & Maintenance</span>
              </h3>
              <div className="space-y-3 text-xs">
                <div className="flex items-center justify-between p-3 rounded-xl border border-slate-200 bg-slate-50">
                  <div>
                    <span className="font-extrabold text-slate-900 block">Runway 09L / 27R</span>
                    <span className="text-slate-500 block text-[11px]">Primary Take-off & Landing Strip</span>
                  </div>
                  <button
                    onClick={() => setRunway1Status(runway1Status === 'AVAILABLE' ? 'UNDER_MAINTENANCE' : 'AVAILABLE')}
                    className={`px-3 py-1.5 rounded-lg text-xs font-black ${
                      runway1Status === 'AVAILABLE' ? 'bg-indigo-900 text-amber-300' : 'bg-amber-600 text-white'
                    }`}
                  >
                    {runway1Status}
                  </button>
                </div>

                <div className="flex items-center justify-between p-3 rounded-xl border border-slate-200 bg-slate-50">
                  <div>
                    <span className="font-extrabold text-slate-900 block">Runway 14R / 32L</span>
                    <span className="text-slate-500 block text-[11px]">Secondary Runway Strip</span>
                  </div>
                  <button
                    onClick={() => setRunway2Status(runway2Status === 'AVAILABLE' ? 'UNDER_MAINTENANCE' : 'AVAILABLE')}
                    className={`px-3 py-1.5 rounded-lg text-xs font-black ${
                      runway2Status === 'AVAILABLE' ? 'bg-indigo-900 text-amber-300' : 'bg-amber-600 text-white'
                    }`}
                  >
                    {runway2Status}
                  </button>
                </div>
              </div>
            </div>

          </div>
        </div>
      )}

      {/* TAB 3: Airlines & Fleet */}
      {activeTab === 'fleet' && (
        <div className="bg-white rounded-3xl border border-slate-200 p-6 shadow-lg space-y-4">
          <h3 className="text-base font-black text-slate-900 flex items-center space-x-2">
            <Plane className="h-5 w-5 text-indigo-900" />
            <span>Registered Airlines & Aircraft Fleet</span>
          </h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 text-xs font-semibold">
            <div className="p-4 rounded-2xl border border-slate-200 bg-slate-50 space-y-1">
              <span className="text-indigo-900 font-extrabold text-sm block">Air India (AI)</span>
              <span className="text-slate-600 block">Fleet: 125 Aircraft</span>
              <span className="text-slate-400 block text-[10px]">Airbus A320, Boeing 787</span>
            </div>
            <div className="p-4 rounded-2xl border border-slate-200 bg-slate-50 space-y-1">
              <span className="text-indigo-900 font-extrabold text-sm block">IndiGo (6E)</span>
              <span className="text-slate-600 block">Fleet: 200 Aircraft</span>
              <span className="text-slate-400 block text-[10px]">Airbus A320neo, A321</span>
            </div>
            <div className="p-4 rounded-2xl border border-slate-200 bg-slate-50 space-y-1">
              <span className="text-indigo-900 font-extrabold text-sm block">Emirates (EK)</span>
              <span className="text-slate-600 block">Fleet: 260 Aircraft</span>
              <span className="text-slate-400 block text-[10px]">Airbus A380, Boeing 777</span>
            </div>
            <div className="p-4 rounded-2xl border border-slate-200 bg-slate-50 space-y-1">
              <span className="text-indigo-900 font-extrabold text-sm block">SkyNova Royal Airways (SN)</span>
              <span className="text-slate-600 block">Fleet: 45 Aircraft</span>
              <span className="text-slate-400 block text-[10px]">Boeing 787-9, A350</span>
            </div>
          </div>
        </div>
      )}

      {/* TAB 4: Flight Operations & Gate Assignments */}
      {activeTab === 'flights' && (
        <div className="bg-white rounded-3xl border border-slate-200 p-6 shadow-lg space-y-6">
          <h3 className="text-base font-black text-slate-900 flex items-center space-x-2">
            <Plane className="h-5 w-5 text-indigo-900" />
            <span>Flight Operations & Gate Assignments</span>
          </h3>

          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-50 text-slate-500 uppercase font-bold border-b border-slate-200">
                <tr>
                  <th className="p-3">Flight #</th>
                  <th className="p-3">Route</th>
                  <th className="p-3">Assigned Gate</th>
                  <th className="p-3">Status</th>
                  <th className="p-3 text-right">Gate & Status Controls</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 font-medium">
                {flights.map((f) => (
                  <tr key={f.id} className="hover:bg-slate-50/80 transition-colors">
                    <td className="p-3 font-extrabold text-indigo-900">{f.flightNumber}</td>
                    <td className="p-3 font-bold text-slate-900">{f.originAirportCode} ➔ {f.destinationAirportCode}</td>
                    <td className="p-3 font-mono font-bold text-indigo-700">{f.gateNumber || 'Gate 5'}</td>
                    <td className="p-3">
                      <span className={`px-2.5 py-1 rounded-full text-[10px] font-bold ${
                        f.status === 'SCHEDULED' ? 'bg-indigo-100 text-indigo-900' :
                        f.status === 'BOARDING' ? 'bg-amber-100 text-amber-800' :
                        f.status === 'CANCELLED' ? 'bg-rose-100 text-rose-800' :
                        'bg-slate-100 text-slate-700'
                      }`}>
                        {f.status}
                      </span>
                    </td>
                    <td className="p-3 text-right space-x-2">
                      <select
                        value={f.gateNumber || 'Gate 5'}
                        onChange={(e) => handleAssignGateWithConflictCheck(f.id, e.target.value)}
                        className="text-xs font-bold bg-slate-100 border border-slate-300 rounded-lg p-1.5 outline-none cursor-pointer"
                      >
                        <option value="Gate 1">Gate 1</option>
                        <option value="Gate 2">Gate 2</option>
                        <option value="Gate 3">Gate 3</option>
                        <option value="Gate 4">Gate 4</option>
                        <option value="Gate 5">Gate 5</option>
                        <option value="Gate 6">Gate 6</option>
                      </select>

                      <select
                        value={f.status}
                        onChange={(e) => handleStatusChange(f.id, e.target.value)}
                        className="text-xs font-bold bg-slate-100 border border-slate-300 rounded-lg p-1.5 outline-none cursor-pointer"
                      >
                        <option value="SCHEDULED">SCHEDULED</option>
                        <option value="BOARDING">BOARDING</option>
                        <option value="DEPARTED">DEPARTED</option>
                        <option value="IN_FLIGHT">IN_FLIGHT</option>
                        <option value="ARRIVED">ARRIVED</option>
                        <option value="DELAYED">DELAYED</option>
                        <option value="CANCELLED">CANCELLED</option>
                      </select>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* TAB 5: Crew Roster */}
      {activeTab === 'crew' && (
        <div className="bg-white rounded-3xl border border-slate-200 p-6 shadow-lg space-y-4">
          <h3 className="text-base font-black text-slate-900 flex items-center space-x-2">
            <UserCheck className="h-5 w-5 text-indigo-900" />
            <span>Flight Crew Roster & Pilot Assignments</span>
          </h3>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-xs">
            <div className="p-4 rounded-2xl border border-slate-200 bg-slate-50 space-y-2">
              <span className="font-extrabold text-slate-900 text-sm block">Flight AI101 (VGA ➔ HYD)</span>
              <div className="space-y-1 text-slate-600 font-medium">
                <p>👨‍✈️ <strong>Pilot / Captain:</strong> Captain Ravi</p>
                <p>🧑‍✈️ <strong>Co-Pilot:</strong> First Officer Kumar</p>
                <p>👩‍✈️ <strong>Cabin Crew:</strong> 4 Members Assigned</p>
              </div>
              <span className="inline-block bg-indigo-100 text-indigo-900 text-[10px] font-bold px-2 py-0.5 rounded">Qualifications Verified</span>
            </div>

            <div className="p-4 rounded-2xl border border-slate-200 bg-slate-50 space-y-2">
              <span className="font-extrabold text-slate-900 text-sm block">Flight SN-701 (HYD ➔ VTZ)</span>
              <div className="space-y-1 text-slate-600 font-medium">
                <p>👨‍✈️ <strong>Pilot / Captain:</strong> Captain Sharma</p>
                <p>🧑‍✈️ <strong>Co-Pilot:</strong> First Officer Vikram</p>
                <p>👩‍✈️ <strong>Cabin Crew:</strong> 4 Members Assigned</p>
              </div>
              <span className="inline-block bg-indigo-100 text-indigo-900 text-[10px] font-bold px-2 py-0.5 rounded">Qualifications Verified</span>
            </div>

            <div className="p-4 rounded-2xl border border-slate-200 bg-slate-50 space-y-2">
              <span className="font-extrabold text-slate-900 text-sm block">Flight SN-703 (VGA ➔ BLR)</span>
              <div className="space-y-1 text-slate-600 font-medium">
                <p>👨‍✈️ <strong>Pilot / Captain:</strong> Captain Deshmukh</p>
                <p>🧑‍✈️ <strong>Co-Pilot:</strong> First Officer Anita</p>
                <p>👩‍✈️ <strong>Cabin Crew:</strong> 4 Members Assigned</p>
              </div>
              <span className="inline-block bg-indigo-100 text-indigo-900 text-[10px] font-bold px-2 py-0.5 rounded">Qualifications Verified</span>
            </div>
          </div>
        </div>
      )}

      {/* TAB 6: Baggage Management */}
      {activeTab === 'baggage' && (
        <div className="bg-white rounded-3xl border border-slate-200 p-6 shadow-lg space-y-4">
          <h3 className="text-base font-black text-slate-900 flex items-center space-x-2">
            <Luggage className="h-5 w-5 text-indigo-900" />
            <span>Luggage Status Tracking & Claims</span>
          </h3>

          <div className="space-y-3 text-xs">
            <div className="flex items-center justify-between p-3 rounded-xl border border-slate-100 bg-slate-50">
              <div>
                <span className="font-mono font-extrabold text-indigo-900 block">Tag # BG-K7P4M2-01</span>
                <span className="text-slate-700 font-bold block">Passenger: Lakshmi Kala (Flight AI101)</span>
              </div>
              <div className="text-right">
                <span className="bg-indigo-100 text-indigo-900 font-bold px-2.5 py-1 rounded-full text-[10px] block">LOADED ON AIRCRAFT</span>
                <span className="text-[10px] font-bold text-slate-400 mt-1 block">Carousel 4B</span>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Add New Flight Modal */}
      {isAddModalOpen && (
        <div className="fixed inset-0 bg-slate-950/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl max-w-lg w-full p-6 space-y-6 relative shadow-2xl">
            <div className="flex items-center justify-between border-b border-slate-100 pb-3">
              <h3 className="text-lg font-black text-slate-900 flex items-center space-x-2">
                <Plane className="h-5 w-5 text-indigo-900" />
                <span>Create New Flight Schedule & Gate Assignment</span>
              </h3>
              <button onClick={() => setIsAddModalOpen(false)} className="text-slate-400 hover:text-slate-700">
                <X className="h-5 w-5" />
              </button>
            </div>

            <form onSubmit={handleAddFlightSubmit} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-xs font-bold text-slate-700 block mb-1">Flight Number</label>
                  <input
                    type="text"
                    value={flightNumber}
                    onChange={(e) => setFlightNumber(e.target.value)}
                    required
                    className="w-full text-xs font-mono font-bold bg-slate-50 border border-slate-200 rounded-xl p-2.5 outline-none"
                  />
                </div>
                <div>
                  <label className="text-xs font-bold text-slate-700 block mb-1">Base Fare ($ USD)</label>
                  <input
                    type="number"
                    value={basePrice}
                    onChange={(e) => setBasePrice(e.target.value)}
                    required
                    className="w-full text-xs font-bold bg-slate-50 border border-slate-200 rounded-xl p-2.5 outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-xs font-bold text-slate-700 block mb-1">Origin Code (IATA)</label>
                  <input
                    type="text"
                    value={originCode}
                    onChange={(e) => setOriginCode(e.target.value.toUpperCase())}
                    required
                    maxLength={3}
                    className="w-full text-xs font-bold bg-slate-50 border border-slate-200 rounded-xl p-2.5 outline-none uppercase"
                  />
                </div>
                <div>
                  <label className="text-xs font-bold text-slate-700 block mb-1">Origin City</label>
                  <input
                    type="text"
                    value={originCity}
                    onChange={(e) => setOriginCity(e.target.value)}
                    required
                    className="w-full text-xs font-bold bg-slate-50 border border-slate-200 rounded-xl p-2.5 outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-xs font-bold text-slate-700 block mb-1">Destination Code (IATA)</label>
                  <input
                    type="text"
                    value={destCode}
                    onChange={(e) => setDestCode(e.target.value.toUpperCase())}
                    required
                    maxLength={3}
                    className="w-full text-xs font-bold bg-slate-50 border border-slate-200 rounded-xl p-2.5 outline-none uppercase"
                  />
                </div>
                <div>
                  <label className="text-xs font-bold text-slate-700 block mb-1">Destination City</label>
                  <input
                    type="text"
                    value={destCity}
                    onChange={(e) => setDestCity(e.target.value)}
                    required
                    className="w-full text-xs font-bold bg-slate-50 border border-slate-200 rounded-xl p-2.5 outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-xs font-bold text-slate-700 block mb-1">Aircraft Model</label>
                  <select
                    value={aircraftModel}
                    onChange={(e) => setAircraftModel(e.target.value)}
                    className="w-full text-xs font-bold bg-slate-50 border border-slate-200 rounded-xl p-2.5 outline-none"
                  >
                    <option value="Airbus A320">Airbus A320</option>
                    <option value="Boeing 787-9 Dreamliner">Boeing 787-9 Dreamliner</option>
                    <option value="Airbus A350-900">Airbus A350-900</option>
                  </select>
                </div>

                <div>
                  <label className="text-xs font-bold text-slate-700 block mb-1">Assign Gate</label>
                  <select
                    value={assignedGate}
                    onChange={(e) => setAssignedGate(e.target.value)}
                    className="w-full text-xs font-bold bg-slate-50 border border-slate-200 rounded-xl p-2.5 outline-none"
                  >
                    <option value="Gate 1">Gate 1</option>
                    <option value="Gate 2">Gate 2</option>
                    <option value="Gate 3">Gate 3</option>
                    <option value="Gate 4">Gate 4</option>
                    <option value="Gate 5">Gate 5</option>
                    <option value="Gate 6">Gate 6</option>
                  </select>
                </div>
              </div>

              <button
                type="submit"
                className="w-full bg-indigo-950 hover:bg-indigo-900 text-amber-300 font-black text-xs py-3.5 rounded-xl shadow-lg transition-all"
              >
                CREATE FLIGHT & ASSIGN GATE
              </button>
            </form>
          </div>
        </div>
      )}

    </div>
  );
};
