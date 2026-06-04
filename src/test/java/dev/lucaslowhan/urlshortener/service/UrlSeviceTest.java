package dev.lucaslowhan.urlshortener.service;

import dev.lucaslowhan.urlshortener.domain.Url;
import dev.lucaslowhan.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UrlSeviceTest {
    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    @Test
    void deveCriarShortCodeComOitoCaracteres(){
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
    void deveRetornarUrlQuandoCodigoExiste(){
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
    void deveRetornarVazioQuandoCodigoNaoExiste(){
        when(urlRepository.findByShortCode("inexistente"))
                .thenReturn(Optional.empty());

        Optional<Url> result = urlService.getOriginalUrl("inexistente");
        assertThat(result).isEmpty();
    }
}
