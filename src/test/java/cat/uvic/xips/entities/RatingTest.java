package cat.uvic.xips.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {
    @Test
    void builderShouldBuildCorrectRating() {
        UUID id = UUID.randomUUID();
        int stars = 5;
        String message = "Great product!";
        User user = new User();

        Rating rating = Rating.builder()
                .id(id)
                .stars(stars)
                .message(message)
                .user(user)
                .build();

        assertEquals(id, rating.getId());
        assertEquals(stars, rating.getStars());
        assertEquals(message, rating.getMessage());
        assertEquals(user, rating.getUser());
    }



}