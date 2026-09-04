package com.airline.entity;

import com.airline.entity.enums.BookingStatus;
import com.airline.entity.enums.CabinClassType;
import com.airline.entity.enums.TripType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String pnr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "return_flight_id")
    private Flight returnFlight;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_type", nullable = false, length = 20)
    @Builder.Default
    private TripType tripType = TripType.ONE_WAY;

    @Enumerated(EnumType.STRING)
    @Column(name = "cabin_class", nullable = false, length = 50)
    private CabinClassType cabinClass;

    @Column(name = "passenger_count", nullable = false)
    @Builder.Default
    private Integer passengerCount = 1;

    @Column(name = "base_fare", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseFare;

    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "seat_fee", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal seatFee = BigDecimal.ZERO;

    @Column(name = "baggage_fee", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal baggageFee = BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private BookingStatus status = BookingStatus.INITIATED;

    @Column(name = "booking_date", insertable = false, updatable = false)
    private ZonedDateTime bookingDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BookingPassenger> bookingPassengers = new ArrayList<>();

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BookingSeat> reservedSeats = new ArrayList<>();
}
