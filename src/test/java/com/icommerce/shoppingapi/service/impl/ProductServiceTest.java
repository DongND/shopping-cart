package com.icommerce.shoppingapi.service.impl;

import com.icommerce.shoppingapi.controller.dto.response.ProductResponseDTO;
import com.icommerce.shoppingapi.exception.ResourceNotFoundException;
import com.icommerce.shoppingapi.repository.model.Category;
import com.icommerce.shoppingapi.repository.model.Product;
import com.icommerce.shoppingapi.repository.ProductRepository;
import com.icommerce.shoppingapi.service.mapper.ProductMapper;
import com.icommerce.shoppingapi.service.search.GenericSpecification;
import com.icommerce.shoppingapi.service.search.SearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.icommerce.shoppingapi.util.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    private Category category;

    private Product product;

    private ProductResponseDTO productResponseDTO;

    @BeforeEach
    public void initTest() {
        category = new Category(DEFAULT_CATEGORY_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION);
        product = new Product(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, category );
        product.setCreatedAt(DEFAULT_CREATED_AT);
        product.setLastModifiedAt(DEFAULT_LAST_MODIFIED_AT);
        productResponseDTO = new ProductResponseDTO(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, DEFAULT_NAME );
    }

    @Test
    public void givenExistedProductId_whenFindProductById_thenSuccess(){
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product actual = productService.findProductById(DEFAULT_ID);

        assertThat(actual).isEqualTo(product);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void givenDoesNotExistedProductId_whenFindProductById_thenThrowResourceNotFoundException(){
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> productService.findProductById(DEFAULT_ID));
        assertEquals("Product not found", ex.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void givenExistedListProductId_whenFindProductByIds_thenSuccess(){
        List<Product> products = new ArrayList<>();
        products.add(product);
        List<Long> input = products.stream().map(Product::getId).collect(Collectors.toList());

        Mockito.when(productRepository.findAllById(anyList())).thenReturn(products);
        List<Product> actual = productService.findProductByIds(input);

        assertEquals(1, products.size());
        assertThat(actual.get(0)).isEqualTo(products.get(0));
        verify(productRepository, times(1)).findAllById(input);
    }

    @Test
    public void givenValidSpecificationAndPageable_whenFindByCriteria_thenSuccess(){
        List<Product> content = new ArrayList<>();
        content.add(product);
        Page<Product> pageProducts = new PageImpl<>(content);

        SearchCriteria searchCriteria = new SearchCriteria();
        Pageable pageable = PageRequest.of(0, 4);
        Specification<Product> specification = new GenericSpecification<Product>(searchCriteria);
        Mockito.when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageProducts);
        Mockito.when(productMapper.toDto(any(Product.class))).thenReturn(productResponseDTO);

        Page<ProductResponseDTO> actual = productService.findByCriteria(specification, pageable);
        ProductResponseDTO actualResponse = actual.getContent().get(0);

        assertEquals(1, actual.getTotalElements());
        assertEquals(product.getId(), actualResponse.getId());
        assertEquals(product.getName(), actualResponse.getName());
        assertEquals(product.getDescription(), actualResponse.getDescription());
        assertEquals(product.getPrice(), actualResponse.getPrice());
        assertEquals(product.getBrand(), actualResponse.getBrand());
        assertEquals(product.getColour(), actualResponse.getColour());
        assertEquals(product.getPictureUrl(), actualResponse.getPictureUrl());
        assertEquals(product.getStockQuantity(), actualResponse.getStockQuantity());
        assertEquals(product.getCategory().getName(), actualResponse.getCategoryName());

        verify(productRepository, times(1)).findAll(specification, pageable);
    }
}
