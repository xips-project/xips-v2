package cat.uvic.xips.controller;

import cat.uvic.xips.services.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cache")
public class CacheController {

    private final CacheService cacheService;

    @GetMapping("clearCache/{cache}")
    public ResponseEntity<?> evictSingleCacheByName(@PathVariable String cache) {
        cacheService.clearCache(cache);
        return ResponseEntity.ok().build();
    }

    @GetMapping("clearCache")
    public ResponseEntity<?> evictSingleCacheByName() {
        cacheService.clearAllCaches();
        return ResponseEntity.ok().build();
    }
}
