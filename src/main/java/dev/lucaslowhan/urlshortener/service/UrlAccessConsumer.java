package dev.lucaslowhan.urlshortener.service;

import dev.lucaslowhan.urlshortener.domain.UrlAccess;
import dev.lucaslowhan.urlshortener.domain.UrlAccessEvent;
import dev.lucaslowhan.urlshortener.repository.UrlAccessRepository;
import dev.lucaslowhan.urlshortener.repository.UrlRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UrlAccessConsumer {
    private final UrlAccessRepository urlAccessRepository;
    private final UrlRepository urlRepository;

    public UrlAccessConsumer(UrlAccessRepository urlAccessRepository, UrlRepository urlRepository) {
        this.urlAccessRepository = urlAccessRepository;
        this.urlRepository = urlRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.url-access}")
    public void consume(UrlAccessEvent event){
        urlRepository.findByShortCode(event.getShortCode())
                .ifPresent(url -> {
                    UrlAccess access = UrlAccess.builder()
                            .urlId(url.getId())
                            .ipAddress(event.getIpAddress())
                            .userAgent(event.getUserAgent())
                            .build();
                    urlAccessRepository.save(access);
                });
    }
}
