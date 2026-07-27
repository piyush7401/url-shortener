    package com.urlshortener.url_shortener.strategy;
    import org.springframework.stereotype.Component;
    import java.util.Random;

    @Component
    public class Base62RandomStrategy implements IdGenerationStrategy {
        private static final String sb = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private static final Random rand = new Random();
        @Override
        public String convertLongShort(String longUrl) {

            StringBuilder shortUrl = new StringBuilder();
            for(int i =0;i<6;i++){
                int randindex = rand.nextInt(sb.length());
                shortUrl.append(sb.charAt(randindex));
            }

            return shortUrl.toString();
        }
        @Override
        public StrategyType getStrategyType() {
            return StrategyType.BASE62;
        }
    }
