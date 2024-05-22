package cat.uvic.xips.services.imp;

import cat.uvic.xips.services.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final CacheManager cacheManager;

    @Override
    public void clearCache(String cacheName) {
        Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();

    }

    @Override
    public void clearAllCaches() {
        var caches = cacheManager.getCacheNames();
        caches.forEach(cache -> Objects.requireNonNull(cacheManager.getCache(cache)).clear());
    }
}
