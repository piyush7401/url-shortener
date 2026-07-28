package com.urlshortener.url_shortener;

import com.urlshortener.url_shortener.model.UrlMapping;
import com.urlshortener.url_shortener.repository.InMemoryUrlRepository;
import com.urlshortener.url_shortener.repository.UrlRepository;
import com.urlshortener.url_shortener.service.UrlShortenerService;
import com.urlshortener.url_shortener.strategy.Base62RandomStrategy;
import com.urlshortener.url_shortener.strategy.IdGenerationStrategy;

public class ManualTestRunner {

    public static void main(String[] args) {
        UrlRepository repository = new InMemoryUrlRepository();
        IdGenerationStrategy strategy = new Base62RandomStrategy();
        UrlShortenerService service = new UrlShortenerService(repository, strategy);

        String shortUrl = service.shortenUrl("https://example.com", null);
        System.out.println("Generated short URL: " + shortUrl);

        String originalUrl = service.getOriginalUrl(shortUrl);
        System.out.println("Original URL: " + originalUrl);

        UrlMapping details = service.getUrlDetails(shortUrl);
        System.out.println("Click count: " + details.getClickCount());

        String alias = service.shortenUrl("https://google.com", "google-link");
        System.out.println("Custom alias: " + alias);

        String aliasOriginalUrl = service.getOriginalUrl(alias);
        System.out.println("Alias original URL: " + aliasOriginalUrl);
    }
}