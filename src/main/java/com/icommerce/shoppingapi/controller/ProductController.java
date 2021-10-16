package com.icommerce.shoppingapi.controller;

import com.icommerce.shoppingapi.controller.dto.response.ProductResponseDTO;
import com.icommerce.shoppingapi.repository.model.Product;
import com.icommerce.shoppingapi.service.ProductService;
import com.icommerce.shoppingapi.service.QueryParserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    private final QueryParserService queryParserService;

    @Operation(summary = "Search and filter product by name, brand, colour, price, category name")
    @GetMapping(value = {""})
    public ResponseEntity<Page<ProductResponseDTO>> search(@RequestParam(required = false, value = "query") String query,
                                                           @Nullable Pageable pageable) {
        log.debug("REST request to search product : query[{}] and pageable[{}]", query, pageable);
        return ResponseEntity.ok().body(productService.findByCriteria(queryParserService.getProductSpec(query), pageable));
    }

    @Operation(summary = "Get a product by its id")
    @GetMapping(value = "/{id}" )
    public @NotNull Product getProductById(@PathVariable Long id) {
        log.debug("REST request to get Product by Id : {}", id);
        return productService.findProductById(id);
    }
}
