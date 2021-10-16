package com.icommerce.shoppingapi.service;

import com.icommerce.shoppingapi.repository.model.Category;
import org.springframework.validation.annotation.Validated;

public interface CategoryService {
    Category findCategoryByName(String name);
}
