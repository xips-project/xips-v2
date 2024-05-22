package cat.uvic.xips.repositories;

import cat.uvic.xips.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
}
