package cat.uvic.xips.services;

import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.entities.User;
import cat.uvic.xips.repositories.UserRepository;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

        userService.deleteById(id);

        verify(userRepository, times(1)).deleteById(id);
    }


}