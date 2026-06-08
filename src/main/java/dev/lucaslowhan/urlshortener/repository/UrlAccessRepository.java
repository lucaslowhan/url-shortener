package dev.lucaslowhan.urlshortener.repository;

import dev.lucaslowhan.urlshortener.domain.UrlAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlAccessRepository extends JpaRepository<UrlAccess, Long> {
}
