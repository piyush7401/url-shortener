package com.urlshortener.url_shortener.repository;

import com.urlshortener.url_shortener.model.UrlMapping;

public interface UrlRepository {
    void save(UrlMapping mapping);
    UrlMapping findByShortUrl(String shortUrl);
    UrlMapping findByLongUrl(String longUrl);
    boolean existsByShortUrl(String shortUrl);
    boolean existsByLongUrl(String longUrl);
    void deleteByShortUrl(String shortUrl);
}
