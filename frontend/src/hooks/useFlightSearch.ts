import { useState, useCallback } from 'react';
import { Flight, FlightSearchQuery } from '../types';
import { api } from '../services/api';

export const useFlightSearch = () => {
  const [flights, setFlights] = useState<Flight[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const search = useCallback(async (query: FlightSearchQuery) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await api.post<Flight[]>('/flights/search', query);
      setFlights(response.data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Error executing flight search');
    } finally {
      setIsLoading(false);
    }
  }, []);

  return { flights, isLoading, error, search };
};
