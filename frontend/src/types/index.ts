export type UserRole = 'ROLE_ADMIN' | 'ROLE_CUSTOMER';

export interface UserProfile {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  dateOfBirth?: string;
  gender?: string;
  nationality?: string;
  passportNumber?: string;
  passportExpiry?: string;
  roles: UserRole[];
  isActive: boolean;
}

export interface JwtResponse {
  token: string;
  type: string;
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  roles: UserRole[];
}

export interface Gate {
  id: number;
  gateNumber: string;
  terminal: string;
  airportCode: string;
  isOccupied: boolean;
  assignedFlightNumber?: string;
}

export interface Runway {
  id: number;
  runwayCode: string;
  airportCode: string;
  status: 'AVAILABLE' | 'UNDER_MAINTENANCE';
  maintenanceSchedule?: string;
}

export interface Airport {
  id: number;
  iataCode: string;
  icaoCode?: string;
  name: string;
  city: string;
  country: string;
  latitude?: number;
  longitude?: number;
  timeZone: string;
  terminalInfo?: string;
  isActive: boolean;
  gates?: Gate[];
  runways?: Runway[];
}

export interface Airline {
  id: number;
  code: string;
  name: string;
  country: string;
  logoUrl?: string;
}

export interface Aircraft {
  id: number;
  registrationNumber: string;
  airlineName: string;
  manufacturer: string;
  model: string;
  totalCapacity: number;
  economyCapacity: number;
  premiumEconomyCapacity: number;
  businessCapacity: number;
  firstClassCapacity: number;
  status: 'ACTIVE' | 'MAINTENANCE' | 'RETIRED';
  manufactureYear?: number;
}

export type CabinClassType = 'ECONOMY' | 'PREMIUM_ECONOMY' | 'BUSINESS' | 'FIRST_CLASS';
export type FlightStatus = 'SCHEDULED' | 'BOARDING' | 'DEPARTED' | 'IN_FLIGHT' | 'ARRIVED' | 'DELAYED' | 'CANCELLED' | 'DIVERTED';

export interface CrewMember {
  id: number;
  employeeId: string;
  firstName: string;
  lastName: string;
  role: 'CAPTAIN' | 'CO_PILOT' | 'PURSER' | 'CABIN_CREW';
  licenseNumber?: string;
  flightHours: number;
  isActive: boolean;
}

export interface Flight {
  id: number;
  flightNumber: string;
  airlineName: string;
  airlineCode: string;
  originAirportCode: string;
  originAirportName: string;
  originCity: string;
  destinationAirportCode: string;
  destinationAirportName: string;
  destinationCity: string;
  aircraftModel: string;
  departureTime: string;
  arrivalTime: string;
  durationMinutes: number;
  basePrice: number;
  calculatedFare: number;
  taxAmount: number;
  status: FlightStatus;
  gateNumber?: string;
  terminal?: string;
  runwayCode?: string;
  availableSeats: number;
  delayMinutes: number;
  assignedCrew?: {
    pilot?: string;
    coPilot?: string;
    cabinCrew?: string[];
  };
}

export interface FlightSearchQuery {
  originAirportIata: string;
  destinationAirportIata: string;
  departureDate: string;
  returnDate?: string;
  tripType: 'ONE_WAY' | 'ROUND_TRIP';
  passengerCount: number;
  cabinClass: CabinClassType;
}

export interface Seat {
  id: number;
  seatNumber: string;
  seatRow: number;
  seatColumn: string;
  cabinClass: CabinClassType;
  isWindow: boolean;
  isAisle: boolean;
  isExitRow: boolean;
  extraLegroom: boolean;
  isOccupied: boolean;
  isBlocked: boolean;
  seatSurcharge: number;
}

export interface SeatMap {
  flightId: number;
  flightNumber: string;
  aircraftModel: string;
  seats: Seat[];
}

export type MealPreference = 'VEGETARIAN' | 'NON_VEGETARIAN' | 'NONE';
export type BaggageOption = 'STANDARD_15KG' | 'EXTRA_10KG' | 'EXTRA_20KG';

export interface PassengerInput {
  title?: string;
  firstName: string;
  middleName?: string;
  lastName: string;
  age?: number;
  dateOfBirth: string;
  gender: string;
  nationality: string;
  passportNumber: string;
  passportExpiry: string;
  email?: string;
  phoneNumber?: string;
  selectedSeatId?: number;
  mealPreference?: MealPreference;
  baggageOption?: BaggageOption;
  specialRequest?: string;
}

export interface BaggageStatusInfo {
  tagNumber: string;
  weightKg: number;
  extraWeightKg: number;
  status: 'CHECKED_IN' | 'LOADED' | 'IN_TRANSIT' | 'ARRIVED' | 'COLLECTED';
  carouselNumber?: string;
}

export interface Booking {
  id: number;
  pnr: string;
  customerEmail: string;
  customerName: string;
  flight: Flight;
  returnFlight?: Flight;
  tripType: 'ONE_WAY' | 'ROUND_TRIP';
  cabinClass: CabinClassType;
  passengerCount: number;
  baseFare: number;
  taxAmount: number;
  seatFee: number;
  baggageFee: number;
  mealFee: number;
  discountAmount: number;
  totalAmount: number;
  status: 'INITIATED' | 'PENDING_PAYMENT' | 'PAYMENT_SUCCESS' | 'CONFIRMED' | 'CANCELLED' | 'REFUNDED';
  isCheckedIn: boolean;
  boardingTime?: string;
  bookingDate: string;
  passengers: Array<{
    id: number;
    title?: string;
    firstName: string;
    lastName: string;
    age?: number;
    dateOfBirth: string;
    gender: string;
    nationality: string;
    passportNumber: string;
    passportExpiry: string;
    email?: string;
    phoneNumber?: string;
    seatNumber: string;
    mealPreference?: MealPreference;
    baggageOption?: BaggageOption;
  }>;
  baggageInfo?: BaggageStatusInfo;
  paymentStatus: string;
  ticketNumber: string;
}

export interface TicketVerification {
  isValid: boolean;
  ticketNumber: string;
  pnr?: string;
  passengerName?: string;
  flightNumber?: string;
  originAirport?: string;
  destinationAirport?: string;
  departureTime?: string;
  gateNumber?: string;
  seatNumber?: string;
  status?: string;
  verificationMessage: string;
}

export interface AnalyticsDashboard {
  totalFlights: number;
  activeFlights: number;
  totalBookings: number;
  confirmedBookings: number;
  cancelledBookings: number;
  delayedFlightsCount: number;
  totalRevenue: number;
  totalPassengers: number;
  availableGatesCount: number;
  occupiedGatesCount: number;
  availableRunwaysCount: number;
  cancellationRatePercentage: number;
  averageOccupancyPercentage: number;
  bookingTrend: Array<{ date: string; value: number; count: number }>;
  revenueTrend: Array<{ date: string; value: number; count: number }>;
  popularRoutes: Array<{ category: string; count: number; value: number }>;
  cabinDistribution: Array<{ category: string; count: number; value: number }>;
}
