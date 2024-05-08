package cat.uvic.xips.services;

import cat.uvic.xips.entities.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    public List<Product> findAll();

    public Optional<Product> findById(UUID id);

    public void save(Product product);

    public void remove(UUID id);

}
