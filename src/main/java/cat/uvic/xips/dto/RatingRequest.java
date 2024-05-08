package cat.uvic.xips.dto;

import lombok.Data;

@Data
public class RatingRequest {
    private int stars;
    private String message;
}
