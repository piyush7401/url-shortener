package com.urlshortener.url_shortener.repository;

import com.urlshortener.url_shortener.model.UrlMapping;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUrlRepository implements UrlRepository {

    private ConcurrentHashMap<String, UrlMapping> map = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, UrlMapping> longUrlDuplicate = new ConcurrentHashMap<>();
    @Override
    public void saveToDB(UrlMapping mapping){
        String shortUrl = mapping.getShortUrl();
        String longUrl = mapping.getLongUrl();
        map.put(shortUrl, mapping);
        longUrlDuplicate.put(longUrl, mapping);
    }

    @Override
    public UrlMapping getLongURL(String shortUrl) {
        return map.get(shortUrl);
    }

    @Override
    public boolean checkShortUrlExist(String shortUrl) {
        if(map.containsKey(shortUrl)){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkLongUrlExist(String longUrl) {
        if(longUrlDuplicate.containsKey(longUrl)){
            UrlMapping mapping = longUrlDuplicate.get(longUrl);
            if(mapping.isExpired()){
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String getShortUrl(String longUrl) {
        return longUrlDuplicate.get(longUrl).getShortUrl();
    }
}
