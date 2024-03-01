package com.shopapp.category.dto;

import lombok.Builder;

@Builder
public record ResponseCategoryDto (

        Long id,
        String name,
        String description
){
}
