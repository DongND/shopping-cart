package com.icommerce.shoppingapi.service.mapper;

import com.icommerce.shoppingapi.controller.dto.response.CartProductResponse;
import com.icommerce.shoppingapi.repository.model.Cart;
import com.icommerce.shoppingapi.repository.model.CartProduct;
import com.icommerce.shoppingapi.repository.model.Category;
import com.icommerce.shoppingapi.repository.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.icommerce.shoppingapi.util.Constant.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CartProductMapperTest {

    private CartProductMapper cartProductMapper;

    private CartProductResponse dto;

    private CartProduct entity;

    @BeforeEach
    public void setUp() {
        cartProductMapper = new CartProductMapperImpl();
        dto = new CartProductResponse(DEFAULT_ID, DEFAULT_PRICE, DEFAULT_QUANTITY, DEFAULT_PRICE * DEFAULT_QUANTITY);

        Cart cart = new Cart(DEFAULT_ID, DEFAULT_DESCRIPTION, DEFAULT_ORDER_STATUS, DEFAULT_USER_PHONE, DEFAULT_USER_ADDRESS);
        Category category = new Category(DEFAULT_CATEGORY_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION);
        Product product = new Product(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, category );

        entity = new CartProduct(DEFAULT_ID, cart, product, DEFAULT_QUANTITY);
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(cartProductMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(cartProductMapper.fromId(null)).isNull();
    }


    @Test
    public void testToDto() {
        assertThat(cartProductMapper.toDto(entity)).isEqualTo(dto);
        entity = null;
        assertThat(cartProductMapper.toDto(entity)).isNull();
    }
    @Test
    public void testToDtos() {
        List<CartProduct> entites = new ArrayList<>();
        entites.add(entity);
        List<CartProductResponse> dtos = new ArrayList<>();
        dtos.add(dto);
        assertThat(cartProductMapper.toDtos(entites)).isEqualTo(dtos);

        entites = null;
        assertThat(cartProductMapper.toDtos(entites)).isNull();
    }
}
