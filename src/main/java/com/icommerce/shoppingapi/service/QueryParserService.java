package com.icommerce.shoppingapi.service;

import com.icommerce.shoppingapi.repository.model.Category;
import com.icommerce.shoppingapi.repository.model.Product;
import com.icommerce.shoppingapi.service.search.SearchOperation;
import com.icommerce.shoppingapi.service.search.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class QueryParserService {

    public static final String CATEGORY_FILTER = "category";

    private final CategoryService categoryService;

    public Specification getProductSpec(String query){
        SpecificationBuilder builder = new SpecificationBuilder<Product>();

        Pattern pattern = Pattern.compile(
                "(\\w+?)(" + SearchOperation.SIMPLE_OPERATION_SET + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(query + ",");
        while (matcher.find()) {
            if (matcher.group(1).equalsIgnoreCase(CATEGORY_FILTER)){
                Category category = categoryService.findCategoryByName(matcher.group(4));
                builder.with(
                        matcher.group(1),
                        ":",
                        category,
                        matcher.group(3),
                        matcher.group(5));
            } else{
                builder.with(
                        matcher.group(1),
                        matcher.group(2),
                        matcher.group(4),
                        matcher.group(3),
                        matcher.group(5));
            }

        }

        return builder.build();
    }
}
