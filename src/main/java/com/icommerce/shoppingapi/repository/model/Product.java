package com.icommerce.shoppingapi.repository.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AbstractAuditingEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product name is required.")
    private String name;

    private String description;

    @NotNull(message = "Price is required.")
    private Double price;

    private String brand;

    private String colour;

    private String pictureUrl;

    private Long stockQuantity;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

}
