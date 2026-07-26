package com.urlshortener.url_shortener.service;

import com.urlshortener.url_shortener.exception.AliasAlreadyExistsException;
import com.urlshortener.url_shortener.exception.ShortCodeGenerationException;
import com.urlshortener.url_shortener.model.UrlMapping;
import com.urlshortener.url_shortener.repository.UrlRepository;
import com.urlshortener.url_shortener.strategy.IdGenerationStrategy;

import java.time.LocalDateTime;

public class UrlShortenerService {
    private UrlRepository urlRepository;
    private IdGenerationStrategy idGenerationStrategy;

    public UrlShortenerService(UrlRepository urlRepository, IdGenerationStrategy idGenerationStrategy) {
        this.urlRepository = urlRepository;
        this.idGenerationStrategy = idGenerationStrategy;
    }

    public String shortenUrl(String longUrl, String alias){
        if(urlRepository.checkLongUrlExist(longUrl)){
            return urlRepository.getShortUrl(longUrl);
        }
        if(alias == null){
            for(int i =0;i<5;i++){
                String shortUrl = idGenerationStrategy.convertLongShort(longUrl);
                if(urlRepository.checkShortUrlExist(shortUrl) == false){
                    LocalDateTime createdAt = LocalDateTime.now();
                    LocalDateTime expiryAt = createdAt.plusDays(10);

                    UrlMapping map = new UrlMapping(longUrl,shortUrl,createdAt,expiryAt,alias);

                    urlRepository.saveToDB(map);
                    return shortUrl;
                }
            }
            throw new ShortCodeGenerationException("Short Url generation Attempt exhausted");
        }
        else{
            if(urlRepository.checkShortUrlExist(alias)){
                throw new AliasAlreadyExistsException("Alias Already Exists");
            }
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime expiryAt = createdAt.plusDays(10);

            UrlMapping map = new UrlMapping(longUrl,alias,createdAt,expiryAt,alias);

            urlRepository.saveToDB(map);
            return alias;
        }
    }

}
