package dev.lucaslowhan.urlshortener.dto.request;

import lombok.Data;

@Data
public class CreateUrlRequest {
    private String originalUrl;
}
