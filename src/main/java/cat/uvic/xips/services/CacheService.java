package cat.uvic.xips.services;

public interface CacheService {

    void clearCache(String cacheName);

    void clearAllCaches();
}
