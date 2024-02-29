package com.example.category.dto;

import lombok.Builder;

@Builder
public record ResponseCategoryDto (

        Long id,
        String name,
        String description
){
}
