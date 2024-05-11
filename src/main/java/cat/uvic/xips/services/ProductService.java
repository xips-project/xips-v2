package cat.uvic.xips.services;

import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    public List<Product> findAll();

    public Product findById(UUID id);

    public Product save(Product product);

    public void remove(UUID id);

    public List<Product> findAllByProductType(ProductType productType);



}
