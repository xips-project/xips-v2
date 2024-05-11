package cat.uvic.xips.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JWTAuthenticationFilterTest {

    private JWTAuthenticationFilter jwtAuthenticationFilter;
    private JWTService jwtService;
    private UserDetails mockUserDetails;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private FilterChain mockFilterChain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JWTService();
        mockUserDetails = new User("testUser", "testPassword", Collections.emptyList());
        jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtService, userDetails -> mockUserDetails);
    }

    @Test
    public void testDoFilterInternal() throws ServletException, IOException {
        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + jwtService.getToken(mockUserDetails));

        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);
    }

    @Test
    public void testGetTokenFromRequest() {
        when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + jwtService.getToken(mockUserDetails));

        String token = jwtAuthenticationFilter.getTokenFromRequest(mockRequest);

        assertEquals(jwtService.getToken(mockUserDetails), token);
    }
}