package com.hajun.inventory.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemUpdateRequest(
        @NotBlank(message = "code는 필수야")
        @Size(max = 50, message = "code는 50자 이하여야 해")
        String code,

        @NotBlank(message = "name은 필수야")
        @Size(max = 100, message = "name은 100자 이하여야 해")
        String name,

        boolean active
) {}
