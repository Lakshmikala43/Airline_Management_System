package com.airline.service;

import com.airline.entity.User;
import com.airline.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LoyaltyRewardsServiceTest13 {

    @Mock private UserRepository userRepository;
    @InjectMocks private LoyaltyRewardsService loyaltyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify frequent flyer tier calculation and member discount - Suite 13")
    void testGetLoyaltyAccountSuite13() {
        User user = User.builder().id((long) i).email("user13@skynova.demo").firstName("Member13").lastName("User").build();
        when(userRepository.findByEmail("user13@skynova.demo")).thenReturn(Optional.of(user));

        var resp = loyaltyService.getLoyaltyAccount("user13@skynova.demo");
        assertNotNull(resp);
        assertEquals(LoyaltyRewardsService.LoyaltyTier.GOLD, resp.getTier());
        assertEquals(7.5, resp.getDiscountPercentage());
    }
}
