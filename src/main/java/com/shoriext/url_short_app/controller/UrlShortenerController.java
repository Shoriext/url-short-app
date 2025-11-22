package com.shoriext.url_short_app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoriext.url_short_app.dto.CreateShortUrlRequest;
import com.shoriext.url_short_app.dto.ShortUrlResponse;
import com.shoriext.url_short_app.service.UrlShortenerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/shorten")
@RequiredArgsConstructor
@Slf4j
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    /**
     * Создает сокращённую ссылку.
     *
     * @param request запрос с оригинальным URL.
     * @return ResponseEntity с ShortUrlResponse и статусом CREATED, либо BadRequest
     *         при ошибке.
     */
    @PostMapping
    public ResponseEntity<ShortUrlResponse> createShortUrl(
            @Valid @RequestBody CreateShortUrlRequest request) {

        try {
            // log.info("Request URL received: {}", request.getOriginalUrl());

            return ResponseEntity.status(HttpStatus.CREATED).body(urlShortenerService.createShortUrl(request));
        } catch (Exception e) {
            // log.error("Error creating short link: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Получает список всех коротких ссылок в системе.
     * 
     * Метод возвращает полный список всех сокращенных URL, которые
     * были созданы в системе. Список включает информацию о каждой
     * короткой ссылке: оригинальный URL, короткий код, дату создания
     * и статистику использования.
     * 
     * @return ResponseEntity со списком всех коротких ссылок или ошибкой:
     *         - HTTP 200 (OK) с List<ShortUrlResponse>, если запрос выполнен
     *         успешно
     *         - HTTP 400 (Bad Request) при возникновении ошибки обработки
     */
    @GetMapping
    public ResponseEntity<List<ShortUrlResponse>> getAllUrls() {
        try {
            // log.info("A request to retrieve all short links has been received.");
            return ResponseEntity.ok(urlShortenerService.getAllUrl());
        } catch (Exception e) {
            // log.error("Error getting all short links: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Получает детальную информацию о короткой ссылке.
     * 
     * Выполняет поиск короткой ссылки по предоставленному коду в системе
     * сокращения URL и возвращает полную информацию о ссылке, включая:
     * - оригинальный URL
     * - дату создания
     * - количество переходов
     * - дополнительную метаинформацию
     * 
     * @param shortCode уникальный идентификатор короткой ссылки
     *                  (не должен быть пустым или null)
     * @return ResponseEntity с результатом запроса:
     *         <ul>
     *         <li>HTTP 200 + ShortUrlResponse - при успешном поиске</li>
     *         <li>HTTP 400 + пустое тело - если код не найден в системе</li>
     *         <li>HTTP 400 + пустое тело - при внутренних ошибках обработки</li>
     *         </ul>
     * @throws IllegalArgumentException если параметр shortCode пустой или содержит
     *                                  только пробелы
     * @see ShortUrlResponse информационная модель ответа
     */
    @GetMapping("/{shortCode}/info")
    public ResponseEntity<ShortUrlResponse> getUrlInfo(@PathVariable String shortCode) {
        try {
            // log.info("Request information by short code: {}", shortCode);

            Optional<ShortUrlResponse> urlInfo = urlShortenerService.getOriginalUrl(shortCode);
            return urlInfo.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        } catch (Exception e) {
            // log.error("Error getting short link information: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
