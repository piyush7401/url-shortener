package com.urlshortener.url_shortener.strategy;

import java.security.MessageDigest;

public class Md5HashStrategy implements IdGenerationStrategy {
    @Override
    public String convertLongShort(String longUrl) {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] temp = md.digest(longUrl.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : temp) {
                String s = Integer.toHexString(0xff & b);
                if(s.length() == 1){
                    sb.append("0");
                }
                sb.append(s);
            }
            return sb.toString().substring(0,6);
        }catch (Exception e){
            throw new RuntimeException("Error generating MD5 Hash", e);
        }
    }
}
