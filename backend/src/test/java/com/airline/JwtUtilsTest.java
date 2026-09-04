package com.airline;

import com.airline.security.JwtUtils;
import com.airline.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000L);
    }

    @Test
    @DisplayName("Generate and validate JWT token")
    void testJwtTokenGenerationAndValidation() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@skynova.demo", "pass", "Test", "User", Collections.emptyList());
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = jwtUtils.generateJwtToken(auth);
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("test@skynova.demo", jwtUtils.getUserNameFromJwtToken(token));
    }
}
