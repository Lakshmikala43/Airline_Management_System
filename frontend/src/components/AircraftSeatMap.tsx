import React from 'react';
import { SeatMap, Seat } from '../types';
import { Check, ShieldAlert, Sparkles, AlertCircle } from 'lucide-react';

interface AircraftSeatMapProps {
  seatMap: SeatMap;
  selectedSeatIds: number[];
  onToggleSeat: (seat: Seat) => void;
  maxSelectable: number;
}

export const AircraftSeatMap: React.FC<AircraftSeatMapProps> = ({
  seatMap,
  selectedSeatIds,
  onToggleSeat,
  maxSelectable,
}) => {
  // Group seats by row
  const rowsMap = new Map<number, Seat[]>();
  seatMap.seats.forEach((seat) => {
    if (!rowsMap.has(seat.seatRow)) {
      rowsMap.set(seat.seatRow, []);
    }
    rowsMap.get(seat.seatRow)!.push(seat);
  });

  const sortedRows = Array.from(rowsMap.keys()).sort((a, b) => a - b);

  return (
    <div className="bg-slate-900 text-white rounded-3xl p-6 sm:p-8 shadow-2xl border border-slate-800 max-w-2xl mx-auto">
      
      {/* Cockpit Front Header */}
      <div className="text-center mb-8">
        <div className="w-32 h-10 bg-slate-800 border border-slate-700 rounded-t-full mx-auto flex items-center justify-center text-[10px] uppercase font-bold tracking-widest text-slate-400">
          Cockpit Front
        </div>
        <p className="text-xs text-sky-400 mt-2 font-semibold">Select up to {maxSelectable} seat(s)</p>
      </div>

      {/* Legend */}
      <div className="flex flex-wrap items-center justify-center gap-4 text-xs mb-8 pb-6 border-b border-slate-800">
        <div className="flex items-center space-x-2">
          <div className="w-5 h-5 rounded bg-white border border-slate-400"></div>
          <span className="text-slate-300">Available</span>
        </div>
        <div className="flex items-center space-x-2">
          <div className="w-5 h-5 rounded bg-sky-600 border border-sky-400 flex items-center justify-center text-white">
            <Check className="h-3 w-3" />
          </div>
          <span className="text-slate-300">Selected</span>
        </div>
        <div className="flex items-center space-x-2">
          <div className="w-5 h-5 rounded bg-slate-700 border border-slate-600 opacity-60"></div>
          <span className="text-slate-400">Occupied</span>
        </div>
        <div className="flex items-center space-x-2">
          <div className="w-5 h-5 rounded border border-amber-500 bg-amber-950/60 text-amber-400"></div>
          <span className="text-amber-400 font-medium">First / Business</span>
        </div>
      </div>

      {/* Seat Map Grid Container */}
      <div className="space-y-3 max-h-[500px] overflow-y-auto pr-2 custom-scrollbar">
        {sortedRows.map((rowNum) => {
          const rowSeats = rowsMap.get(rowNum) || [];
          rowSeats.sort((a, b) => a.seatColumn.localeCompare(b.seatColumn));

          const isFirstOrBusiness = rowSeats.some((s) => s.cabinClass === 'FIRST_CLASS' || s.cabinClass === 'BUSINESS');

          // Split row into left aisle right
          const mid = Math.ceil(rowSeats.length / 2);
          const leftGroup = rowSeats.slice(0, mid);
          const rightGroup = rowSeats.slice(mid);

          return (
            <div
              key={rowNum}
              className={`flex items-center justify-between p-2 rounded-xl border ${
                isFirstOrBusiness ? 'bg-slate-800/80 border-amber-500/30' : 'bg-slate-800/40 border-slate-800'
              }`}
            >
              {/* Left Column Group */}
              <div className="flex items-center space-x-2">
                {leftGroup.map((seat) => {
                  const isSelected = selectedSeatIds.includes(seat.id);
                  const isOccupied = seat.isOccupied;

                  return (
                    <button
                      key={seat.id}
                      type="button"
                      disabled={isOccupied || seat.isBlocked}
                      onClick={() => onToggleSeat(seat)}
                      className={`w-9 h-10 rounded-lg font-bold text-xs flex flex-col items-center justify-center transition-all ${
                        isOccupied
                          ? 'bg-slate-700 text-slate-500 cursor-not-allowed opacity-50'
                          : isSelected
                          ? 'bg-sky-600 text-white shadow-lg ring-2 ring-sky-400 scale-105'
                          : seat.cabinClass === 'FIRST_CLASS' || seat.cabinClass === 'BUSINESS'
                          ? 'bg-amber-950/60 border border-amber-500/50 text-amber-300 hover:bg-amber-900/60'
                          : 'bg-slate-700/80 text-slate-200 hover:bg-slate-600 border border-slate-600'
                      }`}
                      title={`${seat.seatNumber} (${seat.cabinClass}) - ${seat.isOccupied ? 'Occupied' : 'Available'}`}
                    >
                      <span>{seat.seatNumber}</span>
                      {seat.seatSurcharge > 0 && !isOccupied && (
                        <span className="text-[8px] font-extrabold text-emerald-400">+${seat.seatSurcharge}</span>
                      )}
                    </button>
                  );
                })}
              </div>

              {/* Aisle Row Identifier */}
              <div className="w-8 text-center text-xs font-mono font-bold text-slate-500 uppercase">
                R{rowNum}
              </div>

              {/* Right Column Group */}
              <div className="flex items-center space-x-2">
                {rightGroup.map((seat) => {
                  const isSelected = selectedSeatIds.includes(seat.id);
                  const isOccupied = seat.isOccupied;

                  return (
                    <button
                      key={seat.id}
                      type="button"
                      disabled={isOccupied || seat.isBlocked}
                      onClick={() => onToggleSeat(seat)}
                      className={`w-9 h-10 rounded-lg font-bold text-xs flex flex-col items-center justify-center transition-all ${
                        isOccupied
                          ? 'bg-slate-700 text-slate-500 cursor-not-allowed opacity-50'
                          : isSelected
                          ? 'bg-sky-600 text-white shadow-lg ring-2 ring-sky-400 scale-105'
                          : seat.cabinClass === 'FIRST_CLASS' || seat.cabinClass === 'BUSINESS'
                          ? 'bg-amber-950/60 border border-amber-500/50 text-amber-300 hover:bg-amber-900/60'
                          : 'bg-slate-700/80 text-slate-200 hover:bg-slate-600 border border-slate-600'
                      }`}
                      title={`${seat.seatNumber} (${seat.cabinClass}) - ${seat.isOccupied ? 'Occupied' : 'Available'}`}
                    >
                      <span>{seat.seatNumber}</span>
                      {seat.seatSurcharge > 0 && !isOccupied && (
                        <span className="text-[8px] font-extrabold text-emerald-400">+${seat.seatSurcharge}</span>
                      )}
                    </button>
                  );
                })}
              </div>

            </div>
          );
        })}
      </div>

    </div>
  );
};
