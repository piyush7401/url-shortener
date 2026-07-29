package com.urlshortener.url_shortener.dto;

import com.urlshortener.url_shortener.strategy.StrategyType;

public class ShortenUrlRequest {
    private String longUrl;
    private String alias;
    private StrategyType strategy;

    public ShortenUrlRequest(String longUrl, String alias, StrategyType strategy) {
        this.longUrl = longUrl;
    }
    public String getLongUrl() {
        return longUrl;
    }
    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public StrategyType getStrategy() {
        return strategy;
    }
}
