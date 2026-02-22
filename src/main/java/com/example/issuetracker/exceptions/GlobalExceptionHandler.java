package com.example.issuetracker.exceptions;

import com.example.issuetracker.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Method to build response
    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(
            Exception ex, HttpStatus status, HttpServletRequest request){

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(error);
    }


    // Business Exception -> Bad Request 400
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessException(BaseException ex, HttpServletRequest request){
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    // Unauthorized Exceptions 401
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request){
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(TokenErrorException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenException(TokenErrorException ex, HttpServletRequest request){
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request);
    }

    // Not Found Exception -> Not Found 404
    @ExceptionHandler(NotFoundBaseException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundBaseException ex, HttpServletRequest request){
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    // Unexpected Error Exception -> Interal Server Error 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception ex, HttpServletRequest request){
        logger.error("Unhandled exception at {}:{}", request.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // Validation DTO -> Bad Request 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex,
                                                                      HttpServletRequest request){
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildErrorResponse(new Exception(errors), HttpStatus.BAD_REQUEST, request);
    }


}
