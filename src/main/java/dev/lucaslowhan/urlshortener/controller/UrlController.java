package dev.lucaslowhan.urlshortener.controller;

import dev.lucaslowhan.urlshortener.domain.Url;
import dev.lucaslowhan.urlshortener.domain.UrlAccessEvent;
import dev.lucaslowhan.urlshortener.dto.request.CreateUrlRequest;
import dev.lucaslowhan.urlshortener.service.UrlAccessProducer;
import dev.lucaslowhan.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class UrlController {
    private final UrlService urlService;
    private final UrlAccessProducer urlAccessProducer;

    public UrlController(UrlService urlService, UrlAccessProducer urlAccessProducer) {
        this.urlService = urlService;
        this.urlAccessProducer = urlAccessProducer;
    }

    /**
     * Cria uma URL encurtada.
     * POST /api/urls
     *
     * @param request corpo da requisição contendo a URL original
     * @return 201 Created com o código curto gerado
     */
    @PostMapping("/api/urls")
    public ResponseEntity<String> createShortUrl(@RequestBody CreateUrlRequest request){
        String shortCode = urlService.create(request.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(shortCode);
    }

    /**
     * Redireciona um código curto para a URL original.
     * GET /{shortCode}
     *
     * @param shortCode código de 8 caracteres
     * @return 302 Found com header Location, ou 404 se não encontrado
     */
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String shortCode, HttpServletRequest request){
        Optional<Url> result = urlService.getOriginalUrl(shortCode);
        if(result.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(result.get().getOriginalUrl()));
            urlAccessProducer.publish(UrlAccessEvent.builder()
                    .shortCode(shortCode)
                    .ipAddress(request.getRemoteAddr())
                    .userAgent(request.getHeader("User-Agent"))
                    .build());
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
