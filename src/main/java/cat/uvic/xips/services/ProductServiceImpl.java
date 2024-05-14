package cat.uvic.xips.services;

import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;
import cat.uvic.xips.exception.ProductNotFoundException;
import cat.uvic.xips.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Cache productsCache;

    public ProductServiceImpl(ProductRepository productRepository, CacheManager cacheManager) {
        this.productRepository = productRepository;
        this.productsCache = cacheManager.getCache("products");
    }

    @Override
    public List<Product> findAll() {
        return Optional.ofNullable(productsCache.get("", productRepository::findAll))
                .orElseThrow(() -> new RuntimeException("Error retrieving products from cache"));
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
        Product savedProduct = productRepository.save(product);
        updateCachedList(savedProduct);
        return savedProduct;
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void remove(UUID id) {
        productRepository.deleteById(id);
        removeProductFromCachedList(id);
    }

    @Override
    public List<Product> findAllByProductType(ProductType productType) {
        return Optional.ofNullable(productsCache.get(productType,
                        () -> productRepository.findAllByProductType(productType)))
                .orElseThrow(() -> new RuntimeException("Error retrieving products from cache"));
    }

    @Override
    public Product update(UUID id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setProductType(updatedProduct.getProductType());
                    Product savedProduct = productRepository.save(product);
                    updateCachedList(savedProduct);
                    return savedProduct;
                })
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found."));
    }

    private void updateCachedList(Product product) {
        try {
            List<Product> cachedProducts = productsCache.get("", ArrayList::new);
            assert cachedProducts != null;
            cachedProducts.removeIf(p -> p.getId().equals(product.getId()));
            cachedProducts.add(product);
            productsCache.put("", cachedProducts);
        } catch (Cache.ValueRetrievalException e) {
            throw new RuntimeException("Error updating cached list of products", e);
        }
    }

    private void removeProductFromCachedList(UUID id) {
        try {
            List<Product> cachedProducts = productsCache.get("", ArrayList::new);
            Objects.requireNonNull(cachedProducts).removeIf(p -> p.getId().equals(id));
            productsCache.put("", cachedProducts);
        } catch (Cache.ValueRetrievalException e) {
            throw new RuntimeException("Error removing product from cached list", e);
        }
    }
}