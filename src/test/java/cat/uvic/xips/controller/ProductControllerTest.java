package cat.uvic.xips.controller;

import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;
import cat.uvic.xips.exception.ProductNotFoundException;
import cat.uvic.xips.repositories.UserRepository;
import cat.uvic.xips.security.config.ApplicationConfig;
import cat.uvic.xips.security.config.SecurityConfig;
import cat.uvic.xips.security.jwt.JWTService;
import cat.uvic.xips.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import({SecurityConfig.class, ApplicationConfig.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    JWTService jwtService;

    @MockBean
    UserRepository userRepository;

    private final String basePath = "/api/v1/products";
    private static List<Product> products;

    // TODO -> needs some work
    static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtRequestPostProcessor() {

        return jwt().jwt(jwt -> jwt.claims(claims -> {

                })
                .tokenValue("messaging-client")
                .notBefore(Instant.now().minusSeconds(5L)));
    }


    @BeforeAll
    static void setup() {

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .productType(ProductType.BOOKS)
                .name("book1")
                .username("afcasco")
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .productType(ProductType.FILMS)
                .name("movie1")
                .username("aleixmadrid")
                .build();

        products = List.of(product1, product2);
    }


    @Test
    void shouldGetNoProducts() throws Exception {
        given(productService.findAll()).willReturn(new ArrayList<>());

        mockMvc.perform(get(basePath)
                        .with(jwtRequestPostProcessor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    void shouldGetTwoProducts() throws Exception {
        given(productService.findAll()).willReturn(products);

        mockMvc.perform(get(basePath)
                        .with(jwtRequestPostProcessor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    void shouldReturnOneProductOfTypeBook() throws Exception {

        given(productService.findAllByProductType(ProductType.BOOKS))
                .willReturn(List.of(products.get(0)));

        mockMvc.perform(get(basePath + "/type/" + ProductType.BOOKS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }


    @Test
    void findByIdShouldFindProductTest() throws Exception {
        Product product = products.get(0);
        given(productService.findById(product.getId())).willReturn(product);

        mockMvc.perform(get(basePath + "/" + product.getId())
                        .with(jwtRequestPostProcessor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(product.getId().toString())));
    }

    @Test
    void findByIdShouldNotFindProductTest() throws Exception {
        Product product = products.get(0);
        given(productService.findById(product.getId()))
                .willThrow(new ProductNotFoundException("Product not found"));
        mockMvc.perform(get(basePath + "/" + product.getId())
                        .with(jwtRequestPostProcessor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllByProductType() throws Exception {
        given(productService.findAllByProductType(ProductType.BOOKS)).willReturn(List.of(products.get(0)));

        mockMvc.perform(get(basePath + "/type/" + ProductType.BOOKS)
                        .with(jwtRequestPostProcessor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void createProductTest() throws Exception {
        Product product = products.get(0);
        when(productService.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(post(basePath)
                        .with(jwtRequestPostProcessor())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isCreated());


        verify(productService, times(1)).save(any(Product.class));

    }

    @Test
    void updateProductByIdTest() throws Exception {
        Product product = products.get(0);
        when(productService.findById(product.getId())).thenReturn(product);
        when(productService.update(any(UUID.class), any(Product.class))).thenReturn(product);

        mockMvc.perform(put(basePath + "/" + product.getId())
                        .with(jwtRequestPostProcessor())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).update(any(UUID.class), any(Product.class));
    }

    @Test
    void deleteProductByIdTest() throws Exception {
        Product product = products.get(0);
        when(productService.findById(product.getId())).thenReturn(product);
        doNothing().when(productService).remove(product.getId());

        mockMvc.perform(delete(basePath + "/" + product.getId())
                        .with(jwtRequestPostProcessor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).remove(product.getId());
    }
}