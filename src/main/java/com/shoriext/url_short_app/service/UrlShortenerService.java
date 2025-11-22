package com.shoriext.url_short_app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.shoriext.url_short_app.dto.CreateShortUrlRequest;
import com.shoriext.url_short_app.dto.ShortUrlResponse;
import com.shoriext.url_short_app.entity.ShortUrl;
import com.shoriext.url_short_app.repository.ShortUrlRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UrlShortenerService {
    private final ShortUrlRepository shortUrlRepository;
    private static final Random RANDOM = new Random();

    private static final String BASE_URL = "http://localhost:8080/";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int CODE_LENGTH = 8;

    public UrlShortenerService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public ShortUrlResponse createShortUrl(CreateShortUrlRequest request) {
        String shortCode = generateUniqueShortCode();

        ShortUrl shortUrl = ShortUrl.builder()
                .originalUrl(request.getOriginalUrl())
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .expiredAt(request.getExpiresAt())
                .clickCount(0L)
                .build();

        ShortUrl savedUrl = shortUrlRepository.save(shortUrl);
        log.info("Создана короткая ссылка: {} -> {}", shortCode, request.getOriginalUrl());

        return mapToResponse(savedUrl);
    }

    public Optional<ShortUrlResponse> getOriginalUrl(String shortCode) {
        Optional<ShortUrl> shortUrl = shortUrlRepository.findByShortCode(shortCode);

        if (shortUrl.isPresent()) {
            ShortUrl url = shortUrl.get();
            if (url.getExpiredAt() != null && url.getExpiredAt().isBefore(LocalDateTime.now())) {
                log.warn("Короткая ссылка истекла: {}", shortCode);
                return Optional.empty();
            }

            url.setClickCount(url.getClickCount() + 1);
            shortUrlRepository.save(url);

            return Optional.of(mapToResponse(url));
        }

        return Optional.empty();
    }

    public List<ShortUrlResponse> getAllUrl() {
        return shortUrlRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ShortUrlResponse mapToResponse(ShortUrl shortUrl) {
        try {
            return ShortUrlResponse.builder()
                    .shortUrl(BASE_URL + shortUrl.getShortCode())
                    .originalUrl(shortUrl.getOriginalUrl())
                    .shortCode(shortUrl.getShortCode())
                    .createdAt(shortUrl.getCreatedAt())
                    .expiresAt(shortUrl.getExpiredAt())
                    .clickCount(shortUrl.getClickCount())
                    .build();
        } catch (Exception e) {
            log.error("Ошибка при создании короткой ссылки: {}", e.getMessage());
            return null;
        }
    }

    private String generateUniqueShortCode() {
        String shortCode;

        do {
            shortCode = generateRandomCode();
        } while (shortUrlRepository.exexistsByShortCode(shortCode));

        return shortCode;
    }

    private String generateRandomCode() {

        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}
