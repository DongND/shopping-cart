package com.icommerce.shoppingapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartCommonResponseDTO {

    private Long id;

    private String description;

    private String orderStatus;

    private String userPhone;

    private String userAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;

    private List<CartProductResponse> products;

    public Double getTotalCartPrice() {
        double sum = 0D;
        List<CartProductResponse> cartProducts = this.getProducts();
        for (CartProductResponse cp : cartProducts) {
            sum += cp.getTotalPrice();
        }

        return sum;
    }

    public int getNumberOfProducts() {
        return this.products.size();
    }
}
