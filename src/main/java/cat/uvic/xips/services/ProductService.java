package cat.uvic.xips.services;

import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;
import cat.uvic.xips.entities.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductService {

    List<Product> findAll();

    Product findById(UUID id);

    Product save(Product product);

    void remove(UUID id);

    List<Product> findAllByProductType(ProductType productType);




    Product update(UUID id, Product updatedProduct);

    List<Product> getProductsByUser(String username);

    Map<String, Double> getAverageRatingsByUsers();

    List<Product> getProductsByTypeAndSorted(ProductType productType);

    Map<String, Long> countProductsByUser();




    List<User> getTopRatedUsers(long numberOfUsers);

    Map<Boolean, List<User>> partitionUsersByRating();

    long getTotalStars();

    Map<User, Map<String, Double>> getStats();
}
