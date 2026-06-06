package dev.lucaslowhan.urlshortener.service;

import dev.lucaslowhan.urlshortener.domain.Url;
import dev.lucaslowhan.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UrlService {
    @Value("${cache.url.ttl}")
    private long cacheTtl;
    private UrlRepository urlRepository;
    private RedisTemplate<String, String> redisTemplate;

    public UrlService(UrlRepository urlRepository, RedisTemplate<String, String> redisTemplate) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Encurta uma URL original gerando um código único de 8 caracteres.
     *
     * @param originalUrl URL completa a ser encurtada
     * @return código curto gerado
     */
    public String create(String originalUrl){
            String shortCode = UUID.randomUUID().toString().substring(0, 8);
            Url url = Url.builder()
                            .shortCode(shortCode)
                                    .originalUrl(originalUrl)
                                            .build();
            urlRepository.save(url);
            return url.getShortCode();
    }

    /**
     * Busca a URL original associada a um código curto.
     *
     * @param shortCode código de 8 caracteres gerado no momento da criação
     * @return Optional contendo a entidade URL, ou vazio se o código não existir
     */
    public Optional<Url> getOriginalUrl(String shortCode){
        String cachedUrl = redisTemplate.opsForValue().get(shortCode);
        if(cachedUrl != null){
            return Optional.of(Url.builder()
                    .shortCode(shortCode)
                    .originalUrl(cachedUrl)
                    .build());
        }
        Optional<Url> optional = urlRepository.findByShortCode(shortCode);

        optional.ifPresent(url ->
                redisTemplate.opsForValue().set(
                        shortCode,
                        url.getOriginalUrl(),
                        cacheTtl,
                        TimeUnit.SECONDS
                )
        );
        return optional;
    }
}
