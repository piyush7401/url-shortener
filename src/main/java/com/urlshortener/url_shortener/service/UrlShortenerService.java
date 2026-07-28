package com.urlshortener.url_shortener.service;

import com.urlshortener.url_shortener.exception.*;
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

    //Method to shorten the url
    public String shortenUrl(String longUrl, String alias){
        validateLongUrl(longUrl);
        validateAlias(alias);
        if(urlRepository.existsByLongUrl(longUrl)){
            return urlRepository.findByLongUrl(longUrl).getShortUrl();
        }
        if(alias == null){
            for(int i =0;i<5;i++){
                String shortUrl = idGenerationStrategy.convertLongShort(longUrl);
                if(!urlRepository.existsByShortUrl(shortUrl)){
                    LocalDateTime createdAt = LocalDateTime.now();
                    LocalDateTime expiryAt = createdAt.plusDays(10);

                    UrlMapping map = new UrlMapping(longUrl,shortUrl,createdAt,expiryAt,alias);

                    urlRepository.save(map);
                    return shortUrl;
                }
            }
            throw new ShortCodeGenerationException("Short Url generation Attempt exhausted");
        }
        else{
            if(urlRepository.existsByShortUrl(alias)){
                throw new AliasAlreadyExistsException("Alias Already Exists");
            }
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime expiryAt = createdAt.plusDays(10);

            UrlMapping map = new UrlMapping(longUrl,alias,createdAt,expiryAt,alias);

            urlRepository.save(map);
            return alias;
        }
    }

    public UrlMapping getUrlDetails(String shortUrl){
        UrlMapping mapping = urlRepository.findByShortUrl(shortUrl);
        if(mapping == null){
            throw new ShortUrlNotFoundException("Short url not found");
        }
        if(mapping.isExpired()){
            urlRepository.deleteByShortUrl(shortUrl);
            throw new UrlExpiredException("short url has expired");
        }
        return mapping;
    }

    //method to get the original long url from short url
    public String getOriginalUrl(String shortUrl){
        UrlMapping mapping = urlRepository.findByShortUrl(shortUrl);
        if(mapping == null){
            throw new ShortUrlNotFoundException("Short url not found");
        }
        if(mapping.isExpired()){
            urlRepository.deleteByShortUrl(shortUrl);
            throw new UrlExpiredException("Short url is expired");
        }
        mapping.incrementClickCount();

        return mapping.getLongUrl();
    }

    private void validateLongUrl(String longUrl){
        if(longUrl == null || longUrl.isBlank()){
            throw new InvalidUrlException("Url cannot be empty!");
        }
        if(!longUrl.startsWith("http://") && !longUrl.startsWith("https://")){
            throw new InvalidUrlException("URl must start with http:// ot https://");
        }
    }

    private void validateAlias(String alias){
        if(alias == null)
            return;
        if(alias.isBlank()){
            throw new InvalidAliasException("Alias cannot be blank");
        }
        if(alias.length()<3 || alias.length()>30){
            throw new InvalidAliasException("Alias length should be between 3 and 30");
        }
        if(!alias.matches("^[a-zA-Z0-9_-]+$")){
            throw new InvalidAliasException("Alias can only contains letters, numbers, hyphen and underscore");
        }
    }
}
