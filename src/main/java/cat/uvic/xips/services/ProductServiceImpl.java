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

}