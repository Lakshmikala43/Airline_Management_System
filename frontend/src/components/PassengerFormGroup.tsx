import React from 'react';
import { PassengerInput, Seat, MealPreference, BaggageOption } from '../types';
import { User, Utensils, Luggage } from 'lucide-react';

interface PassengerFormGroupProps {
  index: number;
  passenger: PassengerInput;
  onChange: (index: number, updated: PassengerInput) => void;
  availableSeats: Seat[];
}

export const PassengerFormGroup: React.FC<PassengerFormGroupProps> = ({
  index,
  passenger,
  onChange,
  availableSeats,
}) => {
  const updateField = (field: keyof PassengerInput, value: any) => {
    onChange(index, { ...passenger, [field]: value });
  };

  return (
    <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm space-y-5">
      <div className="flex items-center justify-between border-b border-slate-100 pb-3">
        <h3 className="text-sm font-extrabold text-slate-900 flex items-center space-x-2">
          <User className="h-4 w-4 text-sky-600" />
          <span>Passenger {index + 1} Details</span>
        </h3>
        <span className="text-xs font-bold text-sky-600 bg-sky-50 px-2.5 py-1 rounded-full border border-sky-200">
          Passenger {index + 1}
        </span>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-4 gap-4">
        {/* Title */}
        <div>
          <label className="text-xs font-bold text-slate-700 block mb-1">Title</label>
          <select
            value={passenger.title || 'Ms'}
            onChange={(e) => updateField('title', e.target.value)}
            className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-lg p-2.5 focus:ring-2 focus:ring-sky-500 outline-none"
          >
            <option value="Ms">Ms</option>
            <option value="Mr">Mr</option>
            <option value="Mrs">Mrs</option>
            <option value="Dr">Dr</option>
          </select>
        </div>

        {/* First Name */}
        <div>
          <label className="text-xs font-bold text-slate-700 block mb-1">First Name *</label>
          <input
            type="text"
            value={passenger.firstName}
            onChange={(e) => updateField('firstName', e.target.value)}
            placeholder="Lakshmi"
            required
            className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-lg p-2.5 focus:ring-2 focus:ring-sky-500 outline-none"
          />
        </div>

        {/* Last Name */}
        <div>
          <label className="text-xs font-bold text-slate-700 block mb-1">Last Name *</label>
          <input
            type="text"
            value={passenger.lastName}
            onChange={(e) => updateField('lastName', e.target.value)}
            placeholder="Kala"
            required
            className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-lg p-2.5 focus:ring-2 focus:ring-sky-500 outline-none"
          />
        </div>

        {/* Age */}
        <div>
          <label className="text-xs font-bold text-slate-700 block mb-1">Age *</label>
          <input
            type="number"
            value={passenger.age || 21}
            onChange={(e) => updateField('age', parseInt(e.target.value, 10))}
            min={1}
            max={120}
            required
            className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-lg p-2.5 focus:ring-2 focus:ring-sky-500 outline-none"
          />
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        {/* Passport / ID Number */}
        <div>
          <label className="text-xs font-bold text-slate-700 block mb-1">Passport / Government ID *</label>
          <input
            type="text"
            value={passenger.passportNumber}
            onChange={(e) => updateField('passportNumber', e.target.value)}
            placeholder="A12345678"
            required
            className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-lg p-2.5 focus:ring-2 focus:ring-sky-500 outline-none"
          />
        </div>

        {/* Contact Phone */}
        <div>
          <label className="text-xs font-bold text-slate-700 block mb-1">Contact Phone Number *</label>
          <input
            type="tel"
            value={passenger.phoneNumber || '+91 9876543210'}
            onChange={(e) => updateField('phoneNumber', e.target.value)}
            placeholder="+91 9876543210"
            required
            className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-lg p-2.5 focus:ring-2 focus:ring-sky-500 outline-none"
          />
        </div>

        {/* Date of Birth */}
        <div>
          <label className="text-xs font-bold text-slate-700 block mb-1">Date of Birth *</label>
          <input
            type="date"
            value={passenger.dateOfBirth}
            max={new Date().toISOString().split('T')[0]}
            onChange={(e) => updateField('dateOfBirth', e.target.value)}
            required
            className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-lg p-2.5 focus:ring-2 focus:ring-sky-500 outline-none"
          />
        </div>
      </div>

      {/* Add-ons Section: Baggage & Meals */}
      <div className="pt-3 border-t border-slate-100 grid grid-cols-1 sm:grid-cols-2 gap-4">
        
        {/* Meal Selection */}
        <div className="bg-slate-50 p-3 rounded-xl border border-slate-200">
          <label className="text-xs font-bold text-slate-800 flex items-center space-x-1.5 mb-1.5">
            <Utensils className="h-4 w-4 text-amber-600" />
            <span>In-Flight Meal Add-on</span>
          </label>
          <select
            value={passenger.mealPreference || 'VEGETARIAN'}
            onChange={(e) => updateField('mealPreference', e.target.value as MealPreference)}
            className="w-full text-xs font-semibold bg-white border border-slate-200 rounded-lg p-2 focus:ring-2 focus:ring-amber-500 outline-none"
          >
            <option value="VEGETARIAN">🥗 Vegetarian Meal (Included / +$4.00)</option>
            <option value="NON_VEGETARIAN">🍗 Non-Vegetarian Meal (+ $6.00)</option>
            <option value="NONE">No Meal Required</option>
          </select>
        </div>

        {/* Baggage Selection */}
        <div className="bg-slate-50 p-3 rounded-xl border border-slate-200">
          <label className="text-xs font-bold text-slate-800 flex items-center space-x-1.5 mb-1.5">
            <Luggage className="h-4 w-4 text-sky-600" />
            <span>Baggage Allowance & Excess Baggage</span>
          </label>
          <select
            value={passenger.baggageOption || 'STANDARD_15KG'}
            onChange={(e) => updateField('baggageOption', e.target.value as BaggageOption)}
            className="w-full text-xs font-semibold bg-white border border-slate-200 rounded-lg p-2 focus:ring-2 focus:ring-sky-500 outline-none"
          >
            <option value="STANDARD_15KG">🧳 Standard 15 kg Included (Free)</option>
            <option value="EXTRA_10KG">🧳 Extra 10 kg Baggage (+ $15.00 / ₹1,200)</option>
            <option value="EXTRA_20KG">🧳 Extra 20 kg Baggage (+ $30.00 / ₹2,400)</option>
          </select>
        </div>

      </div>

    </div>
  );
};
