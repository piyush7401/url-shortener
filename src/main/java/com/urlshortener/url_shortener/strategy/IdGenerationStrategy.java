package com.urlshortener.url_shortener.strategy;

public interface IdGenerationStrategy {
    String convertLongShort(String longUrl);
}
