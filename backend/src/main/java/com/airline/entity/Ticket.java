package com.airline.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "tickets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"booking_id", "passenger_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_number", nullable = false, unique = true, length = 20)
    private String ticketNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @Column(nullable = false, length = 6)
    private String pnr;

    @Column(name = "issue_date", insertable = false, updatable = false)
    private ZonedDateTime issueDate;

    @Column(name = "qr_code_data", nullable = false, columnDefinition = "TEXT")
    private String qrCodeData;

    @Column(name = "pdf_file_path", length = 500)
    private String pdfFilePath;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "ISSUED";

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
