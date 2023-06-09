package ru.smartup.talksscanner.criteria;

/**
 * Use it for build predicate operation in {@link  ru.smartup.talksscanner.criteria.SpecificationBuilder}.
 * */
public enum QueryOperator {
    STARTS_WITH_IGNORE_CASE,
    CONTAINS_WITH_IGNORE_CASE,
    EQUALS,
    NOT_EQUALS;
}
