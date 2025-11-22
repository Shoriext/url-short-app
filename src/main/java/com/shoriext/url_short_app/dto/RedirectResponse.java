package com.shoriext.url_short_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedirectResponse {
    private String originalUrl;
    private String shortCode;
    private boolean redirect;
}