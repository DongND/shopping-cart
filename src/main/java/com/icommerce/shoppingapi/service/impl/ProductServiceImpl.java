package com.icommerce.shoppingapi.service.impl;

import com.icommerce.shoppingapi.controller.dto.response.ProductResponseDTO;
import com.icommerce.shoppingapi.exception.ResourceNotFoundException;
import com.icommerce.shoppingapi.service.mapper.ProductMapper;
import com.icommerce.shoppingapi.repository.model.Product;
import com.icommerce.shoppingapi.repository.ProductRepository;
import com.icommerce.shoppingapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public List<Product> findProductByIds(List<Long> ids){
        return productRepository.findAllById(ids);
    }

    public Page<ProductResponseDTO> findByCriteria(Specification specs, Pageable page){
        return productRepository.findAll(specs, page).map(e -> productMapper.toDto((Product) e));
    }


    @Override
    public Product findProductById(long id) {
        return productRepository
          .findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}
