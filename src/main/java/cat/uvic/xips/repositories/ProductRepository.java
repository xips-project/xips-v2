package cat.uvic.xips.repositories;

import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository  extends JpaRepository<Product, UUID> {

    List<Product> findAllByProductType(ProductType productType);
}
