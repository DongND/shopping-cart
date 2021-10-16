package com.icommerce.shoppingapi.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AddProductCartRequestDTO {

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;
}
