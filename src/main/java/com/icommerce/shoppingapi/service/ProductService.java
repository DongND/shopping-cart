package com.icommerce.shoppingapi.service;

import com.icommerce.shoppingapi.controller.dto.response.ProductResponseDTO;
import com.icommerce.shoppingapi.repository.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import java.util.List;

@Validated
public interface ProductService {

    List<Product> findProductByIds(List<Long> ids);

    Page<ProductResponseDTO> findByCriteria(Specification specs, Pageable page);

    Product findProductById(@Min(value = 1L, message = "Invalid product ID.") long id);

}
