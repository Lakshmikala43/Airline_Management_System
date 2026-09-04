import React, { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { Flight, SeatMap, Seat, PassengerInput, CabinClassType, Booking } from '../types';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { AircraftSeatMap } from '../components/AircraftSeatMap';
import { PassengerFormGroup } from '../components/PassengerFormGroup';
import { FareSummaryCard } from '../components/FareSummaryCard';
import { MockPaymentForm } from '../components/MockPaymentForm';
import { TicketCard } from '../components/TicketCard';
import { Check, Loader2, ArrowLeft, ShieldCheck, AlertCircle } from 'lucide-react';

export const BookingFlowPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { user, isAuthenticated } = useAuth();

  const flightId = parseInt(searchParams.get('flightId') || '1', 10);
  const cabinClass = (searchParams.get('cabin') as CabinClassType) || 'ECONOMY';
  const passengerCount = parseInt(searchParams.get('pax') || '1', 10);

  const [step, setStep] = useState<1 | 2 | 3 | 4>(1);
  const [flight, setFlight] = useState<Flight | null>(null);
  const [seatMap, setSeatMap] = useState<SeatMap | null>(null);
  const [selectedSeats, setSelectedSeats] = useState<Seat[]>([]);
  const [passengers, setPassengers] = useState<PassengerInput[]>([]);
  const [couponCode, setCouponCode] = useState('WELCOME10');
  const [discountAmount, setDiscountAmount] = useState(50.0);

  const [isLoading, setIsLoading] = useState(true);
  const [isProcessingPayment, setIsProcessingPayment] = useState(false);
  const [createdBooking, setCreatedBooking] = useState<Booking | null>(null);
  const [error, setError] = useState('');

  // Initial setup & mandatory auth check
  useEffect(() => {
    if (!isAuthenticated) {
      alert('Please Sign In or Register to book your flight ticket.');
      navigate('/login');
      return;
    }

    const loadBookingData = async () => {
      setIsLoading(true);
      try {
        const flightRes = await api.get<Flight>(`/flights/${flightId}`);
        setFlight(flightRes.data);

        const seatRes = await api.get<SeatMap>(`/flights/${flightId}/seats`);
        setSeatMap(seatRes.data);

        // Initialize passenger forms: Passenger 1 takes the registered customer's name!
        const initialPassengers: PassengerInput[] = [];
        for (let i = 0; i < passengerCount; i++) {
          if (i === 0) {
            initialPassengers.push({
              title: 'Ms',
              firstName: user?.firstName || '',
              lastName: user?.lastName || '',
              age: 21,
              dateOfBirth: '2005-05-15',
              gender: 'Female',
              nationality: 'India',
              passportNumber: 'Z9876543',
              passportExpiry: '2032-10-20',
              phoneNumber: user?.phoneNumber || '+91 9876543210',
              mealPreference: 'VEGETARIAN',
              baggageOption: 'STANDARD_15KG',
            });
          } else {
            initialPassengers.push({
              title: 'Mr',
              firstName: '',
              lastName: '',
              age: 25,
              dateOfBirth: '1999-01-01',
              gender: 'Male',
              nationality: 'India',
              passportNumber: '',
              passportExpiry: '2032-01-01',
              phoneNumber: '',
              mealPreference: 'VEGETARIAN',
              baggageOption: 'STANDARD_15KG',
            });
          }
        }
        setPassengers(initialPassengers);
      } catch (err) {
        setError('Failed to initialize booking session.');
      } finally {
        setIsLoading(false);
      }
    };

    loadBookingData();
  }, [flightId, passengerCount, isAuthenticated, user]);

  const handleToggleSeat = (seat: Seat) => {
    if (selectedSeats.some((s) => s.id === seat.id)) {
      setSelectedSeats(selectedSeats.filter((s) => s.id !== seat.id));
    } else {
      if (selectedSeats.length >= passengerCount) {
        alert(`You can select a maximum of ${passengerCount} seat(s) for your passengers.`);
        return;
      }
      setSelectedSeats([...selectedSeats, seat]);
    }
  };

  const handlePassengerChange = (index: number, updated: PassengerInput) => {
    const updatedList = [...passengers];
    updatedList[index] = updated;
    setPassengers(updatedList);
  };

  const handleApplyDiscount = (amount: number, code: string) => {
    setDiscountAmount(amount);
    setCouponCode(code);
  };

  const handleCreateBookingAndPay = async (paymentDetails: any) => {
    setIsProcessingPayment(true);
    setError('');
    try {
      // Step A: Associate selected seat IDs to passengers
      const updatedPassengers = passengers.map((p, idx) => ({
        ...p,
        selectedSeatId: selectedSeats[idx] ? selectedSeats[idx].id : undefined,
      }));

      // Step B: Call Booking API
      const bookingRes = await api.post<Booking>('/bookings', {
        flightId: flight!.id,
        cabinClass,
        passengers: updatedPassengers,
        couponCode: couponCode || undefined,
      });

      const booking = bookingRes.data;

      // Step C: Process Mock Payment
      await api.post('/payments/process', {
        bookingId: booking.id,
        paymentMethod: paymentDetails.paymentMethod,
        cardHolderName: paymentDetails.cardHolderName,
        cardNumber: paymentDetails.cardNumber,
      });

      // Fetch confirmed booking details
      const confirmedRes = await api.get<Booking>(`/bookings/pnr/${booking.pnr}`);
      setCreatedBooking(confirmedRes.data);
      setStep(4);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Payment simulation failed. Please try again.');
    } finally {
      setIsProcessingPayment(false);
    }
  };

  if (isLoading || !flight || !seatMap) {
    return (
      <div className="max-w-md mx-auto py-24 text-center space-y-3">
        <Loader2 className="h-8 w-8 text-indigo-800 animate-spin mx-auto" />
        <p className="text-xs font-bold text-slate-600">Initializing SkyNova Reservation Engine...</p>
      </div>
    );
  }

  const selectedSeatsSurcharge = selectedSeats.reduce((sum, s) => sum + s.seatSurcharge, 0);

  let extraBaggageFee = 0;
  let extraMealFee = 0;
  passengers.forEach((p) => {
    if (p.baggageOption === 'EXTRA_10KG') extraBaggageFee += 15.0;
    if (p.baggageOption === 'EXTRA_20KG') extraBaggageFee += 30.0;
    if (p.mealPreference === 'VEGETARIAN') extraMealFee += 4.0;
    if (p.mealPreference === 'NON_VEGETARIAN') extraMealFee += 6.0;
  });

  const totalCalculated = Math.max(0, flight.calculatedFare * passengerCount + selectedSeatsSurcharge + extraBaggageFee + extraMealFee - discountAmount);

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      
      {/* Checkout Stepper */}
      <div className="flex items-center justify-between max-w-2xl mx-auto">
        <div className={`flex items-center space-x-2 text-xs font-extrabold ${step >= 1 ? 'text-indigo-900' : 'text-slate-400'}`}>
          <span className={`w-7 h-7 rounded-full flex items-center justify-center ${step >= 1 ? 'bg-indigo-950 text-amber-300' : 'bg-slate-200'}`}>1</span>
          <span>Seat Map</span>
        </div>
        <div className="h-0.5 w-12 bg-slate-200"></div>
        <div className={`flex items-center space-x-2 text-xs font-extrabold ${step >= 2 ? 'text-indigo-900' : 'text-slate-400'}`}>
          <span className={`w-7 h-7 rounded-full flex items-center justify-center ${step >= 2 ? 'bg-indigo-950 text-amber-300' : 'bg-slate-200'}`}>2</span>
          <span>Passengers & Add-ons</span>
        </div>
        <div className="h-0.5 w-12 bg-slate-200"></div>
        <div className={`flex items-center space-x-2 text-xs font-extrabold ${step >= 3 ? 'text-indigo-900' : 'text-slate-400'}`}>
          <span className={`w-7 h-7 rounded-full flex items-center justify-center ${step >= 3 ? 'bg-indigo-950 text-amber-300' : 'bg-slate-200'}`}>3</span>
          <span>Payment</span>
        </div>
        <div className="h-0.5 w-12 bg-slate-200"></div>
        <div className={`flex items-center space-x-2 text-xs font-extrabold ${step === 4 ? 'text-emerald-700' : 'text-slate-400'}`}>
          <span className={`w-7 h-7 rounded-full flex items-center justify-center ${step === 4 ? 'bg-emerald-600 text-white' : 'bg-slate-200'}`}>4</span>
          <span>Ticket</span>
        </div>
      </div>

      {error && (
        <div className="bg-rose-50 border border-rose-200 p-4 rounded-xl flex items-center space-x-2 text-xs font-bold text-rose-700 max-w-2xl mx-auto">
          <AlertCircle className="h-4 w-4 shrink-0" />
          <span>{error}</span>
        </div>
      )}

      {/* STEP 1: Seat Selection */}
      {step === 1 && (
        <div className="space-y-6">
          <div className="text-center space-y-1">
            <h2 className="text-2xl font-black text-slate-900">Select Passenger Seats</h2>
            <p className="text-xs text-slate-500 font-medium">
              Interactive layout for Flight {flight.flightNumber} ({flight.originAirportCode} → {flight.destinationAirportCode})
            </p>
          </div>

          <AircraftSeatMap
            seatMap={seatMap}
            selectedSeatIds={selectedSeats.map((s) => s.id)}
            onToggleSeat={handleToggleSeat}
            maxSelectable={passengerCount}
          />

          <div className="flex items-center justify-between max-w-2xl mx-auto pt-4">
            <span className="text-xs font-bold text-slate-600">
              Selected: {selectedSeats.map((s) => s.seatNumber).join(', ') || 'None (Default 3A)'}
            </span>
            <button
              onClick={() => setStep(2)}
              className="bg-indigo-950 hover:bg-indigo-900 text-amber-300 font-extrabold text-xs px-6 py-3 rounded-xl shadow-md transition-all"
            >
              CONTINUE TO PASSENGER DETAILS
            </button>
          </div>
        </div>
      )}

      {/* STEP 2: Passenger Details & Add-ons */}
      {step === 2 && (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2 space-y-6">
            <h2 className="text-2xl font-black text-slate-900">Passenger Information & Add-on Services</h2>
            {passengers.map((p, idx) => (
              <PassengerFormGroup
                key={idx}
                index={idx}
                passenger={p}
                onChange={handlePassengerChange}
                availableSeats={selectedSeats}
              />
            ))}
            <div className="flex justify-between pt-4">
              <button
                type="button"
                onClick={() => setStep(1)}
                className="bg-slate-200 hover:bg-slate-300 text-slate-700 font-extrabold text-xs px-5 py-3 rounded-xl transition-colors"
              >
                Back to Seats
              </button>
              <button
                type="button"
                onClick={() => setStep(3)}
                className="bg-indigo-950 hover:bg-indigo-900 text-amber-300 font-extrabold text-xs px-6 py-3 rounded-xl shadow-md transition-all"
              >
                PROCEED TO PAYMENT REVIEW
              </button>
            </div>
          </div>

          <div>
            <FareSummaryCard
              flight={flight}
              cabinClass={cabinClass}
              passengerCount={passengerCount}
              selectedSeatsSurcharge={selectedSeatsSurcharge}
              passengers={passengers}
              onApplyDiscount={handleApplyDiscount}
              appliedDiscount={discountAmount}
            />
          </div>
        </div>
      )}

      {/* STEP 3: Review & Payment */}
      {step === 3 && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          <div>
            <FareSummaryCard
              flight={flight}
              cabinClass={cabinClass}
              passengerCount={passengerCount}
              selectedSeatsSurcharge={selectedSeatsSurcharge}
              passengers={passengers}
              onApplyDiscount={handleApplyDiscount}
              appliedDiscount={discountAmount}
            />
          </div>

          <div>
            <MockPaymentForm
              amount={totalCalculated}
              onPay={handleCreateBookingAndPay}
              isProcessing={isProcessingPayment}
            />
          </div>
        </div>
      )}

      {/* STEP 4: Ticket & Boarding Pass Confirmation */}
      {step === 4 && createdBooking && (
        <div className="space-y-6">
          <div className="text-center space-y-2">
            <div className="w-14 h-14 bg-emerald-100 text-emerald-600 rounded-full flex items-center justify-center mx-auto shadow-lg">
              <Check className="h-8 w-8 stroke-[3]" />
            </div>
            <h2 className="text-3xl font-black text-slate-900">Booking & Ticket Confirmed!</h2>
            <p className="text-xs text-slate-500 font-medium">Your electronic ticket & boarding pass has been generated. PNR: <span className="font-mono font-extrabold text-indigo-900">{createdBooking.pnr}</span></p>
          </div>

          <TicketCard booking={createdBooking} />
        </div>
      )}

    </div>
  );
};
