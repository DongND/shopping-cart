package com.icommerce.shoppingapi.controller;


import com.icommerce.shoppingapi.EcommerceApplication;
import com.icommerce.shoppingapi.repository.CategoryRepository;
import com.icommerce.shoppingapi.repository.ProductRepository;
import com.icommerce.shoppingapi.repository.model.Category;
import com.icommerce.shoppingapi.repository.model.Product;
import com.icommerce.shoppingapi.service.ProductService;
import com.icommerce.shoppingapi.service.QueryParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.icommerce.shoppingapi.util.Constant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = EcommerceApplication.class)
@AutoConfigureMockMvc
public class ProductControllerIT {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private QueryParserService queryParserService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Category category;

    private Product product;

    public Product createProductEntity(EntityManager em) {
        return new Product(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_PRICE, DEFAULT_BRAND, DEFAULT_COLOUR, DEFAULT_PICTURE_URL, DEFAULT_STOCK_QUANTITY, category );

    }


    public Category createCategoryEntity(EntityManager em){
        return new Category(DEFAULT_CATEGORY_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        category = createCategoryEntity(em);
        product = createProductEntity(em);
    }

    @Test
    @Transactional
    public void givenValidProductId_whenGetProductById_thenReturnSuccess() throws Exception {
        // Override the database initialized by Liquibase
        categoryRepository.saveAndFlush(category);
        productRepository.saveAndFlush(product);

        // Get the product
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
    @Transactional
    public void givenNotExistProductId_whenGetProductById_thenReturnNotFound() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", DEFAULT_ID + 100))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

    @Test
    @Transactional
    public void givenInValidProductId_whenGetProductById_thenReturnBadRequest() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", DEFAULT_ID - 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors[0].code").value("Invalid product ID."));
    }

    @Test
    @Transactional
    public void givenSearchCriteriaNothing_whenSearch_thenReturnAllProduct() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get the product list
        // Assert with first element in product.csv
        restProductMockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(7))

                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("TV Set"))
                .andExpect(jsonPath("$.content[0].description").value(""))
                .andExpect(jsonPath("$.content[0].price").value(300))
                .andExpect(jsonPath("$.content[0].brand").value("Fist Brand"))
                .andExpect(jsonPath("$.content[0].colour").value("Black"))
                .andExpect(jsonPath("$.content[0].pictureUrl").value("https://picsum.photos/200"))
                .andExpect(jsonPath("$.content[0].stockQuantity").value(100))
                .andExpect(jsonPath("$.content[0].categoryName").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void givenNameExactly_whenSearch_thenReturnOneProduct() throws Exception {
        // Override the database
        categoryRepository.saveAndFlush(category);

        String criteria = "name:Phone";
        // Get the product by product name
        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))

                .andExpect(jsonPath("$.content[0].id").value(6L))
                .andExpect(jsonPath("$.content[0].name").value("Phone"))
                .andExpect(jsonPath("$.content[0].description").value(""))
                .andExpect(jsonPath("$.content[0].price").value(200))
                .andExpect(jsonPath("$.content[0].brand").value("Second Brand"))
                .andExpect(jsonPath("$.content[0].colour").value("Silver"))
                .andExpect(jsonPath("$.content[0].pictureUrl").value("https://picsum.photos/200"))
                .andExpect(jsonPath("$.content[0].stockQuantity").value(45))
                .andExpect(jsonPath("$.content[0].categoryName").value("Technology"));
    }
    @Test
    @Transactional
    public void givenNameLike_whenSearch_thenReturnProductMatched() throws Exception {

        String criteria = "name:*Phone*";
        // Get the product list
        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].id").value(6L))
                .andExpect(jsonPath("$.content[0].categoryName").value("Technology"))
                .andExpect(jsonPath("$.content[1].id").value(7L))
                .andExpect(jsonPath("$.content[1].categoryName").value("Technology"));
    }

    @Test
    @Transactional
    public void givenNameNotEqual_whenSearch_thenReturnProductMatched() throws Exception {

        String criteria = "name!Phone";
        // Get the product list
        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(6));
    }

    @Test
    @Transactional
    public void givenCategoryName_whenSearch_thenReturnProductMatched() throws Exception {

        String criteria = "category:Technology";
        // Get the product list
        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @Transactional
    public void givenColour_whenSearch_thenReturnProductMatched() throws Exception {

        String criteria = "colour:*black*";
        // Get the product list
        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @Transactional
    public void givenBrand_whenSearch_thenReturnProductMatched() throws Exception {

        String criteria = "brand:*Second*";
        // Get the product list
        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(4));
    }

    @Test
    @Transactional
    public void givenPriceGreaterThan_whenSearch_thenReturnProductMatched() throws Exception {

        String criteria = "price>50";
        // Get the product list
        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(4));
    }

    @Test
    @Transactional
    public void givenPriceLessThan_whenSearch_thenReturnProductMatched() throws Exception {

        String criteria = "price<50";
        // Get the product list
        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    @Transactional
    public void givenPriceBetween_whenSearch_thenReturnProductMatched() throws Exception {

        String criteria = "price>15,price<150";
        // Get the product list
        restProductMockMvc.perform(get("/api/products?query={query}", criteria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @Transactional
    public void givenPaginationInformation_whenSearch_thenReturnProductMatched() throws Exception {

        // Get the product list
        restProductMockMvc.perform((get("/api/products")
                                        .param("page", "0")
                                        .param("size", "5")
                                        .param("sort", "id,desc")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(7))
                .andExpect(jsonPath("$.content[0].id").value(7L));

        restProductMockMvc.perform((get("/api/products")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "id,desc")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(7))
                .andExpect(jsonPath("$.content[0].id").value(2L));
    }
}
