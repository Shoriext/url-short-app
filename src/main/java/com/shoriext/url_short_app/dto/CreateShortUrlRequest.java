package com.shoriext.url_short_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShortUrlRequest {
    @NotBlank(message = "URL не может быть пустым")
    @URL(message = "Некорректный URL формат")
    private String originalUrl;

    private LocalDateTime expiresAt;
}
