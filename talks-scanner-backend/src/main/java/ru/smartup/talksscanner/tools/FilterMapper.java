package ru.smartup.talksscanner.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.smartup.talksscanner.criteria.Filter;
import ru.smartup.talksscanner.criteria.QueryOperator;
import ru.smartup.talksscanner.domain.IdeaStatus;
import ru.smartup.talksscanner.dto.requests.GetAllIdeasParamRequest;
import ru.smartup.talksscanner.dto.requests.GetAllTopicsParamRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Use it for convert you Data Class to {@link Filter}.
 * */
@Component
public class FilterMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterMapper.class);
    public List<Filter> convertToFilters(GetAllIdeasParamRequest request) {
        LOGGER.info("get Filters from : {}", request);
        List<Filter> filters = new ArrayList<>();
        if (request.getTitleStartsWith() != null) {
            LOGGER.debug("get {} Filter from GetAllIdeasParamRequest.titleStartsWith : {}", QueryOperator.STARTS_WITH_IGNORE_CASE, request.getTitleStartsWith());
            filters.add(new Filter("title", QueryOperator.STARTS_WITH_IGNORE_CASE, request.getTitleStartsWith()));
        }
        if (request.getDescriptionStartsWith() != null) {
            LOGGER.debug("get {} Filter from GetAllIdeasParamRequest.descriptionStartsWith : {}", QueryOperator.STARTS_WITH_IGNORE_CASE, request.getDescriptionStartsWith());
            filters.add(new Filter("description", QueryOperator.STARTS_WITH_IGNORE_CASE, request.getDescriptionStartsWith()));
        }
        LOGGER.debug("get {} Filter from status {}", QueryOperator.NOT_EQUALS, IdeaStatus.DELETED);
        filters.add(new Filter("status", QueryOperator.NOT_EQUALS, IdeaStatus.DELETED));
        return filters;
    }

    public List<Filter> convertToFilters(GetAllTopicsParamRequest request) {
        LOGGER.info("get Filters from : {}", request);
        List<Filter> filters = new ArrayList<>();
        if (request.getTags() != null) {
            LOGGER.debug("get {} Filter from GetAllTopicsParamRequest.tags : {}", QueryOperator.CONTAINS_WITH_IGNORE_CASE, request.getTags());
            filters.add(new Filter("tags", QueryOperator.CONTAINS_WITH_IGNORE_CASE, request.getTags()));
        }
        if (request.getStatus() != null) {
            LOGGER.debug("get {} Filter from GetAllIdeasParamRequest.status : {}", QueryOperator.EQUALS, request.getStatus());
            filters.add(new Filter("status", QueryOperator.EQUALS, request.getStatus()));
        }
        return filters;
    }
}
