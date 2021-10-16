package com.icommerce.shoppingapi.service.search;

import com.icommerce.shoppingapi.EcommerceApplication;
import com.icommerce.shoppingapi.repository.ProductRepository;
import com.icommerce.shoppingapi.repository.model.Product;
import com.icommerce.shoppingapi.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = EcommerceApplication.class)
@AutoConfigureMockMvc
public class SearchCriteriaTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private EntityManager em;

    @Test
    public void givenExactlyName_whenSearch_thenCorrect() {
        Product product = productRepository.findById(6L).get();
        SpecificationBuilder builder = new SpecificationBuilder();
        builder.with("name", ":", "Phone","", "");
        List<Product> results = productRepository.findAll(builder.build());

        assertThat(results.size()).isEqualTo(1);
        assertThat(product).isEqualTo(results.get(0));
    }

    @Test
    public void givenNameLike_whenSearch_thenCorrect() {
        SpecificationBuilder builder = new SpecificationBuilder();
        builder.with("name", ":", "Phone","*", "*");
        List<Product> results = productRepository.findAll(builder.build());

        assertThat(results.size()).isEqualTo(2);
    }
    @Test
    public void givenNameStartWith_whenSearch_thenCorrect() {
        SpecificationBuilder builder = new SpecificationBuilder();
        builder.with("name", ":", "Phone","", "*");
        List<Product> results = productRepository.findAll(builder.build());

        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    public void givenNamEndWith_whenSearch_thenCorrect() {
        SpecificationBuilder builder = new SpecificationBuilder();
        builder.with("name", ":", "Phone","*", "");
        List<Product> results = productRepository.findAll(builder.build());

        assertThat(results.size()).isEqualTo(1);
    }

    @Test
    public void givenNegationName_whenSearch_thenCorrect() {
        SpecificationBuilder builder = new SpecificationBuilder();
        builder.with("name", "!", "Phone","", "");

        List<Product> results = productRepository.findAll(builder.build());

        assertThat(results.size()).isEqualTo(6);
    }

    @Test
    public void givenPriceLessThan_whenSearch_thenCorrect() {
        SpecificationBuilder builder = new SpecificationBuilder();
        builder.with("price", "<", "15","", "");

        List<Product> results = productRepository.findAll(builder.build());

        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    public void givenPriceGreaterThan_whenSearch_thenCorrect() {
        SpecificationBuilder builder = new SpecificationBuilder();
        builder.with("price", ">", "15","", "");
        List<Product> results = productRepository.findAll(builder.build());

        assertThat(results.size()).isEqualTo(5);
    }

}
