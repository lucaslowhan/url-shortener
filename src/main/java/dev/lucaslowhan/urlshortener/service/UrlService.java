package dev.lucaslowhan.urlshortener.service;

import dev.lucaslowhan.urlshortener.domain.Url;
import dev.lucaslowhan.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {
    private UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
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
        return urlRepository.findByShortCode(shortCode);
    }
}
