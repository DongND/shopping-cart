package com.icommerce.shoppingapi.controller;

import com.icommerce.shoppingapi.controller.dto.request.CartRequestDTO;
import com.icommerce.shoppingapi.controller.dto.response.CartCommonResponseDTO;
import com.icommerce.shoppingapi.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Get a Shopping Cart by its id")
    @GetMapping(value = "/{id}" )
    public ResponseEntity<CartCommonResponseDTO> getById(@PathVariable Long id) {
        log.debug("REST request to get Cart by Id : {}", id);
        return new ResponseEntity<>(this.cartService.getCartById(id), HttpStatus.OK);

    }

    @Operation(summary = "Create new Shopping Cart")
    @PostMapping
    public ResponseEntity<CartCommonResponseDTO> create(@RequestBody CartRequestDTO request) {
        log.debug("REST request to create new Shopping Cart : {}", request);
        CartCommonResponseDTO responseDTO = cartService.create(request);
        String uri = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/api/carts/{id}")
                .buildAndExpand(responseDTO.getId())
                .toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return new ResponseEntity<>(responseDTO, headers, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a existing Shopping Cart with its id")
    @PutMapping(value = "/{id}" )
    public ResponseEntity<CartCommonResponseDTO> update(@PathVariable Long id, @RequestBody CartRequestDTO request) {
        log.debug("REST request to update existing Shopping Cart : id[{}] and request[{}]", id, request);

        CartCommonResponseDTO responseDTO = cartService.update(id, request);
        String uri = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/api/carts/{id}")
                .buildAndExpand(responseDTO.getId())
                .toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return new ResponseEntity<>(responseDTO, headers, HttpStatus.OK);
    }

}
