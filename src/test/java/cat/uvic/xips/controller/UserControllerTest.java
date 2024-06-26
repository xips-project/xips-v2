package cat.uvic.xips.controller;

import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.entities.Role;
import cat.uvic.xips.entities.User;
import cat.uvic.xips.entities.UserProfile;
import cat.uvic.xips.repositories.UserRepository;
import cat.uvic.xips.security.config.ApplicationConfig;
import cat.uvic.xips.security.config.SecurityConfig;
import cat.uvic.xips.security.jwt.JWTService;
import cat.uvic.xips.services.imp.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, ApplicationConfig.class})
class UserControllerTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    JWTService jwtService;

    @MockBean
    UserServiceImpl userService;

    @Autowired
    ObjectMapper objectMapper;

    private static List<User> users;


    static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtRequestPostProcessor() {

        return jwt().jwt(jwt -> jwt
                .claim("sub", "testUser")
                .claim("roles", "USER")
                .notBefore(Instant.now())
        );
    }

    @BeforeAll
    static void setup(){
        UserProfile userProfile1 = UserProfile.builder()
                .firstname("John")
                .lastname("Doe")
                .birthdate(LocalDate.of(1990, 5, 15))
                .address("123 Main St")
                .cityName("Springfield")
                .zipCode("12345")
                .country("USA")
                .build();

        UserProfile userProfile2 = UserProfile.builder()
                .firstname("Jane")
                .lastname("Smith")
                .birthdate(LocalDate.of(1985, 10, 20))
                .address("456 Oak St")
                .cityName("Oakland")
                .zipCode("54321")
                .country("USA")
                .build();

        User user1 = User.builder()
                .id(UUID.randomUUID())
                .username("johndoe")
                .password("password1")
                .email("john.doe@example.com")
                .role(Role.USER)
                .userProfile(userProfile1)
                .build();

        User user2 = User.builder()
                .id(UUID.randomUUID())
                .username("janesmith")
                .password("password2")
                .email("jane.smith@example.com")
                .role(Role.USER)
                .userProfile(userProfile2)
                .build();

        users = List.of(user1, user2);
    }


    @Test
    void shouldGetUserByUsername() throws Exception {

        User user = new User();
        String username = "test@gmail.com";
        user.setUsername(username);

        when(userService.findUser(username, null)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/user")
                        .with(jwtRequestPostProcessor())
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));

    }

    @Test
    void shouldGetUserId() throws Exception {
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);

        when(userService.findUser(null, id)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/user")
                        .with(jwtRequestPostProcessor())
                        .param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void shouldReturnBadRequestWhenNoIdOrUsernameProvided() throws Exception {

        when(userService.findUser(isNull(),isNull()))
                .thenThrow(new IllegalArgumentException(""));

        mockMvc.perform(get("/api/v1/users/user")
                        .with(jwtRequestPostProcessor()))

                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetUserByUsernameOrId() throws Exception {
        String username = "testUser";
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/users/user")
                        .with(jwtRequestPostProcessor())
                        .param("username", username)
                        .param("id", id.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllUsers() throws Exception {

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwtRequestPostProcessor()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("johndoe")))
                .andExpect(jsonPath("$[1].username", is("janesmith")));

    }

    @Test
    void shouldGetNoUsers() throws Exception {

        when(userService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/users")
                        .with(jwtRequestPostProcessor())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldCreateOneUser() throws Exception {

        UserCreationRequest userCreationRequest = new UserCreationRequest();
        userCreationRequest.setFirstName("Aleix");
        userCreationRequest.setEmail("aleixm@gmail.com");
        userCreationRequest.setLastName("Madrid");
        userCreationRequest.setRole(Role.USER);

        String requestBody = objectMapper.writeValueAsString(userCreationRequest);

        mockMvc.perform(post("/api/v1/users")
                .with(jwtRequestPostProcessor())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

}