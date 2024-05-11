package cat.uvic.xips.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;
    private UserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
        mockUserDetails = new User("testUser", "testPassword", Collections.emptyList());
    }

    @Test
    void testGetToken() {
        String token = jwtService.getToken(mockUserDetails);
        assertNotNull(token);
    }

    @Test
    void testGetUsernameFromToken() {
        String token = jwtService.getToken(mockUserDetails);
        String username = jwtService.getUsernameFromToken(token);
        assertEquals("testUser", username);
    }

    @Test
    void testIsTokenValid() {
        String token = jwtService.getToken(mockUserDetails);
        boolean isValid = jwtService.isTokenValid(token, mockUserDetails);
        assertTrue(isValid);
    }
}