package com.icommerce.shoppingapi.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private String brand;

    private String colour;

    private String pictureUrl;

    private Long stockQuantity;

    private String categoryName;
}
