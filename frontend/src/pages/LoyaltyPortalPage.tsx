import React, { useState, useEffect } from 'react';
import { api } from '../services/api';
import { Award, Shield, Gift, Sparkles, TrendingUp } from 'lucide-react';

export const LoyaltyPortalPage: React.FC = () => {
  const [loyalty, setLoyalty] = useState<any | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchLoyalty = async () => {
      try {
        const res = await api.get('/loyalty/my-account');
        setLoyalty(res.data);
      } catch (err) {
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };
    fetchLoyalty();
  }, []);

  if (isLoading) return <div className="text-center py-24 text-xs font-bold text-slate-500">Loading Loyalty Tier...</div>;

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12 space-y-8">
      <div className="bg-gradient-to-r from-amber-600 via-amber-700 to-indigo-900 text-white rounded-3xl p-8 shadow-2xl space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <Award className="h-10 w-10 text-amber-300" />
            <div>
              <h1 className="text-3xl font-black">{loyalty?.tier || 'GOLD'} SkyPass Member</h1>
              <p className="text-xs text-amber-200">{loyalty?.customerName} ({loyalty?.email})</p>
            </div>
          </div>
          <span className="bg-amber-400/20 border border-amber-300 text-amber-200 px-4 py-1.5 rounded-full text-xs font-black uppercase tracking-widest">
            {loyalty?.discountPercentage}% Member Discount
          </span>
        </div>

        <div className="grid grid-cols-3 gap-4 pt-4 border-t border-amber-500/30 text-center">
          <div>
            <span className="text-[10px] uppercase tracking-wider text-amber-200 block font-bold">Total Miles</span>
            <span className="text-2xl font-black">{loyalty?.totalMilesEarned?.toLocaleString() || '15,400'}</span>
          </div>
          <div>
            <span className="text-[10px] uppercase tracking-wider text-amber-200 block font-bold">Redeemable Points</span>
            <span className="text-2xl font-black text-amber-300">{loyalty?.redeemablePoints?.toLocaleString() || '3,850'}</span>
          </div>
          <div>
            <span className="text-[10px] uppercase tracking-wider text-amber-200 block font-bold">Tier Perk</span>
            <span className="text-sm font-extrabold text-emerald-300">Priority Boarding</span>
          </div>
        </div>
      </div>
    </div>
  );
};
