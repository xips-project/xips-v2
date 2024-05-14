package cat.uvic.xips.controlleradvice;

import cat.uvic.xips.dto.ErrorResponse;
import cat.uvic.xips.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(error ->
                errors.put(error.getField(), "Field: " + error.getField() + ". " + error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllOtherExceptions(HttpServletRequest request, Exception ex) {
        ErrorResponse response = new ErrorResponse();
        response.setBackendMessage(ex.getLocalizedMessage());
        response.setUrl(request.getRequestURL().toString());
        response.setMethod(request.getMethod());
        response.setMessage("Internal Server Error");
        response.setTimestamp(LocalDateTime.now());

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(HttpServletRequest request, AccessDeniedException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setBackendMessage(ex.getLocalizedMessage());
        response.setUrl(request.getRequestURL().toString());
        response.setMethod(request.getMethod());
        response.setMessage("Access denied. You do not have the necessary permissions to access this resource.");
        response.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> wasNotFoundHandler(HttpServletRequest request, ProductNotFoundException ex){
        return ResponseEntity.notFound().build();
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentExceptionHandler(HttpServletRequest request, IllegalArgumentException ex){
        return ResponseEntity.badRequest().build();
    }
}
