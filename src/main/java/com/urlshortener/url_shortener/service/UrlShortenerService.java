package com.urlshortener.url_shortener.service;

import com.urlshortener.url_shortener.exception.AliasAlreadyExistsException;
import com.urlshortener.url_shortener.exception.ShortCodeGenerationException;
import com.urlshortener.url_shortener.exception.UrlExpiredException;
import com.urlshortener.url_shortener.exception.UrlNotFoundException;
import com.urlshortener.url_shortener.model.UrlMapping;
import com.urlshortener.url_shortener.repository.UrlRepository;
import com.urlshortener.url_shortener.strategy.IdGenerationStrategy;
import com.urlshortener.url_shortener.strategy.IdGeneratorFactory;
import com.urlshortener.url_shortener.strategy.StrategyType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlShortenerService {
    private UrlRepository urlRepository;
    private IdGeneratorFactory idGeneratorFactory;

    public UrlShortenerService(UrlRepository urlRepository,IdGeneratorFactory idGeneratorFactory ) {
        this.urlRepository = urlRepository;
        this.idGeneratorFactory = idGeneratorFactory;
    }

    public String shortenUrl(String longUrl, String alias, StrategyType strategyType) {
        if(urlRepository.checkLongUrlExist(longUrl)){
            return urlRepository.getShortUrl(longUrl);
        }
        if(alias == null){
            IdGenerationStrategy strategy;
            if(strategyType == null){
                strategy = idGeneratorFactory.getIdGenerator(StrategyType.BASE62);
            }
            else{
                strategy = idGeneratorFactory.getIdGenerator(strategyType);
            }
            for(int i =0;i<5;i++){
                String shortUrl = strategy.convertLongShort(longUrl);
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

    public UrlMapping resolve(String shortUrl){
        UrlMapping map = urlRepository.getLongURL(shortUrl);
        if(map == null){
            throw new UrlNotFoundException("Url Not Found");
        }
        if(map.isExpired()){
            throw new UrlExpiredException("Url Expired");
        }
        long clicks = map.getClickCount()+ 1;
        map.setClickCount(clicks);
        return map;
    }

}
