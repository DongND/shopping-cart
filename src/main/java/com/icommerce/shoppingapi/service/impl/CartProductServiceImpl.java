package com.icommerce.shoppingapi.service.impl;

import com.icommerce.shoppingapi.repository.model.Cart;
import com.icommerce.shoppingapi.repository.model.CartProduct;
import com.icommerce.shoppingapi.repository.CartProductRepository;
import com.icommerce.shoppingapi.service.CartProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartProductServiceImpl implements CartProductService {

    private final CartProductRepository cartProductRepository;

    @Override
    public CartProduct create(CartProduct cartProduct) {
        return this.cartProductRepository.save(cartProduct);
    }

    @Override
    public List<CartProduct> findByCart(Cart cart){
        return this.cartProductRepository.findCartProductByCart(cart);
    }

    public void deleteById(Long id){
        this.cartProductRepository.deleteById(id);
    }
}
