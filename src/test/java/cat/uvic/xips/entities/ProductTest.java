package cat.uvic.xips.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void builderShouldBuildCorrectProduct() {
        UUID id = UUID.randomUUID();
        String name = "testProduct";
        ProductType productType = ProductType.BOOKS; // replace with actual ProductType
        String username = "testuser";

        Product product = Product.builder()
                .id(id)
                .name(name)
                .productType(productType)
                .username(username)
                .build();

        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(productType, product.getProductType());
        assertEquals(username, product.getUsername());
    }



}