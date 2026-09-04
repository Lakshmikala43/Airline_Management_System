import axios from 'axios';
import { INITIAL_MOCK_FLIGHTS, generateSeatMap } from './mockData';
import { Flight, Booking, SeatMap, TicketVerification, AnalyticsDashboard } from '../types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api/v1';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Local storage stores with automatic version synchronization
const getStoredBookings = (): Booking[] => {
  const data = localStorage.getItem('skynova_mock_bookings');
  return data ? JSON.parse(data) : [];
};

const saveStoredBookings = (bookings: Booking[]) => {
  localStorage.setItem('skynova_mock_bookings', JSON.stringify(bookings));
};

const getStoredFlights = (): Flight[] => {
  const data = localStorage.getItem('skynova_mock_flights');
  if (!data) {
    localStorage.setItem('skynova_mock_flights', JSON.stringify(INITIAL_MOCK_FLIGHTS));
    return INITIAL_MOCK_FLIGHTS;
  }
  try {
    const parsed: Flight[] = JSON.parse(data);
    // Ensure newly added regional flights (HYD, VTZ, VGA, BLR) are always merged in
    const missingMock = INITIAL_MOCK_FLIGHTS.filter(
      (mf) => !parsed.some((f) => f.flightNumber === mf.flightNumber)
    );
    if (missingMock.length > 0) {
      const merged = [...missingMock, ...parsed];
      localStorage.setItem('skynova_mock_flights', JSON.stringify(merged));
      return merged;
    }
    return parsed;
  } catch (e) {
    localStorage.setItem('skynova_mock_flights', JSON.stringify(INITIAL_MOCK_FLIGHTS));
    return INITIAL_MOCK_FLIGHTS;
  }
};

const saveStoredFlights = (flights: Flight[]) => {
  localStorage.setItem('skynova_mock_flights', JSON.stringify(flights));
};

// Request Interceptor for Auth
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('skynova_jwt_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response Interceptor with Intelligent Offline Client Fallback
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const { config } = error;
    if (!config) return Promise.reject(error);

    const url = config.url || '';
    const method = (config.method || 'get').toLowerCase();

    // 1. Flight Search Fallback
    if (url.includes('/flights/search') && method === 'post') {
      const body = JSON.parse(config.data || '{}');
      const origin = (body.originAirportIata || '').toUpperCase();
      const dest = (body.destinationAirportIata || '').toUpperCase();
      const flights = getStoredFlights();

      const filtered = flights.filter((f) => {
        if (origin && f.originAirportCode !== origin) return false;
        if (dest && f.destinationAirportCode !== dest) return false;
        return true;
      });

      return {
        data: filtered.length > 0 ? filtered : flights.filter(f => f.originAirportCode === origin || f.destinationAirportCode === dest),
        status: 200,
        statusText: 'OK',
        headers: {},
        config
      };
    }

    // 2. All Flights Listing
    if (url.endsWith('/flights') && method === 'get') {
      return { data: getStoredFlights(), status: 200, statusText: 'OK', headers: {}, config };
    }

    // 3. Single Flight By ID
    if (url.match(/\/flights\/\d+$/) && method === 'get') {
      const id = parseInt(url.split('/').pop() || '1', 10);
      const flight = getStoredFlights().find((f) => f.id === id) || INITIAL_MOCK_FLIGHTS[0];
      return { data: flight, status: 200, statusText: 'OK', headers: {}, config };
    }

    // 4. Seat Map Grid
    if (url.includes('/seats') && method === 'get') {
      const parts = url.split('/');
      const flightId = parseInt(parts[parts.indexOf('flights') + 1] || '1', 10);
      const flight = getStoredFlights().find((f) => f.id === flightId) || INITIAL_MOCK_FLIGHTS[0];
      const seatMap = generateSeatMap(flightId, flight.flightNumber, flight.aircraftModel);
      return { data: seatMap, status: 200, statusText: 'OK', headers: {}, config };
    }

    // 5. Create Booking Fallback
    if (url.endsWith('/bookings') && method === 'post') {
      const body = JSON.parse(config.data || '{}');
      const flights = getStoredFlights();
      const flight = flights.find((f) => f.id === body.flightId) || flights[0];
      const pnr = 'SN' + Math.random().toString(36).substring(2, 6).toUpperCase();

      const newBooking: Booking = {
        id: Date.now(),
        pnr,
        customerEmail: 'customer@skynova.demo',
        customerName: body.passengers?.[0]
          ? `${body.passengers[0].firstName} ${body.passengers[0].lastName}`
          : 'John Traveler',
        flight,
        tripType: body.tripType || 'ONE_WAY',
        cabinClass: body.cabinClass || 'ECONOMY',
        passengerCount: body.passengers?.length || 1,
        baseFare: flight.basePrice * (body.passengers?.length || 1),
        taxAmount: flight.basePrice * (body.passengers?.length || 1) * 0.12,
        seatFee: 25.0,
        baggageFee: 0.0,
        discountAmount: body.couponCode ? 50.0 : 0.0,
        totalAmount: flight.basePrice * (body.passengers?.length || 1) * 1.12 + 25 - (body.couponCode ? 50 : 0),
        status: 'PENDING_PAYMENT',
        bookingDate: new Date().toISOString(),
        passengers: (body.passengers || []).map((p: any, idx: number) => ({
          id: idx + 1,
          firstName: p.firstName || 'John',
          lastName: p.lastName || 'Traveler',
          dateOfBirth: p.dateOfBirth || '1995-05-15',
          gender: p.gender || 'Male',
          nationality: p.nationality || 'United States',
          passportNumber: p.passportNumber || 'A12345678',
          passportExpiry: p.passportExpiry || '2030-10-20',
          seatNumber: `12${String.fromCharCode(65 + idx)}`,
        })),
        paymentStatus: 'PENDING_PAYMENT',
        ticketNumber: `TK-${pnr}-01`,
      };

      const stored = getStoredBookings();
      stored.unshift(newBooking);
      saveStoredBookings(stored);

      return { data: newBooking, status: 200, statusText: 'OK', headers: {}, config };
    }

    // 6. Payment Authorization Fallback
    if (url.includes('/payments/process') && method === 'post') {
      const body = JSON.parse(config.data || '{}');
      const stored = getStoredBookings();
      const booking = stored.find((b) => b.id === body.bookingId) || stored[0];
      if (booking) {
        booking.status = 'CONFIRMED';
        booking.paymentStatus = 'SUCCESS';
        saveStoredBookings(stored);
      }
      return {
        data: {
          transactionReference: 'TXN-' + Math.random().toString(36).substring(2, 10).toUpperCase(),
          status: 'SUCCESS',
          message: 'Payment Simulated Successfully',
        },
        status: 200,
        statusText: 'OK',
        headers: {},
        config,
      };
    }

    // 7. Booking Lookup By PNR
    if (url.includes('/bookings/pnr/') && method === 'get') {
      const pnr = url.split('/').pop()?.toUpperCase();
      const stored = getStoredBookings();
      const booking = stored.find((b) => b.pnr === pnr) || {
        id: 999,
        pnr: pnr || 'K7P4M2',
        customerEmail: 'customer@skynova.demo',
        customerName: 'John Traveler',
        flight: INITIAL_MOCK_FLIGHTS[0],
        tripType: 'ONE_WAY',
        cabinClass: 'ECONOMY',
        passengerCount: 1,
        baseFare: 450,
        taxAmount: 54,
        seatFee: 25,
        baggageFee: 0,
        discountAmount: 0,
        totalAmount: 529,
        status: 'CONFIRMED',
        bookingDate: new Date().toISOString(),
        passengers: [
          {
            id: 1,
            firstName: 'John',
            lastName: 'Traveler',
            dateOfBirth: '1995-05-15',
            gender: 'Male',
            nationality: 'United States',
            passportNumber: 'A12345678',
            passportExpiry: '2030-10-20',
            seatNumber: '12A',
          },
        ],
        paymentStatus: 'CONFIRMED',
        ticketNumber: `TK-${pnr || 'K7P4M2'}-01`,
      };
      return { data: booking, status: 200, statusText: 'OK', headers: {}, config };
    }

    // 8. My Bookings Fallback
    if (url.includes('/bookings/my-bookings') && method === 'get') {
      const stored = getStoredBookings();
      if (stored.length === 0) {
        const demoBooking: Booking = {
          id: 1,
          pnr: 'K7P4M2',
          customerEmail: 'customer@skynova.demo',
          customerName: 'John Traveler',
          flight: INITIAL_MOCK_FLIGHTS[0],
          tripType: 'ONE_WAY',
          cabinClass: 'ECONOMY',
          passengerCount: 1,
          baseFare: 450,
          taxAmount: 54,
          seatFee: 25,
          baggageFee: 0,
          discountAmount: 0,
          totalAmount: 529,
          status: 'CONFIRMED',
          bookingDate: new Date().toISOString(),
          passengers: [
            {
              id: 1,
              firstName: 'John',
              lastName: 'Traveler',
              dateOfBirth: '1995-05-15',
              gender: 'Male',
              nationality: 'United States',
              passportNumber: 'A12345678',
              passportExpiry: '2030-10-20',
              seatNumber: '12A',
            },
          ],
          paymentStatus: 'CONFIRMED',
          ticketNumber: 'TK-K7P4M2-01',
        };
        saveStoredBookings([demoBooking]);
        return { data: [demoBooking], status: 200, statusText: 'OK', headers: {}, config };
      }
      return { data: stored, status: 200, statusText: 'OK', headers: {}, config };
    }

    // 9. Booking Cancel Fallback
    if (url.includes('/cancel') && method === 'post') {
      const parts = url.split('/');
      const pnr = parts[parts.indexOf('bookings') + 1];
      const stored = getStoredBookings();
      const booking = stored.find((b) => b.pnr === pnr);
      if (booking) {
        booking.status = 'CANCELLED';
        saveStoredBookings(stored);
      }
      return {
        data: {
          pnr,
          originalAmount: booking ? booking.totalAmount : 529.0,
          cancellationFee: 50.0,
          refundAmount: booking ? booking.totalAmount - 50.0 : 479.0,
          status: 'REFUNDED',
          refundReference: 'REF-' + Math.random().toString(36).substring(2, 10).toUpperCase(),
        },
        status: 200,
        statusText: 'OK',
        headers: {},
        config,
      };
    }

    // 10. Coupon Validation Fallback
    if (url.includes('/promotions/validate') && method === 'get') {
      const params = config.params || {};
      const code = (params.code || '').toUpperCase();
      if (code === 'SKYNOVA10' || code === 'WELCOME50') {
        const discount = code === 'WELCOME50' ? 50.0 : (params.amount || 500) * 0.1;
        return {
          data: {
            isValid: true,
            couponCode: code,
            calculatedDiscountAmount: discount,
            message: `Coupon ${code} Applied Successfully!`,
          },
          status: 200,
          statusText: 'OK',
          headers: {},
          config,
        };
      }
      return {
        data: {
          isValid: false,
          couponCode: code,
          calculatedDiscountAmount: 0,
          message: 'Invalid coupon code. Try SKYNOVA10 or WELCOME50',
        },
        status: 200,
        statusText: 'OK',
        headers: {},
        config,
      };
    }

    // 11. Ticket Verification Fallback
    if (url.includes('/tickets/verify/') && method === 'get') {
      const ticketNo = url.split('/').pop()?.toUpperCase();
      return {
        data: {
          isValid: true,
          ticketNumber: ticketNo,
          pnr: 'K7P4M2',
          passengerName: 'John Traveler',
          flightNumber: 'SN-101',
          originAirport: 'JFK',
          destinationAirport: 'LHR',
          departureTime: new Date(Date.now() + 86400000).toISOString(),
          status: 'ISSUED',
          verificationMessage: 'VERIFIED AUTHENTIC: SkyNova Airways Official Boarding Pass',
        },
        status: 200,
        statusText: 'OK',
        headers: {},
        config,
      };
    }

    // 12. Admin Analytics Dashboard Fallback
    if (url.includes('/reports/dashboard') && method === 'get') {
      const analytics: AnalyticsDashboard = {
        totalFlights: 12,
        activeFlights: 8,
        totalBookings: 142,
        confirmedBookings: 118,
        cancelledBookings: 14,
        totalRevenue: 124500.0,
        totalPassengers: 284,
        cancellationRatePercentage: 9.85,
        averageOccupancyPercentage: 84.5,
        bookingTrend: [
          { date: 'Mon', value: 18, count: 18 },
          { date: 'Tue', value: 24, count: 24 },
          { date: 'Wed', value: 31, count: 31 },
          { date: 'Thu', value: 28, count: 28 },
          { date: 'Fri', value: 42, count: 42 },
          { date: 'Sat', value: 38, count: 38 },
          { date: 'Sun', value: 45, count: 45 },
        ],
        revenueTrend: [
          { date: 'Mon', value: 12400, count: 12400 },
          { date: 'Tue', value: 16800, count: 16800 },
          { date: 'Wed', value: 21500, count: 21500 },
          { date: 'Thu', value: 19200, count: 19200 },
          { date: 'Fri', value: 28400, count: 28400 },
          { date: 'Sat', value: 24900, count: 24900 },
          { date: 'Sun', value: 31200, count: 31200 },
        ],
        popularRoutes: [
          { category: 'HYD -> VTZ', count: 184, value: 24800 },
          { category: 'VGA -> BLR', count: 165, value: 22275 },
          { category: 'JFK -> LHR', count: 142, value: 142000 },
          { category: 'SFO -> HND', count: 84, value: 112000 },
        ],
        cabinDistribution: [
          { category: 'Economy', count: 65, value: 65 },
          { category: 'Premium Economy', count: 18, value: 18 },
          { category: 'Business', count: 12, value: 12 },
          { category: 'First Class', count: 5, value: 5 },
        ],
      };
      return { data: analytics, status: 200, statusText: 'OK', headers: {}, config };
    }

    // 13. Admin Patch Flight Status Fallback
    if (url.includes('/status') && method === 'patch') {
      const parts = url.split('/');
      const id = parseInt(parts[parts.indexOf('flights') + 1], 10);
      const body = JSON.parse(config.data || '{}');
      const flights = getStoredFlights();
      const flight = flights.find((f) => f.id === id);
      if (flight) {
        flight.status = body.status;
        saveStoredFlights(flights);
      }
      return { data: flight || flights[0], status: 200, statusText: 'OK', headers: {}, config };
    }

    return Promise.reject(error);
  }
);
