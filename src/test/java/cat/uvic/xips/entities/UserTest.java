package cat.uvic.xips.entities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    void getAuthoritiesShouldReturnCorrectAuthority() {
        User user = User.builder()
                .role(Role.USER)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(Role.USER.name())));
    }

    @Test
    void accountStatusMethodsShouldReturnTrue() {
        User user = new User();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void builderShouldBuildCorrectUser() {
        UUID id = UUID.randomUUID();
        String username = "testuser";
        String password = "password";
        String email = "testuser@example.com";
        Role role = Role.USER;

        User user = User.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .build();

        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(role, user.getRole());
    }

    @Test
    void invalidEmailShouldFailValidation() {
        User user = User.builder()
                .email("invalid email")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    void invalidPasswordShouldFailValidation() {
        User user = User.builder()
                .password("short")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

}