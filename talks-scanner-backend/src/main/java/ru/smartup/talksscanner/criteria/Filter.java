package ru.smartup.talksscanner.criteria;

/**
 * Use this for building Spring Specification in {@link  ru.smartup.talksscanner.criteria.SpecificationBuilder}.
 */
public record Filter(String field, QueryOperator operator, Object value) {

}
