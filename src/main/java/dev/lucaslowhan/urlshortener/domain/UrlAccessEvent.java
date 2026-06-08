package dev.lucaslowhan.urlshortener.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlAccessEvent {
    private String shortCode;
    private String ipAddress;
    private String userAgent;
}
