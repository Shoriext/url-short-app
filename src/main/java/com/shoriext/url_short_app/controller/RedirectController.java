package com.shoriext.url_short_app.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shoriext.url_short_app.dto.RedirectResponse;
import com.shoriext.url_short_app.dto.ShortUrlResponse;
import com.shoriext.url_short_app.service.UrlShortenerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final UrlShortenerService urlShortenerService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<RedirectResponse> getOriginalUrl(@PathVariable String shortCode) {
        Optional<ShortUrlResponse> urlResponse = urlShortenerService.getOriginalUrl(shortCode);

        RedirectResponse redirectResponse = RedirectResponse.builder()
                .originalUrl(urlResponse.get().getOriginalUrl())
                .shortCode(shortCode)
                .redirect(true)
                .build();

        return ResponseEntity.ok(redirectResponse);

    }
}
