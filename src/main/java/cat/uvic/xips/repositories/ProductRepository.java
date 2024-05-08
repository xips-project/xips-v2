package cat.uvic.xips.repositories;

import cat.uvic.xips.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.UUID;

public interface ProductRepository  extends JpaRepository<Product, UUID> {
}
