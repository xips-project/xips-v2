package cat.uvic.xips.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void builderShouldBuildCorrectUserProfile() {
        UUID id = UUID.randomUUID();
        String firstname = "John";
        String lastname = "Doe";
        LocalDate birthdate = LocalDate.now();
        String address = "123 Main St";
        String cityName = "Anytown";
        String zipCode = "12345";
        String country = "USA";
        User user = new User();

        UserProfile userProfile = UserProfile.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .birthdate(birthdate)
                .address(address)
                .cityName(cityName)
                .zipCode(zipCode)
                .country(country)
                .user(user)
                .build();

        assertEquals(id, userProfile.getId());
        assertEquals(firstname, userProfile.getFirstname());
        assertEquals(lastname, userProfile.getLastname());
        assertEquals(birthdate, userProfile.getBirthdate());
        assertEquals(address, userProfile.getAddress());
        assertEquals(cityName, userProfile.getCityName());
        assertEquals(zipCode, userProfile.getZipCode());
        assertEquals(country, userProfile.getCountry());
        assertNotNull(userProfile.getUser());
    }

}