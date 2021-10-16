package com.icommerce.shoppingapi.controller.dto.request;

import com.icommerce.shoppingapi.repository.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CartRequestDTO {

    @NotEmpty(message = "Can not add cart with empty product")
    private List<AddProductCartRequestDTO> cartProducts;

    private String description;

    private String userPhone;

    private String userAddress;

    private String orderStatus;
}
