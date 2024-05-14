package cat.uvic.xips.controller;

import cat.uvic.xips.repositories.UserRepository;
import cat.uvic.xips.security.config.ApplicationConfig;
import cat.uvic.xips.security.config.SecurityConfig;
import cat.uvic.xips.security.jwt.JWTService;
import cat.uvic.xips.services.CacheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CacheController.class)
@Import({SecurityConfig.class, ApplicationConfig.class})
class CacheControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CacheService cacheService;

    @MockBean
    JWTService jwtService;

    @MockBean
    UserRepository userRepository;

    static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtRequestPostProcessor() {

        return jwt().jwt(jwt -> jwt.claims(claims -> {

                })
                .tokenValue("messaging-client")
                .notBefore(Instant.now().minusSeconds(5L)));
    }

    @Test
    void evictSingleCacheByNameTest() throws Exception {
        String cacheName = "testCache";
        mockMvc.perform(get("/api/v1/cache/clearCache/" + cacheName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwtRequestPostProcessor()))
                .andExpect(status().isOk());

        verify(cacheService, times(1)).clearCache(cacheName);
    }

    @Test
    void evictAllCachesTest() throws Exception {
        mockMvc.perform(get("/api/v1/cache/clearCache")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwtRequestPostProcessor()))
                .andExpect(status().isOk());

        verify(cacheService, times(1)).clearAllCaches();
    }
}