package ru.smartup.talksscanner.tools;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.smartup.talksscanner.criteria.Filter;
import ru.smartup.talksscanner.criteria.QueryOperator;
import ru.smartup.talksscanner.dto.requests.GetAllIdeasParamRequest;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.smartup.talksscanner.test_data.IdeaTestData.makeGetAllIdeaWithParamDtoRequest;

public class TestFilterMapper {

    private final FilterMapper mapper = new FilterMapper();

    public static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of(makeGetAllIdeaWithParamDtoRequest("t", null),
                        List.of(new Filter("title", QueryOperator.STARTS_WITH_IGNORE_CASE, "t"))),
                Arguments.of(makeGetAllIdeaWithParamDtoRequest(null, "u"),
                        List.of(new Filter("description", QueryOperator.STARTS_WITH_IGNORE_CASE, "u"))),
                Arguments.of(makeGetAllIdeaWithParamDtoRequest("t", "u"),
                        List.of(new Filter("title", QueryOperator.STARTS_WITH_IGNORE_CASE, "t")),
                                new Filter("description", QueryOperator.STARTS_WITH_IGNORE_CASE, "u"))
        );
    }

    @ParameterizedTest
    @MethodSource("arguments")
    public void testConvertGetAllIdeaWithParamDtoRequestToFilters(GetAllIdeasParamRequest request, List<Filter> filters) {
        assertTrue(mapper.convertToFilters(request).containsAll(filters));
    }


}
