package com.urlshortener.url_shortener.repository;

import com.urlshortener.url_shortener.model.UrlMapping;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUrlRepository implements UrlRepository {

    private final ConcurrentHashMap<String, UrlMapping> shortUrlToMapping = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, UrlMapping> longUrlToMapping = new ConcurrentHashMap<>();

    @Override
    public void save(UrlMapping mapping){
        String shortUrl = mapping.getShortUrl();
        String longUrl = mapping.getLongUrl();
        shortUrlToMapping.put(shortUrl, mapping);
        longUrlToMapping.put(longUrl, mapping);
    }

    @Override
    public UrlMapping findByShortUrl(String shortUrl) {
        return shortUrlToMapping.get(shortUrl);
    }

    @Override
    public UrlMapping findByLongUrl(String longUrl) {
        return longUrlToMapping.get(longUrl);
    }

    @Override
    public boolean existsByShortUrl(String shortUrl) {
        return shortUrlToMapping.containsKey(shortUrl);
    }

    @Override
    public boolean existsByLongUrl(String longUrl) {
        UrlMapping mapping = longUrlToMapping.get(longUrl);
        return mapping != null && !mapping.isExpired();
    }

    @Override
    public void deleteByShortUrl(String shortUrl) {
        UrlMapping mapping = shortUrlToMapping.remove(shortUrl);

        if (mapping != null) {
            longUrlToMapping.remove(mapping.getLongUrl());
        }
    }
}
