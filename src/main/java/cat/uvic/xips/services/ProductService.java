package cat.uvic.xips.services;

import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<Product> findAll();

    Product findById(UUID id);

    Product save(Product product);

    void remove(UUID id);

    List<Product> findAllByProductType(ProductType productType);




    Product update(UUID id, Product updatedProduct);
}
