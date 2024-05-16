package cat.uvic.xips.dto;

import cat.uvic.xips.entities.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreationRequest {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Email(message = "Invalid email format. Please provide a valid email address.")
    private String email;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}", message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and be at least 8 characters long.")
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern= "dd-MM-yyyy")
    private LocalDate birthdate;

    private String address;

    private String cityName;

    private String zipCode;

    private String country;


}
