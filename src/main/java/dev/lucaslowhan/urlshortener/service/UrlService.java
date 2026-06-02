package dev.lucaslowhan.urlshortener.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {
    private HashMap<String, String> urlDatabase = new HashMap<>();

    public String create(String originalUrl){
            String shortCode = UUID.randomUUID().toString().substring(0, 8);
            urlDatabase.put(shortCode, originalUrl);
            return shortCode;
    }

    public Optional<String> getOriginalUrl(String shortCode){
        return Optional.ofNullable(urlDatabase.get(shortCode));
    }
}
