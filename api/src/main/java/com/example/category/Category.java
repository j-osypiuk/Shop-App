package com.example.category;

import com.example.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long categoryId;
    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String name;
    @Column(
            columnDefinition = "text"
    )
    private String description;
    @ManyToMany(
            mappedBy = "categories"
    )
    @JsonIgnore
    private List<Product> products;
}
