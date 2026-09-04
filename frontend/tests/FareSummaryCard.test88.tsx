import { describe, it, expect, vi } from 'vitest';
import React from 'react';
import { render, screen } from '@testing-library/react';
import { FareSummaryCard } from '../src/components/FareSummaryCard';
import { Flight } from '../src/types';

describe('FareSummaryCard Component Unit Test Suite 88', () => {
  const mockFlight: Flight = {
    id: 88,
    flightNumber: 'SN-488',
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

  it('renders fare summary breakdown calculations correctly - Test 88', () => {
    const handleApplyDiscount = vi.fn();
    render(
      <FareSummaryCard
        flight={mockFlight}
        cabinClass="ECONOMY"
        passengerCount={1}
        selectedSeatsSurcharge={0}
        onApplyDiscount={handleApplyDiscount}
        appliedDiscount={0}
      />
    );

    expect(screen.getByText('Fare Summary')).toBeDefined();
    expect(screen.getByText('$560.00')).toBeDefined();
  });
});
