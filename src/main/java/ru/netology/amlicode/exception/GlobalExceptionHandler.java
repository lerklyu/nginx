package ru.netology.amlicode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Задача не найдена
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(
            ResourceNotFoundException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", exception.getMessage()));
    }

    // Ошибки валидации входных данных
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "error", "Ошибка валидации",
                        "messages", errors
                ));
    }
}

