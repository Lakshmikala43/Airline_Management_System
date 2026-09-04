import React, { useState, useEffect } from 'react';
import { api } from '../services/api';
import { Users, UserCheck, Shield, Award } from 'lucide-react';

export const CrewRosterPage: React.FC = () => {
  const [crew, setCrew] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchCrew = async () => {
      try {
        const res = await api.get('/crew');
        setCrew(res.data);
      } catch (err) {
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };
    fetchCrew();
  }, []);

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-black text-slate-900 flex items-center space-x-3">
            <Users className="h-8 w-8 text-sky-600" />
            <span>Flight Crew & Pilot Roster</span>
          </h1>
          <p className="text-xs text-slate-500 mt-1">Active flight personnel, captain assignments, and flight hour tracking</p>
        </div>
      </div>

      <div className="bg-white rounded-3xl border border-slate-200 p-6 shadow-lg overflow-x-auto">
        <table className="w-full text-left text-xs">
          <thead className="bg-slate-50 text-slate-500 uppercase font-bold border-b border-slate-200">
            <tr>
              <th className="p-3">Employee ID</th>
              <th className="p-3">Name</th>
              <th className="p-3">Role</th>
              <th className="p-3">License #</th>
              <th className="p-3">Flight Hours</th>
              <th className="p-3">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 font-medium">
            {crew.map((c) => (
              <tr key={c.id} className="hover:bg-slate-50/80 transition-colors">
                <td className="p-3 font-mono font-bold text-sky-600">{c.employeeId}</td>
                <td className="p-3 font-extrabold text-slate-900">{c.fullName}</td>
                <td className="p-3"><span className="bg-slate-100 px-2.5 py-1 rounded-md text-[10px] font-bold">{c.role}</span></td>
                <td className="p-3 font-mono text-slate-500">{c.licenseNumber || 'N/A'}</td>
                <td className="p-3 font-bold">{c.flightHours} hrs</td>
                <td className="p-3"><span className="text-emerald-600 font-bold">Active</span></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
