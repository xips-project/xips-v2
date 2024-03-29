package cat.uvic.xips.controllers;

import cat.uvic.xips.controller.UserController;
import cat.uvic.xips.entities.Role;
import cat.uvic.xips.entities.User;
import cat.uvic.xips.entities.UserProfile;
import cat.uvic.xips.security.jwt.JWTAuthenticationFilter;
import cat.uvic.xips.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    JWTAuthenticationFilter jwtAuthenticationFilter;


    @Test
    void shouldGetAllUsers() throws Exception {

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

        List<User> userList = Arrays.asList(user1, user2);

        when(userService.findAll()).thenReturn(userList);

        mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZmNhc2NvQGdtYWlsLmNvbSIsInJvbGUiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTcxMTcwNzgwOCwiZXhwIjoxNzExNzExNDA4fQ.KKZnFueK70AxtKQP2Kgyx2KGn5Vk67_4bK_0jWgewZ2rW8tXwmno_8yedRL2mFQyVGJclSN2CBnhBCktw8BLxg")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
