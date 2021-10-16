package com.icommerce.shoppingapi.service.impl;

import com.icommerce.shoppingapi.controller.dto.request.AddProductCartRequestDTO;
import com.icommerce.shoppingapi.controller.dto.request.CartRequestDTO;
import com.icommerce.shoppingapi.controller.dto.response.CartCommonResponseDTO;
import com.icommerce.shoppingapi.exception.CartAlreadyConfirmedException;
import com.icommerce.shoppingapi.exception.ResourceNotFoundException;
import com.icommerce.shoppingapi.repository.CartRepository;
import com.icommerce.shoppingapi.repository.model.Cart;
import com.icommerce.shoppingapi.repository.model.CartProduct;
import com.icommerce.shoppingapi.repository.model.OrderStatus;
import com.icommerce.shoppingapi.repository.model.Product;
import com.icommerce.shoppingapi.service.CartProductService;
import com.icommerce.shoppingapi.service.CartService;
import com.icommerce.shoppingapi.service.ProductService;
import com.icommerce.shoppingapi.service.mapper.CartMapper;
import com.icommerce.shoppingapi.service.mapper.CartProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartProductService cartProductService;

    private final ProductService productService;

    private final CartMapper cartMapper;

    private final CartProductMapper cartProductMapper;

    @Override
    public CartCommonResponseDTO getCartById(Long id){
        Cart cart = this.cartRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Cart is not found"));
        List<CartProduct> cartProducts = cartProductService.findByCart(cart);
        CartCommonResponseDTO responseDTO =  cartMapper.toDto(cart);
        responseDTO.setProducts(cartProductMapper.toDtos(cartProducts));
        return responseDTO;
    }

    @Override
    public CartCommonResponseDTO create(CartRequestDTO cart) {
        List<AddProductCartRequestDTO> products = cart.getCartProducts();
        Map<Long, Product> validated = validateProductsExistence(products);
        Cart cartEntity = new Cart();
        cartEntity.setOrderStatus(OrderStatus.INITIALIZE.name());
        cartEntity.setDescription(cart.getDescription());
        cartEntity.setUserAddress(cart.getUserAddress());
        cartEntity.setUserPhone(cart.getUserPhone());
        cartEntity = this.cartRepository.save(cartEntity);

        List<CartProduct> cartProducts = new ArrayList<>();
        for (AddProductCartRequestDTO dto : products) {
            cartProducts.add(cartProductService.create(new CartProduct(null, cartEntity, validated.get(dto.getProductId()), dto.getQuantity())));
        }
        CartCommonResponseDTO responseDTO = cartMapper.toDto(cartEntity);
        responseDTO.setProducts(cartProductMapper.toDtos(cartProducts));

        return responseDTO;
    }

    @Override
    public CartCommonResponseDTO update(Long id, CartRequestDTO request) {
        List<AddProductCartRequestDTO> products = request.getCartProducts();
        Cart cart = this.cartRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Cart is not found"));
        if (cart.getOrderStatus() != OrderStatus.INITIALIZE.name()){
            throw new CartAlreadyConfirmedException("Can not update cart which in processing or processed");
        }

        Map<Long, Product> validated = validateProductsExistence(products);
        cart.setOrderStatus(request.getOrderStatus());
        cart.setDescription(request.getDescription());
        cart.setUserAddress(request.getUserAddress());
        cart.setUserPhone(request.getUserPhone());
        this.cartRepository.save(cart);

        Map<Long, CartProduct> existedProducts = cartProductService.findByCart(cart).stream()
                                                .collect(Collectors.toMap(e -> e.getProduct().getId(), Function.identity()));

        List<CartProduct> cartProductsAdded = new ArrayList<>();
        for (AddProductCartRequestDTO dto : products) {
            if (existedProducts.get(dto.getProductId()) != null){
                CartProduct existed = existedProducts.get(dto.getProductId());
                if (existed.getQuantity() != dto.getQuantity()){
                    existed.setQuantity(dto.getQuantity());
                    cartProductService.create(existed);
                }
                cartProductsAdded.add(existed);
                existedProducts.remove(dto.getProductId());
            } else {
                cartProductsAdded.add(cartProductService.create(new CartProduct(null, cart, validated.get(dto.getProductId()), dto.getQuantity())));
            }
        }
        if (!existedProducts.isEmpty()){
            for (Map.Entry<Long, CartProduct> entry : existedProducts.entrySet()) {
                this.cartProductService.deleteById(entry.getValue().getId());
            }
        }
        CartCommonResponseDTO responseDTO = cartMapper.toDto(cart);
        responseDTO.setProducts(cartProductMapper.toDtos(cartProductsAdded));
        return responseDTO;
    }

    private Map<Long, Product> validateProductsExistence(List<AddProductCartRequestDTO> orderProducts) {
        List<Product> products = productService.findProductByIds(orderProducts
                .stream().map(e -> e.getProductId()).collect(Collectors.toList()));

        if (orderProducts.size() != products.size()) {
            new ResourceNotFoundException("Product not found");
        }

        return products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
    }
}
