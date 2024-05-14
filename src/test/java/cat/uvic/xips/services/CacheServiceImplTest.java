package cat.uvic.xips.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheServiceImplTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private CacheServiceImpl cacheService;

    @Test
    void clearCache() {
        String cacheName = "testCache";
        when(cacheManager.getCache(cacheName)).thenReturn(cache);

        cacheService.clearCache(cacheName);

        verify(cache, times(1)).clear();
    }

    @Test
    void clearAllCaches() {
        List<String> cacheNames = Arrays.asList("cache1", "cache2", "cache3");
        when(cacheManager.getCacheNames()).thenReturn(cacheNames);
        when(cacheManager.getCache(anyString())).thenReturn(cache);

        cacheService.clearAllCaches();

        verify(cache, times(cacheNames.size())).clear();
    }
}