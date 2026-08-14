package com.suhasm.ecommerce.exception;

import com.suhasm.ecommerce.dto.ErrorResponse;
import com.suhasm.ecommerce.dto.FieldErrorResponse;
import com.suhasm.ecommerce.dto.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(ProductNotFoundException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<FieldErrorResponse> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldErrorResponse(
                        fieldError.getField(),
               fieldError.getDefaultMessage())).toList();
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();
        validationErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        validationErrorResponse.setTimestamp(LocalDateTime.now());
        validationErrorResponse.setMessage( "Validation Failed");
        validationErrorResponse.setErrors(errors);
        log.error("Validation failed {}", String.valueOf(exception));
        return ResponseEntity.badRequest().body(validationErrorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ValidationErrorResponse> handleMissingRequestParameter(
            MissingServletRequestParameterException ex) {

        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();

        validationErrorResponse.setTimestamp(LocalDateTime.now());
        validationErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        validationErrorResponse.setMessage(
                "Required request parameter '" +
                        ex.getParameterName() +
                        "' is missing."
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(validationErrorResponse);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(
            CategoryNotFoundException ex,
            HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Category Not Found",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex,
            HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Email Not Found",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }


    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(
            RoleNotFoundException ex,
            HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Role Not Found",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(AuthenticatedUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticatedUserNotFound(
            AuthenticatedUserNotFoundException ex,
            HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "User Not Found",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(
            InsufficientStockException ex,
            HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Product Not in Stock",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFoundException(
            CartNotFoundException ex,
            HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Cart Not Found",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ErrorResponse> handleEmptyCartException(
            EmptyCartException ex,
            HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Cart is Empty",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(
            OrderNotFoundException ex,
            HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Order not found",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InvalidOrderStatusTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrderStatusTransitionException(
            InvalidOrderStatusTransitionException ex,
            HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Invalid Order transition",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }

}
