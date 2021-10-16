package com.icommerce.shoppingapi.service.mapper;

import com.icommerce.shoppingapi.controller.dto.response.CartCommonResponseDTO;
import com.icommerce.shoppingapi.repository.model.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface CartMapper extends EntityMapper<CartCommonResponseDTO, Cart>{

    default Cart fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cart cart = new Cart();
        cart.setId(id);
        return cart;
    }
}
