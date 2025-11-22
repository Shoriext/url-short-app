package com.shoriext.url_short_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shoriext.url_short_app.dto.ShortUrlResponse;
import com.shoriext.url_short_app.service.UrlShortenerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RedirectController {
    private final UrlShortenerService urlShortenerService;

    @GetMapping("/{shortCode}")
    public String redirectToOriginalUrl(@PathVariable("shortCode") String shortCode) {
        return urlShortenerService.getOriginalUrl(shortCode)
                .map(ShortUrlResponse::getOriginalUrl)
                .map(url -> "redirect:" + url)
                .orElseThrow(() -> new IllegalArgumentException("Short code not found: " + shortCode));
    }
}
