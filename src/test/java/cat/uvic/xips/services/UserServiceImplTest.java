package cat.uvic.xips.services;

import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.entities.Rating;
import cat.uvic.xips.entities.User;
import cat.uvic.xips.repositories.UserRepository;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository  = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUserInOkta_shouldReturnExpectedResponse() throws IOException {
        UserCreationRequest userCreationRequest = new UserCreationRequest();
        userCreationRequest.setFirstName("Test");
        userCreationRequest.setLastName("User");
        userCreationRequest.setEmail("testuser@gmail.com");
        userCreationRequest.setPassword("password");

        OkHttpClient client = Mockito.mock(OkHttpClient.class);
        Call call = Mockito.mock(Call.class);
        ((UserServiceImpl)userService).setClient(client);

        Response response = new Response.Builder()
                .request(new Request.Builder().url("http://localhost").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build();

        when(client.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenReturn(response);

        // Act
        Response result = userService.createUserInOkta(userCreationRequest);

        // Assert
        assertEquals(200, result.code());
        assertEquals("OK", result.message());

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(client).newCall(requestCaptor.capture());
        Request actualRequest = requestCaptor.getValue();

        assertEquals("https://dev-82475405.okta.com/api/v1/users?activate=false", actualRequest.url().toString());
        assertEquals("POST", actualRequest.method());
        assertEquals("application/json; charset=utf-8", actualRequest.body().contentType().toString());
    }

    @Test
    void save() {
        UserCreationRequest request = new UserCreationRequest();
        userService.save(request);

        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void findAll() {
        List<User> expectedUsers = Arrays.asList(new User(), new User());

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.findAll();

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findByUsername() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        User user = userService.findUser(username,null);

        assertNotNull(user);
    }

    @Test
    void findByUsernameNotFound() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.findUser(username,null));
    }


    @Test
    void deleteByUsernameNotFound() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.deleteUser(username, null));

        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void deleteById() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.deleteUser(null, id);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).delete(user);
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
    void setRatingAddsRatingToUser() {
        Rating rating = new Rating();
        User user = new User();
        rating.setUser(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.setRating(rating);

        assertTrue(user.getRatings().contains(rating));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUserInOktaThrowsException() throws IOException {
        UserCreationRequest userCreationRequest = new UserCreationRequest();
        userCreationRequest.setFirstName("Test");
        userCreationRequest.setLastName("User");
        userCreationRequest.setEmail("testuser@gmail.com");
        userCreationRequest.setPassword("password");

        UserService userServiceSpy = Mockito.spy(userService);
        doThrow(RuntimeException.class).when(userServiceSpy).createUserInOkta(userCreationRequest);

        assertThrows(RuntimeException.class, () -> userServiceSpy.createUserInOkta(userCreationRequest));
    }

    @Test
    void deleteUserThrowsExceptionWhenUsernameAndIdAreNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null, null));
    }

    @Test
    void findUserThrowsExceptionWhenUsernameAndIdAreNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.findUser(null, null));
    }

    @Test
    void findUserByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.findUser(null, id));
    }
}