package ru.smartup.talksscanner.integration.repos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import ru.smartup.talksscanner.criteria.Filter;
import ru.smartup.talksscanner.criteria.QueryOperator;
import ru.smartup.talksscanner.criteria.SpecificationBuilder;
import ru.smartup.talksscanner.domain.Sex;
import ru.smartup.talksscanner.domain.Topic;
import ru.smartup.talksscanner.domain.TopicStatus;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.GetAllTopicsParamRequest;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.repos.TopicRepo;
import ru.smartup.talksscanner.repos.UserRepo;
import ru.smartup.talksscanner.tools.FilterMapper;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ComponentScan("ru.smartup.talksscanner.criteria")
public class TestTopicRepo {
    @Autowired
    private SpecificationBuilder specificationBuilder;

    @Autowired
    private TopicRepo topicRepo;

    @Autowired
    private UserRepo userRepo;

    private FilterMapper filterMapper;

    private final int defaultRecordPageCount = 10;

    public static Stream<Arguments> filtersWithResult() {
        return Stream.of(
                Arguments.of(List.of(
                        new Filter("status", QueryOperator.EQUALS, TopicStatus.ANNOUNCED)), 3),
                Arguments.of(List.of(
                        new Filter("status", QueryOperator.EQUALS, TopicStatus.ANNOUNCED),
                        new Filter("tags", QueryOperator.STARTS_WITH_IGNORE_CASE, "java")), 2),
                Arguments.of(List.of(
                        new Filter("status", QueryOperator.EQUALS, TopicStatus.BEING_PREPARED),
                        new Filter("tags", QueryOperator.STARTS_WITH_IGNORE_CASE, "java")), 0),
                Arguments.of(List.of(
                        new Filter("tags", QueryOperator.STARTS_WITH_IGNORE_CASE, "FrOnT")), 2),
                Arguments.of(List.of(
                        new Filter("status", QueryOperator.EQUALS, TopicStatus.DONE)), 2),
                Arguments.of(List.of(
                        new Filter("tags", QueryOperator.STARTS_WITH_IGNORE_CASE, "front")), 2),
                Arguments.of(List.of (
                        new Filter("tags", QueryOperator.STARTS_WITH_IGNORE_CASE, "m") ), 2)
        );
    }

    @BeforeEach
    public void set_up() {
        filterMapper = new FilterMapper();
        User user = new User("email@gmail.com", "login", "firstname", "lastname", Sex.MALE, "./logo");

        user = userRepo.save(user);

        // size = 8
        topicRepo.saveAll(List.of(
                new Topic(1L, "title",  user.getLastname() + " " + user.getFirstname(), "description","java", null, null, null, TopicStatus.ANNOUNCED, user, 0.0),
                new Topic(2L, "title",  user.getLastname() + " " + user.getFirstname(), "description","java", null, null, null, TopicStatus.ANNOUNCED, user, 0.0),
                new Topic(3L, "title",  user.getLastname() + " " + user.getFirstname(), "description","FrOnt", null, null, null, TopicStatus.BEING_PREPARED, user, 0.0),
                new Topic(4L, "uTitle", user.getLastname() + " " + user.getFirstname(), "description","front", null, null, null, TopicStatus.ANNOUNCED, user, 0.0),
                new Topic(5L, "uTitle", user.getLastname() + " " + user.getFirstname(), "description","mobx", null, null, null, TopicStatus.DONE, user, 0.0),
                new Topic(6L, "uTitle", user.getLastname() + " " + user.getFirstname(), "description","go", null, null, null, TopicStatus.SCHEDULED, user, 0.0),
                new Topic(7L, "uTitle", user.getLastname() + " " + user.getFirstname(), "description","ts", null, null, null, TopicStatus.DONE, user, 0.0),
                new Topic(8L, "Title",  user.getLastname() + " " + user.getFirstname(), "description","mysql", null, null, null, TopicStatus.SCHEDULED, user, 0.0)
        ));

    }

    @Test
    public void testFindAllWithSpecificationAndPagination__emptyFilters__fromTopic() {
        ServiceException responses = assertThrows(ServiceException.class, () -> topicRepo.findAll(
                specificationBuilder.buildSpecifications(filterMapper.convertToFilters(new GetAllTopicsParamRequest())),
                PageRequest.of(0, defaultRecordPageCount)).getContent());

        assertEquals(ErrorCode.EMPTY_FILTER, responses.getErrorCode());
    }

    @ParameterizedTest
    @MethodSource("filtersWithResult")
    public void testFindAllWithSpecificationAndPagination__withFilters__fromTopic(List<Filter> filters, int expectedCounts) throws ServiceException {
        assertEquals(expectedCounts, topicRepo.findAll(specificationBuilder.buildSpecifications(filters), PageRequest.of(0, defaultRecordPageCount)).getContent().size());
    }

    @Test
    public void testFindAllPagination__withSmallRecordsPage__fromIdea() throws ServiceException {

        int total = topicRepo.findAll(PageRequest.of(0, defaultRecordPageCount)).getContent().size();

        for(int page = 0; page * 3 < total; page++) {
            List<Topic> responses = topicRepo.findAll(PageRequest.of(page, 2)).getContent();
            assertEquals(2, responses.size());
        }
    }

}

