-- V5__create_payments_refunds_promotions.sql
-- SkyNova Airways - Payments, Refunds & Dynamic Coupon System Schema

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    transaction_reference VARCHAR(50) NOT NULL UNIQUE,
    booking_id BIGINT NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    amount NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    payment_method VARCHAR(50) NOT NULL, -- CREDIT_CARD, DEBIT_CARD, MOCK_CARD, NET_BANKING, UPI
    provider_name VARCHAR(50) NOT NULL DEFAULT 'MOCK_PROVIDER',
    provider_transaction_id VARCHAR(100),
    authorization_code VARCHAR(50),
    masked_card_number VARCHAR(20),
    card_holder_name VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, AUTHORIZED, SUCCESS, FAILED, REFUNDED
    failure_reason VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE refunds (
    id BIGSERIAL PRIMARY KEY,
    refund_reference VARCHAR(50) NOT NULL UNIQUE,
    booking_id BIGINT NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    payment_id BIGINT NOT NULL REFERENCES payments(id),
    original_amount NUMERIC(10,2) NOT NULL,
    cancellation_fee NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    refund_amount NUMERIC(10,2) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PROCESSED', -- PENDING, PROCESSED, FAILED
    processed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE promotions (
    id BIGSERIAL PRIMARY KEY,
    coupon_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    discount_type VARCHAR(20) NOT NULL, -- PERCENTAGE, FLAT_AMOUNT
    discount_value NUMERIC(10,2) NOT NULL,
    minimum_booking_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    maximum_discount_amount NUMERIC(10,2),
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    usage_limit INT NOT NULL DEFAULT 1000,
    times_used INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE booking_promotions (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    promotion_id BIGINT NOT NULL REFERENCES promotions(id),
    discount_applied NUMERIC(10,2) NOT NULL,
    applied_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_booking_promo UNIQUE (booking_id, promotion_id)
);

-- Indexing for payment lookups and promos
CREATE INDEX idx_payments_transaction ON payments(transaction_reference);
CREATE INDEX idx_payments_booking ON payments(booking_id);
CREATE INDEX idx_refunds_reference ON refunds(refund_reference);
CREATE INDEX idx_promotions_code ON promotions(coupon_code);
