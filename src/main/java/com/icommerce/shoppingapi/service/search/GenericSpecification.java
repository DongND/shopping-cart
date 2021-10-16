package com.icommerce.shoppingapi.service.search;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;
    @Override
    public Predicate toPredicate
        (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(root.<String> get(
                        criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.<String> get(
                        criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return builder.like(builder.lower(root.<String> get(
                        criteria.getKey())), criteria.getValue().toString().toLowerCase());
            case STARTS_WITH:
                return builder.like(builder.lower(root.<String> get(criteria.getKey())), criteria.getValue().toString().toLowerCase() + "%");
            case ENDS_WITH:
                return builder.like(builder.lower(root.<String> get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase());
            case CONTAINS:
                return builder.like(builder.lower(root.<String> get(
                        criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
            default:
                return null;
        }
    }
}
