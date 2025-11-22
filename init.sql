CREATE TABLE IF NOT EXISTS short_urls (
    id BIGSERIAL PRIMARY KEY,
    original_url VARCHAR(2048) NOT NULL,
    short_code VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP,
    expires_at TIMESTAMP,
    click_count BIGINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_short_code ON short_urls(short_code);