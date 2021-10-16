package com.icommerce.shoppingapi.controller;

import com.icommerce.shoppingapi.controller.dto.response.ProductResponseDTO;
import com.icommerce.shoppingapi.repository.model.Category;
import com.icommerce.shoppingapi.repository.model.Product;
import com.icommerce.shoppingapi.service.ProductService;
import com.icommerce.shoppingapi.service.QueryParserService;
import com.icommerce.shoppingapi.service.search.GenericSpecification;
import com.icommerce.shoppingapi.service.search.SearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.icommerce.shoppingapi.util.Constant.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProductController} REST controller.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @MockBean
    ProductService productService;

    @MockBean
    QueryParserService queryParserService;

    @Autowired
    private MockMvc restProductMockMvc;

    private Category category;

    private Product product;

    private ProductResponseDTO productResponseDTO;

    private List<ProductResponseDTO> productResponseDTOs =  new ArrayList<>();

    private Specification<Product> specification;

    @BeforeEach
    public void initTest() {
        category = new Category(DEFAULT_CATEGORY_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION);
        product = new Product(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, category );
        product.setCreatedAt(DEFAULT_CREATED_AT);
        product.setLastModifiedAt(DEFAULT_LAST_MODIFIED_AT);

        productResponseDTO = new ProductResponseDTO(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, DEFAULT_CATEGORY_NAME );
        productResponseDTOs.add(productResponseDTO);

        SearchCriteria searchCriteria = new SearchCriteria();
        specification = new GenericSpecification<>(searchCriteria);
    }

    @Test
    public void givenValidId_whenGetProductById_shouldSuccess() throws Exception {

        when(productService.findProductById(anyLong())).thenReturn(product);

        restProductMockMvc.perform(get("/api/products/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(product.getId().longValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
                .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
                .andExpect(jsonPath("$.colour").value(DEFAULT_COLOUR))
                .andExpect(jsonPath("$.pictureUrl").value(DEFAULT_PICTURE_URL))
                .andExpect(jsonPath("$.stockQuantity").value(DEFAULT_STOCK_QUANTITY.longValue()))
                .andExpect(jsonPath("$.category.name").value(DEFAULT_NAME));
    }

    @Test
    public void givenNameExactlySearchCriteria_whenSearch_shouldSuccess() throws Exception {

        String criteria = "name:" + DEFAULT_NAME;
        Page<ProductResponseDTO> expected = new PageImpl<>(productResponseDTOs);
        when(productService.findByCriteria(any(GenericSpecification.class), any(Pageable.class) )).thenReturn(expected);
        when(queryParserService.getProductSpec(anyString())).thenReturn(specification);
        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(product.getId().longValue()))
                .andExpect(jsonPath("$.content[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.content[0].description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.content[0].price").value(DEFAULT_PRICE.doubleValue()))
                .andExpect(jsonPath("$.content[0].brand").value(DEFAULT_BRAND))
                .andExpect(jsonPath("$.content[0].colour").value(DEFAULT_COLOUR))
                .andExpect(jsonPath("$.content[0].pictureUrl").value(DEFAULT_PICTURE_URL))
                .andExpect(jsonPath("$.content[0].stockQuantity").value(DEFAULT_STOCK_QUANTITY.longValue()))
                .andExpect(jsonPath("$.content[0].categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    public void givenNameLikeSearchCriteria_whenSearch_shouldSuccess() throws Exception {

        String criteria = "name:*" + NAME_SEARCH_LIKE + "*";
        Page<ProductResponseDTO> expected = new PageImpl<>(productResponseDTOs);
        when(productService.findByCriteria(any(GenericSpecification.class), any(Pageable.class) )).thenReturn(expected);
        when(queryParserService.getProductSpec(anyString())).thenReturn(specification);

        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(product.getId().longValue()))
                .andExpect(jsonPath("$.content[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.content[0].description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.content[0].price").value(DEFAULT_PRICE.doubleValue()))
                .andExpect(jsonPath("$.content[0].brand").value(DEFAULT_BRAND))
                .andExpect(jsonPath("$.content[0].colour").value(DEFAULT_COLOUR))
                .andExpect(jsonPath("$.content[0].pictureUrl").value(DEFAULT_PICTURE_URL))
                .andExpect(jsonPath("$.content[0].stockQuantity").value(DEFAULT_STOCK_QUANTITY.longValue()))
                .andExpect(jsonPath("$.content[0].categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    public void givenNameNegation_whenSearch_shouldSuccess() throws Exception {

        String criteria = "name!" + NAME_SEARCH_LIKE;
        Page<ProductResponseDTO> expected = new PageImpl<>(new ArrayList<>());
        when(productService.findByCriteria(any(GenericSpecification.class), any(Pageable.class) )).thenReturn(expected);
        when(queryParserService.getProductSpec(anyString())).thenReturn(specification);

        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    public void givenCategoryName_whenSearch_shouldSuccess() throws Exception {

        String criteria = "category:"+ DEFAULT_CATEGORY_NAME;
        Page<ProductResponseDTO> expected = new PageImpl<>(productResponseDTOs);
        when(productService.findByCriteria(any(GenericSpecification.class), any(Pageable.class) )).thenReturn(expected);
        when(queryParserService.getProductSpec(anyString())).thenReturn(specification);

        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(product.getId().longValue()))
                .andExpect(jsonPath("$.content[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.content[0].description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.content[0].price").value(DEFAULT_PRICE.doubleValue()))
                .andExpect(jsonPath("$.content[0].brand").value(DEFAULT_BRAND))
                .andExpect(jsonPath("$.content[0].colour").value(DEFAULT_COLOUR))
                .andExpect(jsonPath("$.content[0].pictureUrl").value(DEFAULT_PICTURE_URL))
                .andExpect(jsonPath("$.content[0].stockQuantity").value(DEFAULT_STOCK_QUANTITY.longValue()))
                .andExpect(jsonPath("$.content[0].categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    public void givenBrand_whenSearch_shouldSuccess() throws Exception {

        String criteria = "brand:" + DEFAULT_BRAND;
        Page<ProductResponseDTO> expected = new PageImpl<>(productResponseDTOs);
        when(productService.findByCriteria(any(GenericSpecification.class), any(Pageable.class) )).thenReturn(expected);
        when(queryParserService.getProductSpec(anyString())).thenReturn(specification);

        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(product.getId().longValue()))
                .andExpect(jsonPath("$.content[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.content[0].description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.content[0].price").value(DEFAULT_PRICE.doubleValue()))
                .andExpect(jsonPath("$.content[0].brand").value(DEFAULT_BRAND))
                .andExpect(jsonPath("$.content[0].colour").value(DEFAULT_COLOUR))
                .andExpect(jsonPath("$.content[0].pictureUrl").value(DEFAULT_PICTURE_URL))
                .andExpect(jsonPath("$.content[0].stockQuantity").value(DEFAULT_STOCK_QUANTITY.longValue()))
                .andExpect(jsonPath("$.content[0].categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    public void givenColour_whenSearch_shouldSuccess() throws Exception {

        String criteria = "colour:" + DEFAULT_COLOUR;
        Page<ProductResponseDTO> expected = new PageImpl<>(productResponseDTOs);
        when(productService.findByCriteria(any(GenericSpecification.class), any(Pageable.class) )).thenReturn(expected);
        when(queryParserService.getProductSpec(anyString())).thenReturn(specification);

        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(product.getId().longValue()))
                .andExpect(jsonPath("$.content[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.content[0].description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.content[0].price").value(DEFAULT_PRICE.doubleValue()))
                .andExpect(jsonPath("$.content[0].brand").value(DEFAULT_BRAND))
                .andExpect(jsonPath("$.content[0].colour").value(DEFAULT_COLOUR))
                .andExpect(jsonPath("$.content[0].pictureUrl").value(DEFAULT_PICTURE_URL))
                .andExpect(jsonPath("$.content[0].stockQuantity").value(DEFAULT_STOCK_QUANTITY.longValue()))
                .andExpect(jsonPath("$.content[0].categoryName").value(DEFAULT_CATEGORY_NAME));
    }
}
