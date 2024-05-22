package cat.uvic.xips.services.imp;

import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;
import cat.uvic.xips.entities.Rating;
import cat.uvic.xips.entities.User;
import cat.uvic.xips.exception.ProductNotFoundException;
import cat.uvic.xips.repositories.ProductRepository;
import cat.uvic.xips.repositories.RatingRepository;
import cat.uvic.xips.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final RatingRepository ratingRepository;


    public ProductServiceImpl(ProductRepository productRepository, RatingRepository ratingRepository) {
        this.productRepository = productRepository;

        this.ratingRepository = ratingRepository;


    }

    @Override
    @Cacheable(value = "products", key = "'all'")
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Product findById(UUID id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + "not found."));
    }

    @Override
    @CachePut(value = "products", key = "#product.id")
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void remove(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "products", key = "#productType")
    public List<Product> findAllByProductType(ProductType productType) {
        return productRepository.findAllByProductType(productType);
    }

    @Override
    @CachePut(value = "products", key = "#id")
    public Product update(UUID id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setProductType(updatedProduct.getProductType());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found."));
    }


    // Testing Stream API

    @Override
    public List<Product> getProductsByUser(String username) {
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getUsername().equals(username))
                .toList();
    }


    public Map<String, Double> getAverageRatingsByUsers() {
        return ratingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        rating -> rating.getUser().getUsername(),
                        Collectors.averagingInt(Rating::getStars)
                ));
    }


    @Override
    public List<Product> getProductsByTypeAndSorted(ProductType productType) {
        return productRepository.findAll().stream()
                .filter(product -> product.getProductType() == productType)
                .sorted(Comparator.comparing(Product::getName))
                .toList();
    }

    @Override
    public Map<String, Long> countProductsByUser() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Product::getUsername,
                        Collectors.counting()
                ));
    }


    @Override
    public List<User> getTopRatedUsers(long numberOfUsers) {

        return ratingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Rating::getUser,
                        Collectors.averagingInt(Rating::getStars)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<User, Double>comparingByValue().reversed())
                .limit(numberOfUsers)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Boolean, List<User>> partitionUsersByRating() {
        return ratingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Rating::getUser,
                        Collectors.averagingInt(Rating::getStars)
                ))
                .entrySet().stream()
                .collect(Collectors.partitioningBy(
                        entry -> entry.getValue() > 3,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                ));
    }


    @Override
    public long getTotalStars() {
        return ratingRepository.findAll().stream()
                .map(Rating::getStars)
                .reduce(0, Integer::sum);
    }


    @Override
    public Map<User, Map<String, Double>> getStats() {
        return ratingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Rating::getUser,
                        Collectors.collectingAndThen(
                                Collectors.summarizingDouble(Rating::getStars),
                                stats -> {
                                    Map<String, Double> finalStats = new HashMap<>();
                                    finalStats.put("Ratings", (double) stats.getCount());
                                    finalStats.put("Total stars", stats.getSum());
                                    finalStats.put("Average", stats.getAverage());
                                    return finalStats;
                                }
                        )
                ));
    }
}