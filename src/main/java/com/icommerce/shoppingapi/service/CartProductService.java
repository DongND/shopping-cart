package com.icommerce.shoppingapi.service;

import com.icommerce.shoppingapi.repository.model.Cart;
import com.icommerce.shoppingapi.repository.model.CartProduct;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface CartProductService {

    CartProduct create(@NotNull(message = "The products for cart cannot be null.") @Valid CartProduct cartProduct);
    List<CartProduct> findByCart(Cart cart);
    void deleteById(Long id);
}
