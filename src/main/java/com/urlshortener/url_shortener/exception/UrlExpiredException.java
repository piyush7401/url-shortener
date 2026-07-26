package com.urlshortener.url_shortener.exception;

public class UrlExpiredException extends RuntimeException{
    public UrlExpiredException() {
        super();
    }
    public UrlExpiredException(String message) {
        super(message);
    }
}
