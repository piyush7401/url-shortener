package com.urlshortener.url_shortener.model;

import java.time.LocalDateTime;

public class UrlMapping {
    private String longUrl;
    private String shortUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiryAt;
    private String customAlias;
    private long clickCount;


    public String getLongUrl() {
        return longUrl;
    }
    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
    public String getShortUrl() {
        return shortUrl;
    }
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getExpiryAt() {
        return expiryAt;
    }
    public void setExpiryAt(LocalDateTime expiryAt) {
        this.expiryAt = expiryAt;
    }
    public String getCustomAlias() {
        return customAlias;
    }
    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }
    public long getClickCount() {
        return clickCount;
    }
    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }
    public  UrlMapping(String longUrl, String shortUrl, LocalDateTime createdAt, LocalDateTime expiryAt, String customAlias) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.createdAt = createdAt;
        this.expiryAt = expiryAt;
        this.customAlias = customAlias;
        clickCount = 0;
    }
    public boolean isExpired(){
        if(this.expiryAt == null){
            return false;
        }
        if(LocalDateTime.now().isAfter(this.expiryAt)){
            return true;
        }
        return false;
    }

    public void incrementClickCount() {
        this.clickCount++;
    }


}
