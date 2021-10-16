package com.icommerce.shoppingapi.service;

import com.icommerce.shoppingapi.controller.dto.request.CartRequestDTO;
import com.icommerce.shoppingapi.controller.dto.response.CartCommonResponseDTO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface CartService {

    CartCommonResponseDTO getCartById(Long id);

    CartCommonResponseDTO create(@NotNull(message = "The cart cannot be null.") @Valid CartRequestDTO cart);

    CartCommonResponseDTO update(Long id, @NotNull(message = "The cart cannot be null.") @Valid CartRequestDTO cart);
}
