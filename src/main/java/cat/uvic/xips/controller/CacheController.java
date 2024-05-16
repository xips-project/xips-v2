package cat.uvic.xips.controller;

import cat.uvic.xips.services.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cache")
public class CacheController {

    private final CacheService cacheService;

    @GetMapping("clearCache/{cache}")
    @ResponseStatus(HttpStatus.OK)
    public void evictSingleCacheByName(@PathVariable String cache) {
        cacheService.clearCache(cache);
    }

    @GetMapping("clearCache")
    @ResponseStatus(HttpStatus.OK)
    public void evictAllCaches() {
        cacheService.clearAllCaches();
    }
}
