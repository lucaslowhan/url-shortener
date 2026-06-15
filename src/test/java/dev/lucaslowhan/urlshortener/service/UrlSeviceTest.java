package dev.lucaslowhan.urlshortener.service;

import dev.lucaslowhan.urlshortener.domain.Url;
import dev.lucaslowhan.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UrlSeviceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private UrlService urlService;

    @BeforeEach
    void setUp() {
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(null);
    }

    @Test
    void deveCriarShortCodeComOitoCaracteres() {
        String originalUrl = "https://www.google.com";
        Url urlFalsa = Url.builder()
                .shortCode("abc12345")
                .originalUrl(originalUrl)
                .build();
        when(urlRepository.save(any(Url.class))).thenReturn(urlFalsa);

        String shortCode = urlService.create(originalUrl);

        assertThat(shortCode).hasSize(8);
        verify(urlRepository, times(1)).save(any(Url.class));
    }

    @Test
    void deveRetornarUrlQuandoCodigoExiste() {
        Url urlFalsa = Url.builder()
                .shortCode("abc12345")
                .originalUrl("https://www.google.com")
                .build();
        when(urlRepository.findByShortCode("abc12345"))
                .thenReturn(Optional.of(urlFalsa));

        Optional<Url> result = urlService.getOriginalUrl("abc12345");

        assertThat(result).isPresent();
        assertThat(result.get().getOriginalUrl())
                .isEqualTo("https://www.google.com");
    }

    @Test
    void deveRetornarVazioQuandoCodigoNaoExiste() {
        when(urlRepository.findByShortCode("inexistente"))
                .thenReturn(Optional.empty());

        Optional<Url> result = urlService.getOriginalUrl("inexistente");

        assertThat(result).isEmpty();
    }
}