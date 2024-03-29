package cat.uvic.xips.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {

    private LocalDate birthdate;

    private String address;

    private String cityName;

    private String zipCode;

    private String country;
}
