package dev.lucaslowhan.urlshortener.controller;

import dev.lucaslowhan.urlshortener.dto.request.CreateUrlRequest;
import dev.lucaslowhan.urlshortener.service.UrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/urls")
    public ResponseEntity<String> createShortUrl(@RequestBody CreateUrlRequest request){
        String shortCode = urlService.create(request.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(shortCode);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String shortCode){
        Optional<String> result = urlService.getOriginalUrl(shortCode);
        if(result.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(result.get()));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
