package com.urlshortener.url_shortener.controller;

import com.urlshortener.url_shortener.model.UrlMapping;
import com.urlshortener.url_shortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UrlController {
    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/short")
    public String shortenUrl(@RequestParam String longurl, @RequestParam(required = false) String alias) {
        return urlShortenerService.shortenUrl(longurl,alias);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl){
        UrlMapping map = urlShortenerService.resolve(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, map.getLongUrl()).build();
    }
}
