import React, { useState } from 'react';
import { Flight, CabinClassType, PassengerInput } from '../types';
import { Tag, CheckCircle2, ShieldCheck, AlertCircle, Luggage, Utensils } from 'lucide-react';
import { api } from '../services/api';

interface FareSummaryCardProps {
  flight: Flight;
  cabinClass: CabinClassType;
  passengerCount: number;
  selectedSeatsSurcharge: number;
  passengers?: PassengerInput[];
  onApplyDiscount: (amount: number, code: string) => void;
  appliedDiscount: number;
}

export const FareSummaryCard: React.FC<FareSummaryCardProps> = ({
  flight,
  cabinClass,
  passengerCount,
  selectedSeatsSurcharge,
  passengers = [],
  onApplyDiscount,
  appliedDiscount,
}) => {
  const [couponCode, setCouponCode] = useState('WELCOME10');
  const [couponStatus, setCouponStatus] = useState<{ message: string; success: boolean } | null>(null);

  const basePrice = flight.basePrice;
  let multiplier = 1.0;
  if (cabinClass === 'PREMIUM_ECONOMY') multiplier = 1.35;
  if (cabinClass === 'BUSINESS') multiplier = 2.2;
  if (cabinClass === 'FIRST_CLASS') multiplier = 3.8;

  const perPaxPrice = basePrice * multiplier;
  const totalBaseFare = perPaxPrice * passengerCount;
  const taxAmount = totalBaseFare * 0.12;
  const seatFee = selectedSeatsSurcharge;

  // Calculate Baggage and Meal fees
  let baggageFee = 0;
  let mealFee = 0;

  passengers.forEach((p) => {
    if (p.baggageOption === 'EXTRA_10KG') baggageFee += 15.0;
    if (p.baggageOption === 'EXTRA_20KG') baggageFee += 30.0;

    if (p.mealPreference === 'VEGETARIAN') mealFee += 4.0;
    if (p.mealPreference === 'NON_VEGETARIAN') mealFee += 6.0;
  });

  const grandTotal = Math.max(0, totalBaseFare + taxAmount + seatFee + baggageFee + mealFee - appliedDiscount);

  const handleValidateCoupon = async () => {
    if (!couponCode.trim()) return;
    try {
      const res = await api.get('/promotions/validate', {
        params: { code: couponCode.trim(), amount: totalBaseFare },
      });
      if (res.data.isValid) {
        setCouponStatus({ message: res.data.message, success: true });
        onApplyDiscount(res.data.calculatedDiscountAmount, couponCode.trim());
      } else {
        setCouponStatus({ message: res.data.message, success: false });
      }
    } catch (err: any) {
      setCouponStatus({ message: 'Error validating coupon', success: false });
    }
  };

  return (
    <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-lg space-y-6">
      <h3 className="text-base font-black text-slate-900 border-b border-slate-100 pb-3 flex items-center justify-between">
        <span>Fare Summary</span>
        <span className="text-xs font-extrabold uppercase bg-sky-100 text-sky-800 px-2.5 py-1 rounded-md">
          {cabinClass}
        </span>
      </h3>

      <div className="space-y-3 text-xs">
        <div className="flex items-center justify-between text-slate-600">
          <span>Flight Fare (${perPaxPrice.toFixed(2)} × {passengerCount} pax)</span>
          <span className="font-bold text-slate-900">${totalBaseFare.toFixed(2)}</span>
        </div>

        <div className="flex items-center justify-between text-slate-600">
          <span>Airport & Government Taxes (12%)</span>
          <span className="font-bold text-slate-900">${taxAmount.toFixed(2)}</span>
        </div>

        {seatFee > 0 && (
          <div className="flex items-center justify-between text-slate-600">
            <span>Seat Selection Surcharges</span>
            <span className="font-bold text-emerald-600">+${seatFee.toFixed(2)}</span>
          </div>
        )}

        {baggageFee > 0 && (
          <div className="flex items-center justify-between text-slate-600">
            <span className="flex items-center gap-1"><Luggage className="h-3.5 w-3.5 text-sky-600" /> Extra Baggage Add-ons</span>
            <span className="font-bold text-sky-700">+${baggageFee.toFixed(2)}</span>
          </div>
        )}

        {mealFee > 0 && (
          <div className="flex items-center justify-between text-slate-600">
            <span className="flex items-center gap-1"><Utensils className="h-3.5 w-3.5 text-amber-600" /> In-Flight Meal Add-ons</span>
            <span className="font-bold text-amber-700">+${mealFee.toFixed(2)}</span>
          </div>
        )}

        {appliedDiscount > 0 && (
          <div className="flex items-center justify-between text-emerald-700 bg-emerald-50 p-2 rounded-lg border border-emerald-200 font-bold">
            <span className="flex items-center gap-1"><Tag className="h-3.5 w-3.5" /> Coupon Discount</span>
            <span>-${appliedDiscount.toFixed(2)}</span>
          </div>
        )}
      </div>

      {/* Coupon Input */}
      <div className="pt-3 border-t border-slate-100 space-y-2">
        <label className="text-[10px] uppercase font-bold text-slate-400 block">Apply Promotional Coupon Code</label>
        <div className="flex space-x-2">
          <input
            type="text"
            value={couponCode}
            onChange={(e) => setCouponCode(e.target.value.toUpperCase())}
            placeholder="WELCOME10"
            className="flex-1 text-xs font-mono font-bold bg-slate-50 border border-slate-200 rounded-lg px-3 py-2 uppercase outline-none focus:ring-2 focus:ring-sky-500"
          />
          <button
            type="button"
            onClick={handleValidateCoupon}
            className="bg-slate-900 hover:bg-slate-800 text-white font-bold text-xs px-3.5 py-2 rounded-lg transition-colors"
          >
            Apply
          </button>
        </div>
        {couponStatus && (
          <p className={`text-[11px] font-semibold flex items-center gap-1 ${couponStatus.success ? 'text-emerald-600' : 'text-rose-600'}`}>
            {couponStatus.success ? <CheckCircle2 className="h-3 w-3" /> : <AlertCircle className="h-3 w-3" />}
            {couponStatus.message}
          </p>
        )}
      </div>

      {/* Grand Total */}
      <div className="pt-4 border-t-2 border-slate-900 flex items-baseline justify-between">
        <div>
          <span className="text-xs uppercase font-extrabold tracking-wider text-slate-400 block">Total Amount</span>
          <span className="text-[10px] text-slate-400">Includes all taxes, seat, baggage & meals</span>
        </div>
        <div className="text-right">
          <span className="text-3xl font-black text-sky-700">${grandTotal.toFixed(2)}</span>
          <span className="text-xs font-bold text-slate-400 block">USD</span>
        </div>
      </div>

      <div className="bg-sky-50 p-3 rounded-xl border border-sky-100 flex items-center space-x-2 text-[11px] text-sky-800">
        <ShieldCheck className="h-4 w-4 text-sky-600 shrink-0" />
        <span>Transparent Pricing: Zero hidden fees or booking charges.</span>
      </div>

    </div>
  );
};
