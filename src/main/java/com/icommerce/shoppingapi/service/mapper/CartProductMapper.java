package com.icommerce.shoppingapi.service.mapper;

import com.icommerce.shoppingapi.controller.dto.response.CartProductResponse;
import com.icommerce.shoppingapi.repository.model.CartProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {})
public interface CartProductMapper extends EntityMapper<CartProductResponse, CartProduct>{

    @Mappings({
            @Mapping(source = "entity.product.id", target = "productId" ),
            @Mapping(source = "entity.product.price", target = "price" )
    })
    CartProductResponse toDto(CartProduct entity);

    default CartProduct fromId(Long id) {
        if (id == null) {
            return null;
        }
        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(id);
        return cartProduct;
    }
}
