package cat.uvic.xips.controller;

import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;
import cat.uvic.xips.entities.User;
import cat.uvic.xips.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    public final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/type/{productType}")
    public List<Product> findAllByProductType(@PathVariable ProductType productType) {
        return productService.findAllByProductType(productType);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        productService.update(id, product);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable UUID id) {
        productService.remove(id);
        return ResponseEntity.noContent().build();
    }


    // Stream API Endpoints

    @GetMapping("/{username}/all")
    public List<Product> getProductsByUser(@PathVariable String username){
        return productService.getProductsByUser(username);

    }

    @GetMapping("/average-ratings")
    public Map<String, Double> getAverageRatingsByUser() {
        return productService.getAverageRatingsByUsers();
    }

    @GetMapping("/by-type-sorted")
    public List<Product> getProductsByTypeAndSorted(@RequestParam ProductType productType){
        return productService.getProductsByTypeAndSorted(productType);
    }


    @GetMapping("/count-by-user")
    public Map<String, Long> countProductsByUser(){
        return productService.countProductsByUser();
    }

    @GetMapping("/top-rated-users/{numberOfUsers}")
    public List<User> getTopRatedUsers(@PathVariable long numberOfUsers){
        return productService.getTopRatedUsers(numberOfUsers);
    }

    @GetMapping("/partition-by-rating")
    public Map<Boolean,List<User>> partitionByRating(){
        return productService.partitionUsersByRating();
    }

    @GetMapping("/total-stars")
    public Long getTotalStars(){
        return productService.getTotalStars();
    }

    @GetMapping("/stats")
    public Map<User, Map<String, Double>> getStats() {
        return productService.getStats();
    }


}





