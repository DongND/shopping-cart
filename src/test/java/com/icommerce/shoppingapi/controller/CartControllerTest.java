package com.icommerce.shoppingapi.controller;

import com.icommerce.shoppingapi.controller.dto.request.AddProductCartRequestDTO;
import com.icommerce.shoppingapi.controller.dto.request.CartRequestDTO;
import com.icommerce.shoppingapi.controller.dto.response.CartCommonResponseDTO;
import com.icommerce.shoppingapi.controller.dto.response.CartProductResponse;
import com.icommerce.shoppingapi.repository.model.Category;
import com.icommerce.shoppingapi.repository.model.OrderStatus;
import com.icommerce.shoppingapi.repository.model.Product;
import com.icommerce.shoppingapi.service.CartService;
import com.icommerce.shoppingapi.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.icommerce.shoppingapi.util.Constant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CartController} REST controller.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(CartController.class)
public class CartControllerTest {

    @MockBean
    CartService cartService;

    @Autowired
    private MockMvc restCartMockMvc;

    private Category category;

    private Product product;

    private CartCommonResponseDTO cartCommonResponseDTO;

    private CartRequestDTO requestDTO;

    @BeforeEach
    public void initTest() {
        category = new Category(DEFAULT_CATEGORY_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION);
        product = new Product(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, category );
        product.setCreatedAt(DEFAULT_CREATED_AT);
        product.setLastModifiedAt(DEFAULT_LAST_MODIFIED_AT);

        List<CartProductResponse> cartProductResponses = new ArrayList<>();
        CartProductResponse cartProductResponse = new CartProductResponse(DEFAULT_ID, DEFAULT_PRICE, DEFAULT_QUANTITY, 0D);
        cartProductResponses.add(cartProductResponse);
        cartCommonResponseDTO = new CartCommonResponseDTO(DEFAULT_ID, DEFAULT_DESCRIPTION, DEFAULT_ORDER_STATUS, DEFAULT_USER_PHONE, DEFAULT_USER_ADDRESS,  DEFAULT_CREATED_AT, DEFAULT_LAST_MODIFIED_AT, cartProductResponses);

        List<AddProductCartRequestDTO> addProductCartRequestDTOs = new ArrayList<>();
        addProductCartRequestDTOs.add(new AddProductCartRequestDTO(DEFAULT_ID, DEFAULT_QUANTITY));
        requestDTO = new CartRequestDTO(addProductCartRequestDTOs, DEFAULT_DESCRIPTION, DEFAULT_USER_PHONE, DEFAULT_USER_ADDRESS, OrderStatus.INITIALIZE.name());
    }

    @Test
    public void givenValidCartId_whenGetById_shouldSuccess() throws Exception {

        Mockito.when(cartService.getCartById(anyLong())).thenReturn(cartCommonResponseDTO);

        restCartMockMvc.perform(get("/api/carts/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(product.getId().longValue()))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS))
                .andExpect(jsonPath("$.userPhone").value(DEFAULT_USER_PHONE))
                .andExpect(jsonPath("$.userAddress").value(DEFAULT_USER_ADDRESS))
                .andExpect(jsonPath("$.products[0].productId").value(DEFAULT_ID))
                .andExpect(jsonPath("$.products[0].quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    public void givenValidCartRequest_whenCreate_shouldSuccess() throws Exception {

        Mockito.when(cartService.create(any(CartRequestDTO.class))).thenReturn(cartCommonResponseDTO);

        restCartMockMvc.perform(post("/api/carts")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(TestUtil.convertObjectToJsonBytes(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(product.getId().longValue()))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS))
                .andExpect(jsonPath("$.userPhone").value(DEFAULT_USER_PHONE))
                .andExpect(jsonPath("$.userAddress").value(DEFAULT_USER_ADDRESS))
                .andExpect(jsonPath("$.products[0].productId").value(DEFAULT_ID))
                .andExpect(jsonPath("$.products[0].quantity").value(DEFAULT_QUANTITY));
    }
    @Test
    public void givenValidCartRequest_whenUpdate_shouldSuccess() throws Exception {

        List<AddProductCartRequestDTO> addProductCartRequestDTOs = new ArrayList<>();
        addProductCartRequestDTOs.add(new AddProductCartRequestDTO(DEFAULT_ID, DEFAULT_QUANTITY + 10));
        requestDTO = new CartRequestDTO(addProductCartRequestDTOs, DEFAULT_DESCRIPTION, DEFAULT_USER_PHONE, DEFAULT_USER_ADDRESS, OrderStatus.INITIALIZE.name());

        List<CartProductResponse> cartProductResponses = new ArrayList<>();
        CartProductResponse cartProductResponse = new CartProductResponse(DEFAULT_ID, DEFAULT_PRICE, DEFAULT_QUANTITY +10, 0D);
        cartProductResponses.add(cartProductResponse);
        cartCommonResponseDTO = new CartCommonResponseDTO(DEFAULT_ID, DEFAULT_DESCRIPTION, DEFAULT_ORDER_STATUS, DEFAULT_USER_PHONE, DEFAULT_USER_ADDRESS,  DEFAULT_CREATED_AT, DEFAULT_LAST_MODIFIED_AT, cartProductResponses);


        Mockito.when(cartService.update(anyLong(),any(CartRequestDTO.class))).thenReturn(cartCommonResponseDTO);

        restCartMockMvc.perform(put("/api/carts/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(product.getId().longValue()))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS))
                .andExpect(jsonPath("$.userPhone").value(DEFAULT_USER_PHONE))
                .andExpect(jsonPath("$.userAddress").value(DEFAULT_USER_ADDRESS))
                .andExpect(jsonPath("$.products[0].productId").value(DEFAULT_ID))
                .andExpect(jsonPath("$.products[0].quantity").value(DEFAULT_QUANTITY + 10));
    }

}
