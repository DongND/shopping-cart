package com.icommerce.shoppingapi.service.impl;

import com.icommerce.shoppingapi.repository.model.Cart;
import com.icommerce.shoppingapi.repository.model.CartProduct;
import com.icommerce.shoppingapi.repository.model.Category;
import com.icommerce.shoppingapi.repository.model.Product;
import com.icommerce.shoppingapi.repository.CartProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.icommerce.shoppingapi.util.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartProductServiceTest {

    @InjectMocks
    private CartProductServiceImpl cartProductService;

    @Mock
    private CartProductRepository cartProductRepository;

    private CartProduct cartProduct;

    private Cart cart;

    private Product product;

    @BeforeEach
    public void initTest() {
        product = new Product(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, new Category());

        cart = new Cart(DEFAULT_ID, DEFAULT_DESCRIPTION, DEFAULT_ORDER_STATUS, DEFAULT_USER_PHONE, DEFAULT_USER_ADDRESS);
        cart.setCreatedAt(DEFAULT_CREATED_AT);
        cart.setLastModifiedAt(DEFAULT_LAST_MODIFIED_AT);

        cartProduct = new CartProduct(DEFAULT_ID, cart, product, DEFAULT_QUANTITY);
    }

    @Test
    public void givenValidCartProduct_whenCreate_thenSuccess(){
        Mockito.when(cartProductRepository.save(any(CartProduct.class))).thenReturn(cartProduct);
        cartProduct.setId(null);
        CartProduct actual = cartProductService.create(cartProduct);

        cartProduct.setId(DEFAULT_ID);
        assertThat(actual).isEqualTo(cartProduct);
        verify(cartProductRepository, times(1)).save(cartProduct);
    }

    @Test
    public void givenValidCart_whenFindByCart_thenSuccess(){
        List<CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(cartProduct);

        Mockito.when(cartProductRepository.findCartProductByCart(any(Cart.class))).thenReturn(cartProducts);
        List<CartProduct> actual = cartProductService.findByCart(cart);

        assertEquals(1, actual.size());
        assertThat(actual.get(0)).isEqualTo(cartProduct);

        verify(cartProductRepository, times(1)).findCartProductByCart(cart);
    }

    @Test
    public void givenValidId_whenDeleteById_thenSuccess(){
        doNothing().when(cartProductRepository).deleteById(anyLong());

        cartProductService.deleteById(DEFAULT_ID);

        verify(cartProductRepository, times(1)).deleteById(DEFAULT_ID);
    }

}
