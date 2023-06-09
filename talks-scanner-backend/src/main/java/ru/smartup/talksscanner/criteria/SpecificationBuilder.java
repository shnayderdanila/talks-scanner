package ru.smartup.talksscanner.criteria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.ServiceException;

import java.util.List;


/**
 * Use it for convert {@link Filter} to Spring Specification.
 * */
@Component
public class SpecificationBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecificationBuilder.class);

    public <T> Specification<T> buildSpecifications(List<Filter> filters) throws ServiceException {
        LOGGER.debug("create Specification from Filter : {}", filters);
        if (filters.isEmpty()) {
            throw new ServiceException(ErrorCode.EMPTY_FILTER, ErrorCode.EMPTY_FILTER.getTemplate());
        }

        Specification<T> totalSpecification = createSimpleSpecification(filters.get(0));

        for(int i = 1; i < filters.size(); i++) {
            totalSpecification = totalSpecification.and(createSimpleSpecification(filters.get(i)));
        }

        return Specification.where(totalSpecification);
    }
    private <T> Specification<T> createSimpleSpecification(Filter input) {
        return switch (input.operator()) {
            case EQUALS ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(input.field()), input.value());
            case NOT_EQUALS ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(input.field()), input.value());
            case CONTAINS_WITH_IGNORE_CASE ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(input.field())), "%" + ((String) input.value()).toUpperCase() + "%");
            case STARTS_WITH_IGNORE_CASE ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(input.field())), ((String) input.value()).toUpperCase() + "%");
        };
    }

}
