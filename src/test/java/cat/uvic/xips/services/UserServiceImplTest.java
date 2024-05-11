package cat.uvic.xips.services;

import cat.uvic.xips.controller.NotFoundException;
import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.entities.Rating;
import cat.uvic.xips.entities.User;
import cat.uvic.xips.repositories.UserRepository;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OkHttpClient client;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testCreateUserInOkta() throws IOException {
        UserServiceImpl userService = new UserServiceImpl(userRepository);

        UserCreationRequest userCreationRequest = new UserCreationRequest();
        userCreationRequest.setFirstName("Test");
        userCreationRequest.setLastName("User");
        userCreationRequest.setEmail("testuser@gmail.com");
        userCreationRequest.setPassword("password");

        Response response = new Response.Builder()
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build();

        UserServiceImpl userServiceSpy = Mockito.spy(userService);
        doReturn(response).when(userServiceSpy).createUserInOkta(userCreationRequest);

        Response result = userServiceSpy.createUserInOkta(userCreationRequest);

        assertEquals(200, result.code());
        assertEquals("OK", result.message());
    }

    @Test
    void save() {
        UserCreationRequest request = new UserCreationRequest();
        userService.save(request);

        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void findAll() {
        userService.findAll();

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findByUsername() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        User user = userService.findByUsername(username);

        assertNotNull(user);
    }

    @Test
    void findByUsernameNotFound() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername(username));
    }

    @Test
    void findUserById() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));

        Optional<User> user = userService.findUserById(id);

        assertTrue(user.isPresent());
    }

    @Test
    void deleteByUsername() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        userService.deleteByUsername(username);

        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void deleteByUsernameNotFound() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.deleteByUsername(username));
    }

    @Test
    void deleteById() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteById(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void saveUpdatesExistingUser() {
        UserCreationRequest request = new UserCreationRequest();
        User existingUser = new User();
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        userService.save(request);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteByUsernameThrowsExceptionForNonExistingUser() {
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.deleteByUsername(username));
    }

    @Test
    void deleteByIdThrowsExceptionForNonExistingUser() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteById(id));
    }

    @Test
    void setRatingAddsRatingToUser() {
        Rating rating = new Rating();
        User user = new User();
        rating.setUser(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.setRating(rating);

        assertTrue(user.getRatings().contains(rating));
        verify(userRepository, times(1)).save(user);
    }




}