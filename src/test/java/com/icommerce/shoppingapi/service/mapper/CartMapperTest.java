package com.icommerce.shoppingapi.service.mapper;

import com.icommerce.shoppingapi.controller.dto.response.CartCommonResponseDTO;
import com.icommerce.shoppingapi.controller.dto.response.CartProductResponse;
import com.icommerce.shoppingapi.repository.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.icommerce.shoppingapi.util.Constant.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CartMapperTest {

    private CartMapper cartMapper;

    private Cart entity;

    private CartCommonResponseDTO dto;

    @BeforeEach
    public void setUp() {
        cartMapper = new CartMapperImpl();
        entity = new Cart(DEFAULT_ID, DEFAULT_DESCRIPTION, DEFAULT_ORDER_STATUS, DEFAULT_USER_PHONE, DEFAULT_USER_ADDRESS);
        List<CartProductResponse> cartProductResponses = new ArrayList<>();

        CartProductResponse cartProductResponse = new CartProductResponse(DEFAULT_ID, DEFAULT_PRICE, DEFAULT_QUANTITY, 0D);
        cartProductResponses.add(cartProductResponse);
        dto = new CartCommonResponseDTO(DEFAULT_ID, DEFAULT_DESCRIPTION, DEFAULT_ORDER_STATUS, DEFAULT_USER_PHONE, DEFAULT_USER_ADDRESS, DEFAULT_CREATED_AT, DEFAULT_LAST_MODIFIED_AT, cartProductResponses);

    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(cartMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(cartMapper.fromId(null)).isNull();
    }
    @Test
    public void testToEntity() {

        entityAssertion(cartMapper.toEntity(dto), entity);
        dto = null;
        assertThat(cartMapper.toEntity(dto)).isNull();
    }

    @Test
    public void testToEntities() {
        List<CartCommonResponseDTO> dtos = new ArrayList<>();
        dtos.add(dto);
        List<Cart> entities = new ArrayList<>();
        entities.add(entity);
        entitiesAssertion(cartMapper.toEntity(dtos), entities);
        dtos = null;
        assertThat(cartMapper.toEntity(dtos)).isNull();
    }

    @Test
    public void testToDto() {

        dtoAssertion(cartMapper.toDto(entity), dto);
        entity = null;
        assertThat(cartMapper.toDto(entity)).isNull();
    }

    @Test
    public void testToDtos() {
        List<CartCommonResponseDTO> dtos = new ArrayList<>();
        dtos.add(dto);
        List<Cart> entities = new ArrayList<>();
        entities.add(entity);
        dtosAssertion(cartMapper.toDtos(entities), dtos);
        entities = null;
        assertThat(cartMapper.toDtos(entities)).isNull();
    }

    private void dtoAssertion(CartCommonResponseDTO actual, CartCommonResponseDTO expected){
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
        assertThat(actual.getUserPhone()).isEqualTo(expected.getUserPhone());
        assertThat(actual.getUserAddress()).isEqualTo(expected.getUserAddress());
    }

    private void dtosAssertion(List<CartCommonResponseDTO> actuals, List<CartCommonResponseDTO> expecteds){
        assertThat(actuals.size()).isEqualTo(expecteds.size());
        if (actuals.size() > 0){
            for (int i=0; i<actuals.size(); i++){
                dtoAssertion(actuals.get(i), expecteds.get(i));
            }
        }
    }

    private void entityAssertion(Cart actual, Cart expected){
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
        assertThat(actual.getUserPhone()).isEqualTo(expected.getUserPhone());
        assertThat(actual.getUserAddress()).isEqualTo(expected.getUserAddress());
    }

    private void entitiesAssertion(List<Cart> actuals, List<Cart> expecteds){
        assertThat(actuals.size()).isEqualTo(expecteds.size());
        if (actuals.size() > 0){
            for (int i=0; i<actuals.size(); i++){
                entityAssertion(actuals.get(i), expecteds.get(i));
            }
        }
    }
}
