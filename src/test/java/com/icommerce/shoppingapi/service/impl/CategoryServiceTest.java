package com.icommerce.shoppingapi.service.impl;

import com.icommerce.shoppingapi.exception.ResourceNotFoundException;
import com.icommerce.shoppingapi.repository.model.Category;
import com.icommerce.shoppingapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.icommerce.shoppingapi.util.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    public void initTest() {
        category = new Category(DEFAULT_CATEGORY_ID, DEFAULT_NAME, DEFAULT_DESCRIPTION);
    }

    @Test
    public void givenExistedCategoryName_whenFindCategoryByName_thenSuccess(){
        Mockito.when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));

        Category actual = categoryService.findCategoryByName(DEFAULT_NAME);

        assertThat(actual).isEqualTo(category);
        verify(categoryRepository, times(1)).findCategoryByName(DEFAULT_NAME);
    }

    @Test
    public void givenDoesNotExistedCategoryName_whenFindCategoryByName_thenThrowResourceNotFoundException(){
        Mockito.when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> categoryService.findCategoryByName(DEFAULT_NAME));
        assertEquals("Category is not found", ex.getMessage());
        verify(categoryRepository, times(1)).findCategoryByName(DEFAULT_NAME);
    }


}
