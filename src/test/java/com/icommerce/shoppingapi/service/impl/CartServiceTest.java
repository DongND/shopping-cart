package com.icommerce.shoppingapi.service.impl;

import com.icommerce.shoppingapi.controller.dto.request.AddProductCartRequestDTO;
import com.icommerce.shoppingapi.controller.dto.request.CartRequestDTO;
import com.icommerce.shoppingapi.controller.dto.response.CartCommonResponseDTO;
import com.icommerce.shoppingapi.controller.dto.response.CartProductResponse;
import com.icommerce.shoppingapi.exception.CartAlreadyConfirmedException;
import com.icommerce.shoppingapi.exception.ResourceNotFoundException;
import com.icommerce.shoppingapi.repository.model.*;
import com.icommerce.shoppingapi.repository.CartRepository;
import com.icommerce.shoppingapi.service.CartProductService;
import com.icommerce.shoppingapi.service.ProductService;
import com.icommerce.shoppingapi.service.mapper.CartMapper;
import com.icommerce.shoppingapi.service.mapper.CartProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.icommerce.shoppingapi.util.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartProductService cartProductService;

    @Mock
    private ProductService productService;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private CartProductMapper cartProductMapper;

    private Category category;

    private Product product;

    private CartProduct cartProduct;

    private Cart cart;

    private CartProductResponse cartProductResponse;

    private CartCommonResponseDTO cartCommonResponseDTO;

    private CartRequestDTO requestDTO;

    List<CartProductResponse> cartProductResponses = new ArrayList<>();

    List<AddProductCartRequestDTO> addProductCartRequestDTOs = new ArrayList<>();

    List<CartProduct> cartProducts = new ArrayList<>();

    @BeforeEach
    public void initTest() {
        category = new Category(DEFAULT_CATEGORY_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION);

        product = new Product(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, category );
        product.setCreatedAt(DEFAULT_CREATED_AT);
        product.setLastModifiedAt(DEFAULT_LAST_MODIFIED_AT);

        cart = new Cart(DEFAULT_ID, DEFAULT_DESCRIPTION, DEFAULT_ORDER_STATUS, DEFAULT_USER_PHONE, DEFAULT_USER_ADDRESS);

        cartProductResponse = new CartProductResponse(DEFAULT_ID, DEFAULT_PRICE, DEFAULT_QUANTITY, 0D);
        cartProductResponses.add(cartProductResponse);
        cartCommonResponseDTO = new CartCommonResponseDTO(DEFAULT_ID, DEFAULT_DESCRIPTION, DEFAULT_ORDER_STATUS, DEFAULT_USER_PHONE, DEFAULT_USER_ADDRESS, DEFAULT_CREATED_AT, DEFAULT_LAST_MODIFIED_AT, cartProductResponses);

        AddProductCartRequestDTO addProductCartRequestDTO = new AddProductCartRequestDTO(DEFAULT_ID, DEFAULT_QUANTITY);
        addProductCartRequestDTOs.add(addProductCartRequestDTO);
        requestDTO = new CartRequestDTO(addProductCartRequestDTOs, DEFAULT_DESCRIPTION, DEFAULT_USER_PHONE, DEFAULT_USER_ADDRESS, OrderStatus.INITIALIZE.name());

        cartProduct = new CartProduct(DEFAULT_ID, cart, product, DEFAULT_QUANTITY);
        cartProducts.add(cartProduct);
    }

    @Test
    public void givenExistedCartId_whenGetCartById_thenSuccess(){
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(cartMapper.toDto(any(Cart.class))).thenReturn(cartCommonResponseDTO);

        CartCommonResponseDTO actual = cartService.getCartById(DEFAULT_ID);

        assertThat(actual).isEqualTo(cartCommonResponseDTO);
        verify(cartRepository, times(1)).findById(DEFAULT_ID);
    }

    @Test
    public void givenExistedCartId_whenGetCartById_thenThrowResourceNotFoundException(){
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> cartService.getCartById(DEFAULT_ID));
        assertEquals("Cart is not found", ex.getMessage());
        verify(cartRepository, times(1)).findById(DEFAULT_ID);
    }

    @Test
    public void givenValidCartRequest_whenCreate_thenSuccess(){

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartProductService.create(any(CartProduct.class))).thenReturn(cartProduct);
        when(cartMapper.toDto(any(Cart.class))).thenReturn(cartCommonResponseDTO);
        when(cartProductMapper.toDtos(anyList())).thenReturn(cartProductResponses);
        cart.setId(null);
        CartCommonResponseDTO actual = cartService.create(requestDTO);

        assertThat(actual).isEqualTo(cartCommonResponseDTO);
        verify(cartRepository, times(1)).save(cart);
    }


    @Test
    public void givenValidCartRequest_whenUpdate_thenSuccess(){

        when(cartRepository.findById(any(Long.class))).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartProductService.create(any(CartProduct.class))).thenReturn(cartProduct);
        when(cartMapper.toDto(any(Cart.class))).thenReturn(cartCommonResponseDTO);
        when(cartProductMapper.toDtos(anyList())).thenReturn(cartProductResponses);

        CartCommonResponseDTO actual = cartService.update(cart.getId(), requestDTO);

        assertThat(actual).isEqualTo(cartCommonResponseDTO);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void givenValidCartRequestWithNewQuantity_whenUpdate_thenSuccess(){

        List<CartProduct> existedCartProducts = new ArrayList<>();
        existedCartProducts.add(new CartProduct(DEFAULT_ID, cart, product, DEFAULT_QUANTITY +1));

        when(cartRepository.findById(any(Long.class))).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartProductService.findByCart(any(Cart.class))).thenReturn(existedCartProducts);
        when(cartProductService.create(any(CartProduct.class))).thenReturn(cartProduct);

        when(cartMapper.toDto(any(Cart.class))).thenReturn(cartCommonResponseDTO);
        when(cartProductMapper.toDtos(anyList())).thenReturn(cartProductResponses);

        CartCommonResponseDTO actual = cartService.update(cart.getId(), requestDTO);

        assertThat(actual).isEqualTo(cartCommonResponseDTO);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void givenValidCartRequestWithNewProduct_whenUpdate_thenSuccess(){
        Product otherProduct1 = new Product(DEFAULT_ID +1, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, category );
        Product otherProduct2 = new Product(DEFAULT_ID +2, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, category );
        List<CartProduct> existedCartProducts = new ArrayList<>();
        existedCartProducts.add(new CartProduct(DEFAULT_ID + 1, cart, otherProduct1, DEFAULT_QUANTITY +1));
        existedCartProducts.add(new CartProduct(DEFAULT_ID + 2, cart, otherProduct2, DEFAULT_QUANTITY +2));

        addProductCartRequestDTOs.add(new AddProductCartRequestDTO(DEFAULT_ID +1 , DEFAULT_QUANTITY +10));
        requestDTO.setCartProducts(addProductCartRequestDTOs);

        cartProductResponses.add(new CartProductResponse(DEFAULT_ID+1, DEFAULT_PRICE, DEFAULT_QUANTITY+10, 0D));
        cartCommonResponseDTO.setProducts(cartProductResponses);


        when(cartRepository.findById(any(Long.class))).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartProductService.findByCart(any(Cart.class))).thenReturn(existedCartProducts);
        when(cartProductService.create(any(CartProduct.class))).thenReturn(cartProduct);

        when(cartMapper.toDto(any(Cart.class))).thenReturn(cartCommonResponseDTO);
        when(cartProductMapper.toDtos(anyList())).thenReturn(cartProductResponses);
        doNothing().when(cartProductService).deleteById(anyLong());

        CartCommonResponseDTO actual = cartService.update(cart.getId(), requestDTO);

        assertThat(actual.getProducts().size()).isEqualTo(cartCommonResponseDTO.getProducts().size());
        assertThat(actual.getTotalCartPrice()).isEqualTo(cartCommonResponseDTO.getTotalCartPrice());
        List<Long> newIds = actual.getProducts().stream().map(CartProductResponse::getProductId).collect(Collectors.toList());
        assertThat(newIds.contains(DEFAULT_ID));
        assertThat(newIds.contains(DEFAULT_ID +1));
        assertThat(!newIds.contains(DEFAULT_ID +2));
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void givenDoesNotExistedCart_whenUpdate_thenThrowResourceNotFoundException(){

        cart.setOrderStatus(OrderStatus.ORDERED.name());
        when(cartRepository.findById(any(Long.class))).thenReturn(Optional.of(cart));

        Exception ex = assertThrows(CartAlreadyConfirmedException.class, () -> cartService.update(DEFAULT_ID, requestDTO));
        assertEquals("Can not update cart which in processing or processed", ex.getMessage());
        verify(cartRepository, times(1)).findById(DEFAULT_ID);

    }

    @Test
    public void givenExistedCartIdWithOrderStatusNotInitialize_whenGetCartById_thenThrowResourceNotFoundException(){
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> cartService.getCartById(DEFAULT_ID));
        assertEquals("Cart is not found", ex.getMessage());
        verify(cartRepository, times(1)).findById(DEFAULT_ID);
    }

}
