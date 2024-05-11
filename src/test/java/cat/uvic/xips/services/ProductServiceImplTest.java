package cat.uvic.xips.services;

import cat.uvic.xips.controller.NotFoundException;
import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;
import cat.uvic.xips.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void findByIdThrowsExceptionWhenProductNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.findById(id));

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void saveCallsSaveOnRepositoryWithCorrectProduct() {
        Product product = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.save(product);

        assertAll(
                () -> assertEquals(product, savedProduct),
                () -> verify(productRepository, times(1)).save(productCaptor.capture()),
                () -> assertEquals(product, productCaptor.getValue())
        );
    }

    @Test
    void removeCallsDeleteByIdOnRepositoryWithCorrectId() {
        UUID id = UUID.randomUUID();

        productService.remove(id);

        verify(productRepository, times(1)).deleteById(uuidCaptor.capture());
        assertEquals(id, uuidCaptor.getValue());
    }

    @Test
    void findAllReturnsNonEmptyList() {
        List<Product> expectedProducts = List.of(new Product());
        when(productRepository.findAll()).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.findAll();

        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    void findByIdReturnsProduct() {
        UUID id = UUID.randomUUID();
        Product expectedProduct = new Product();
        when(productRepository.findById(id)).thenReturn(Optional.of(expectedProduct));

        Product actualProduct = productService.findById(id);

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void saveUpdatesExistingProduct() {
        Product existingProduct = new Product();
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product updatedProduct = productService.save(existingProduct);

        assertEquals(existingProduct, updatedProduct);
    }

    @Test
    void removeNonExistingProductThrowsException() {
        UUID id = UUID.randomUUID();
        doThrow(new NotFoundException("Product not found")).when(productRepository).deleteById(id);

        assertThrows(NotFoundException.class, () -> productService.remove(id));
    }

    @Test
    void findAllByProductTypeReturnsNonEmptyList() {
        ProductType productType = ProductType.BOOKS;
        List<Product> expectedProducts = List.of(new Product());
        when(productRepository.findAllByProductType(productType)).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.findAllByProductType(productType);

        assertEquals(expectedProducts, actualProducts);
    }

}