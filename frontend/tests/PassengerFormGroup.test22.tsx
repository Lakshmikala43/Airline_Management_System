import { describe, it, expect, vi } from 'vitest';
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { PassengerFormGroup } from '../src/components/PassengerFormGroup';
import { PassengerInput } from '../src/types';

describe('PassengerFormGroup Component Unit Test Suite 22', () => {
  const mockPassenger: PassengerInput = {
    title: 'Mr',
    firstName: 'John',
    lastName: 'Doe',
    dateOfBirth: '1995-05-15',
    gender: 'Male',
    nationality: 'United States',
    passportNumber: 'A12345678',
    passportExpiry: '2030-10-20',
  };

  it('renders passenger input fields correctly - Test 22', () => {
    const handleChange = vi.fn();
    render(
      <PassengerFormGroup
        index={0}
        passenger={mockPassenger}
        onChange={handleChange}
        availableSeats={[]}
      />
    );

    expect(screen.getByPlaceholderText('John')).toBeDefined();
    expect(screen.getByPlaceholderText('Doe')).toBeDefined();
  });
});
