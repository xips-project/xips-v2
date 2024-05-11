package cat.uvic.xips.controller;

import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;
import cat.uvic.xips.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    public final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> findAll(){
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> findById(@PathVariable UUID id) {
        return productService.findById(id);
    }

    /*@GetMapping("/type/{productType}")
    public List<Product> findAllByProductType(@PathVariable ProductType productType){
        return productService.findAllByProductType(productType);
    }*/

    @PostMapping
    public void createProduct(@RequestBody Product product) {
        productService.save(product);
    }

    @PutMapping ("/{id}")
    public void updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        Optional<Product> existingProduct = productService.findById(id);

        if (existingProduct.isPresent()){
            Product updatedProduct = existingProduct.get();
            updatedProduct.setName(product.getName());
            updatedProduct.setProductType(product.getProductType());
            updatedProduct.setUsername(product.getUsername());
            productService.save(updatedProduct);
        } else {
            throw new NotFoundException("Product with id: "+id+" not found");
        }

    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable UUID id) {
        Optional<Product> existingProduct = productService.findById(id);
        if (existingProduct.isEmpty()){
            throw new NotFoundException("Product with id: "+id+" not found");
        }
        productService.remove(id);
    }
}
