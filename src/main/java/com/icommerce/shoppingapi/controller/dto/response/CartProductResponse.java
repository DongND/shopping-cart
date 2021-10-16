package com.icommerce.shoppingapi.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProductResponse {

    private Long productId;

    private Double price;

    private Integer quantity;

    private Double totalPrice;

}
