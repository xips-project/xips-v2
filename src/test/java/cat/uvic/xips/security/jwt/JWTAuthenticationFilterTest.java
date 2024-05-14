package cat.uvic.xips.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTAuthenticationFilterTest {

    private JWTAuthenticationFilter jwtAuthenticationFilter;


    @Mock
    private JWTService jwtService;
    private UserDetails mockUserDetails;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private FilterChain mockFilterChain;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {

        mockUserDetails = new User("testUser", "testPassword", Collections.emptyList());
        jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtService, userDetails -> mockUserDetails);

       /* Field jwtSecretField = JWTService.class.getDeclaredField("jwtSecret");
        jwtSecretField.setAccessible(true);
        jwtSecretField.set(jwtService, "bXktYXBwLXN1cGVyLXNlY3JldC1mb3ItdGVzdHMtb25seQ==");*/

    }

    @Test
    void testDoFilterInternal() throws ServletException, IOException {
        String mockToken = "mockToken";
        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + mockToken);

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }

    @Test
    void testGetTokenFromRequest() {
        String mockToken = "mockToken";
        when(jwtService.getToken(any())).thenReturn(mockToken);
        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + mockToken);

        String token = jwtAuthenticationFilter.getTokenFromRequest(mockRequest);

        assertEquals(jwtService.getToken(mockUserDetails), token);
    }
}