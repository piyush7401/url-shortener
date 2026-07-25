package com.urlshortener.url_shortener.repository;

import com.urlshortener.url_shortener.model.UrlMapping;

public interface UrlRepository {
    void saveToDB(UrlMapping mapping);
    UrlMapping getLongURL(String shortUrl);
    boolean checkShortURL(String shortUrl);
    boolean checkLongUrlExist(String longUrl);
}
