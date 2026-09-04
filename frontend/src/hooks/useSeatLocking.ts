import { useState, useCallback } from 'react';
import { Seat } from '../types';

export const useSeatLocking = (maxSeats: number) => {
  const [selectedSeats, setSelectedSeats] = useState<Seat[]>([]);

  const toggleSeat = useCallback((seat: Seat) => {
    setSelectedSeats((prev) => {
      const exists = prev.some((s) => s.id === seat.id);
      if (exists) {
        return prev.filter((s) => s.id !== seat.id);
      }
      if (prev.length >= maxSeats) {
        return prev;
      }
      return [...prev, seat];
    });
  }, [maxSeats]);

  const clearSeats = useCallback(() => {
    setSelectedSeats([]);
  }, []);

  const totalSurcharge = selectedSeats.reduce((sum, s) => sum + s.seatSurcharge, 0);

  return { selectedSeats, toggleSeat, clearSeats, totalSurcharge };
};
