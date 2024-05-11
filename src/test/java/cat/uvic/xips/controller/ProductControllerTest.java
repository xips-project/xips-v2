package cat.uvic.xips.controller;

import cat.uvic.xips.entities.Product;
import cat.uvic.xips.entities.ProductType;
import cat.uvic.xips.repositories.ProductRepository;
import cat.uvic.xips.repositories.UserRepository;
import cat.uvic.xips.security.config.ApplicationConfig;
import cat.uvic.xips.security.config.SecurityConfig;
import cat.uvic.xips.security.jwt.JWTService;
import cat.uvic.xips.services.ProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import({SecurityConfig.class, ApplicationConfig.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    JWTService jwtService;

    @MockBean
    UserRepository userRepository;

    private final String basePath = "/api/v1/products";
    private static List<Product> products;

    // TODO -> needs some work
    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtRequestPostProcessor() {

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
    public void shouldGetNoProducts() throws Exception {
        given(productService.findAll()).willReturn(new ArrayList<>());

        mockMvc.perform(get(basePath)
                        .with(jwtRequestPostProcessor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    public void shouldGetTwoProducts() throws Exception {
        given(productService.findAll()).willReturn(products);

        mockMvc.perform(get(basePath)
                        .with(jwtRequestPostProcessor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test // TODO
    void getProductsAndRatingsByUsername() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get(basePath + "/list/user/afcasco")).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
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






}