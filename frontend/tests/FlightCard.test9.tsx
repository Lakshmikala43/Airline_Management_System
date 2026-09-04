import { describe, it, expect, vi } from 'vitest';
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { FlightCard } from '../src/components/FlightCard';
import { Flight } from '../src/types';

describe('FlightCard Component Unit Test Suite 9', () => {
  const mockFlight: Flight = {
    id: 9,
    flightNumber: 'SN-309',
    airlineName: 'SkyNova Airways',
    airlineCode: 'SN',
    originAirportCode: 'JFK',
    originAirportName: 'John F. Kennedy Intl',
    originCity: 'New York',
    destinationAirportCode: 'LHR',
    destinationAirportName: 'London Heathrow',
    destinationCity: 'London',
    aircraftModel: 'Boeing 787-9',
    departureTime: '2026-09-10T10:00:00Z',
    arrivalTime: '2026-09-10T18:00:00Z',
    durationMinutes: 480,
    basePrice: 500,
    calculatedFare: 560,
    taxAmount: 60,
    status: 'SCHEDULED',
    availableSeats: 45,
    delayMinutes: 0,
  };

  it('renders flight number and calculated fare correctly - Test 9', () => {
    const handleSelect = vi.fn();
    render(<FlightCard flight={mockFlight} onSelect={handleSelect} />);

    expect(screen.getByText('SN-309')).toBeDefined();
    expect(screen.getByText('$560')).toBeDefined();
  });

  it('triggers onSelect callback when Select Flight button is clicked - Test 9', () => {
    const handleSelect = vi.fn();
    render(<FlightCard flight={mockFlight} onSelect={handleSelect} />);

    const button = screen.getByText('SELECT FLIGHT');
    fireEvent.click(button);

    expect(handleSelect).toHaveBeenCalledWith(mockFlight);
  });
});
