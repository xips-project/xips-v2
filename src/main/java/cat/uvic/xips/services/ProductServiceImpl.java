package cat.uvic.xips.services;

import cat.uvic.xips.controller.NotFoundException;
import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;
import cat.uvic.xips.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id: " + id + "not found."));
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
