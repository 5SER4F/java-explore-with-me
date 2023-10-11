package ru.practicum;

public class BadResponseException extends RuntimeException {
    public BadResponseException(String message) {
        super(message);
    }
}
