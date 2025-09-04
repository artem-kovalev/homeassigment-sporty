package com.sportygroup.providerapi.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatus(ResponseStatusException ex, HttpServletRequest req) {
        var status = HttpStatus.valueOf(ex.getStatusCode().value());
        var title = ex.getReason() != null
                ? ex.getReason()
                : status.getReasonPhrase();

        var pd = problem(status, title, "Request failed", req);
        return ResponseEntity.status(status).body(pd);
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ProblemDetail> handleMethodArgNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(
                FieldError::getField,
                DefaultMessageSourceResolvable::getDefaultMessage,
                (a, b) -> a, LinkedHashMap::new));

        var pd = problem(HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more fields are invalid",
                req);
        pd.setProperty("errors", fieldErrors);
        return ResponseEntity.badRequest().body(pd);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error", ex);

        var pd = problem(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred",
                req);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail, HttpServletRequest req) {
        var pd = ProblemDetail.forStatus(status);
        pd.setTitle(title);
        pd.setDetail(detail);
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }
}
