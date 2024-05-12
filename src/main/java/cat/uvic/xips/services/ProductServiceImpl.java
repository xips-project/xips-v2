package cat.uvic.xips.services;

import cat.uvic.xips.exception.ProductNotFoundException;
import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;
import cat.uvic.xips.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Cacheable("products")
    public List<Product> findAll() {
        log.warn("findAll hit!");
        return productRepository.findAll();
    }

    @Cacheable("products")
    public Product findById(UUID id) {
        log.warn("FindById hit!");
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + "not found."));
    }


    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void remove(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findAllByProductType(ProductType productType) {
        return productRepository.findAllByProductType(productType);
    }


}
