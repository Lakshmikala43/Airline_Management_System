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

class LoyaltyRewardsServiceTest27 {

    @Mock private UserRepository userRepository;
    @InjectMocks private LoyaltyRewardsService loyaltyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify frequent flyer tier calculation and member discount - Suite 27")
    void testGetLoyaltyAccountSuite27() {
        User user = User.builder().id((long) i).email("user27@skynova.demo").firstName("Member27").lastName("User").build();
        when(userRepository.findByEmail("user27@skynova.demo")).thenReturn(Optional.of(user));

        var resp = loyaltyService.getLoyaltyAccount("user27@skynova.demo");
        assertNotNull(resp);
        assertEquals(LoyaltyRewardsService.LoyaltyTier.GOLD, resp.getTier());
        assertEquals(7.5, resp.getDiscountPercentage());
    }
}
