package net.trustly.github.exception;

public class InvalidRepositoryException extends RuntimeException{

    public InvalidRepositoryException(String errorMessage) {
        super(errorMessage);
    }

}
