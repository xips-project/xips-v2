package cat.uvic.xips.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;



class JWTServiceTest {


    private JWTService jwtService;
    private UserDetails mockUserDetails;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        jwtService = new JWTService();
        mockUserDetails = new User("testUser", "testPassword", Collections.emptyList());

        Field jwtSecretField = JWTService.class.getDeclaredField("jwtSecret");
        jwtSecretField.setAccessible(true);
        jwtSecretField.set(jwtService, "bXktYXBwLXN1cGVyLXNlY3JldC1mb3ItdGVzdHMtb25seQ==");
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