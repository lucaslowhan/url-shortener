CREATE TABLE url_accesses (
                              id BIGSERIAL PRIMARY KEY,
                              url_id BIGINT,
                              FOREIGN KEY (url_id) REFERENCES urls(id),
                              accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              ip_address TEXT,
                              user_agent TEXT
);