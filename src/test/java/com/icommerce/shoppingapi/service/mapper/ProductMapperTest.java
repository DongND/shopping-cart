package com.icommerce.shoppingapi.service.mapper;

import com.icommerce.shoppingapi.controller.dto.response.ProductResponseDTO;
import com.icommerce.shoppingapi.repository.model.Category;
import com.icommerce.shoppingapi.repository.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.icommerce.shoppingapi.util.Constant.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductMapperTest {

    private ProductMapper productMapper;

    private ProductResponseDTO dto;

    private Product entity;

    @BeforeEach
    public void setUp() {
        productMapper = new ProductMapperImpl();
        Category category = new Category(DEFAULT_CATEGORY_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION);
        entity = new Product(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, category );
        dto = new ProductResponseDTO(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, DEFAULT_NAME );
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(productMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(productMapper.fromId(null)).isNull();
    }

    @Test
    public void testToDto() {
        assertThat(productMapper.toDto(entity)).isEqualTo(dto);
        entity = null;
        assertThat(productMapper.toDto(entity)).isNull();
    }
    @Test
    public void testToDtos() {
        List<Product> entites = new ArrayList<>();
        entites.add(entity);
        List<ProductResponseDTO> dtos = new ArrayList<>();
        dtos.add(dto);
        assertThat(productMapper.toDtos(entites)).isEqualTo(dtos);

        entites = null;
        assertThat(productMapper.toDtos(entites)).isNull();
    }

    @Test
    public void testToEntity() {
        entityAssertion(productMapper.toEntity(dto),entity);
        dto = null;
        assertThat(productMapper.toEntity(dto)).isNull();
    }

    @Test
    public void testToEntities() {

        List<Product> entities = new ArrayList<>();
        entities.add(entity);
        List<ProductResponseDTO> dtos = new ArrayList<>();
        dtos.add(dto);

        entitiesAssertion(productMapper.toEntity(dtos),entities);
        dto = null;
        assertThat(productMapper.toEntity(dto)).isNull();
    }

    private void entityAssertion(Product actual, Product expected){
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getBrand()).isEqualTo(expected.getBrand());
        assertThat(actual.getColour()).isEqualTo(expected.getColour());

        assertThat(actual.getPictureUrl()).isEqualTo(expected.getPictureUrl());
        assertThat(actual.getStockQuantity()).isEqualTo(expected.getStockQuantity());

    }

    private void entitiesAssertion(List<Product> actual, List<Product> expected){
        assertThat(actual.size()).isEqualTo(expected.size());
        if (actual.size() > 0){
            for (int i = 0; i < actual.size(); i++) {
                entityAssertion(actual.get(i), expected.get(i));
            }
        }
    }
}
