package net.trustly.github.controller;

import net.trustly.github.domain.ApiError;
import net.trustly.github.exception.InvalidRepositoryException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CustomRestExceptionHandlerController {

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ApiError handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST.value(),
                        ex.getLocalizedMessage(),
                        error,
                        request.getRequestURI());
        return apiError;
    }

    @ExceptionHandler(value = InvalidRepositoryException.class)
    public ApiError handleInvalidInput(InvalidRepositoryException ex, HttpServletRequest request) {
        String message = "The url parameter must be a valid public repository " +
                "address from github. Example: https://github.com/example/test";

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST.value(),
                        message,
                        ex.getLocalizedMessage(),
                        request.getRequestURI());
        return apiError;
    }


}
