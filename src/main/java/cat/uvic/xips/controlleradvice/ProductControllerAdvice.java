package cat.uvic.xips.controlleradvice;

import cat.uvic.xips.dto.ErrorResponse;
import cat.uvic.xips.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Order(1)
@RestControllerAdvice
public class ProductControllerAdvice {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> productNotFoundException(HttpServletRequest request, ProductNotFoundException ex){
        ErrorResponse response = new ErrorResponse();
        response.setBackendMessage(ex.getLocalizedMessage());
        response.setUrl(request.getRequestURL().toString());
        response.setMethod(request.getMethod());
        response.setMessage("Product not found");
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


}
