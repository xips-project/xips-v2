package cat.uvic.xips.dto;

import jdk.jfr.Timestamp;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ErrorResponse implements Serializable {

    private String backendMessage;
    private String message;
    private String url;
    private String method;
    private LocalDateTime timestamp;

}
