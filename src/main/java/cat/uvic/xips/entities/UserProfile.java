package cat.uvic.xips.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {"id", "user"})
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstname;
    private String lastname;
    @DateTimeFormat(pattern="dd-MM-yyyy")
    private LocalDate birthdate;
    private String address;
    private String cityName;
    private String zipCode;
    private String country;
    @OneToOne
    @JoinColumn(name="user_id", unique = true)
    private User user;

}
