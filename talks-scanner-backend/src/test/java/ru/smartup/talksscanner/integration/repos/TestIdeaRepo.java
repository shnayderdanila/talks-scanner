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
import ru.smartup.talksscanner.domain.Idea;
import ru.smartup.talksscanner.domain.IdeaStatus;
import ru.smartup.talksscanner.domain.Sex;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.GetAllIdeasParamRequest;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.repos.IdeaRepo;
import ru.smartup.talksscanner.repos.UserRepo;
import ru.smartup.talksscanner.tools.FilterMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ComponentScan("ru.smartup.talksscanner.criteria")
public class TestIdeaRepo {
    @Autowired
    private SpecificationBuilder specificationBuilder;

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private UserRepo userRepo;

    private FilterMapper filterMapper;

    private final int defaultRecordPageCount = 10;

    public static Stream<Arguments> filtersWithResult() {
        return Stream.of(
                Arguments.of(List.of(
                        new Filter("status", QueryOperator.NOT_EQUALS, IdeaStatus.DELETED)), 8),
                Arguments.of(List.of(
                        new Filter("status", QueryOperator.NOT_EQUALS, IdeaStatus.DELETED),
                        new Filter("title", QueryOperator.STARTS_WITH_IGNORE_CASE, "t")), 4),
                Arguments.of(List.of(
                        new Filter("status", QueryOperator.NOT_EQUALS, IdeaStatus.DELETED),
                        new Filter("description", QueryOperator.STARTS_WITH_IGNORE_CASE, "u")), 2),
                Arguments.of(List.of (
                        new Filter("status", QueryOperator.NOT_EQUALS, IdeaStatus.DELETED),
                        new Filter("title", QueryOperator.STARTS_WITH_IGNORE_CASE, "T"),
                        new Filter("description", QueryOperator.STARTS_WITH_IGNORE_CASE, "D") ), 2)
        );
    }

    @BeforeEach
    public void set_up() {
        filterMapper = new FilterMapper();
        User user = new User("email@gmail.com", "login", "firstname", "lastname", Sex.MALE, "./logo");

        user = userRepo.save(user);

        // size = 8
        ideaRepo.saveAll(List.of(
                new Idea("title",  "description", user, LocalDateTime.parse("2023-03-18T12:56:30"), LocalDateTime.parse("2023-03-21T12:56:30"), IdeaStatus.CREATED),
                new Idea("title",  "uDescription", user, LocalDateTime.parse("2023-03-18T12:56:00"), LocalDateTime.parse("2023-03-21T12:56:30"), IdeaStatus.CREATED),
                new Idea("title",  "uDescription", user, LocalDateTime.parse("2023-03-19T12:56:24"), LocalDateTime.parse("2023-03-21T12:56:30"), IdeaStatus.CREATED),
                new Idea("uTitle", "description", user, LocalDateTime.parse("2023-03-19T12:56:30"), LocalDateTime.parse("2023-03-21T12:58:30"), IdeaStatus.CREATED),
                new Idea("uTitle", "Description", user, LocalDateTime.parse("2023-03-20T12:56:30"), LocalDateTime.parse("2023-03-22T12:56:30"), IdeaStatus.CREATED),
                new Idea("uTitle", "Description", user, LocalDateTime.parse("2023-03-20T12:56:30"), LocalDateTime.parse("2023-04-20T12:56:30"), IdeaStatus.CREATED),
                new Idea("uTitle", "Description", user, LocalDateTime.parse("2023-03-20T12:58:30"), LocalDateTime.parse("2023-04-20T12:56:30"), IdeaStatus.CREATED),
                new Idea("Title",  "Description", user, LocalDateTime.parse("2023-04-18T12:58:30"), LocalDateTime.parse("2023-04-22T12:56:30"), IdeaStatus.CREATED)
        ));

    }

    @Test
    public void testFindAllWithSpecificationAndPagination__withNotDeletedStatus__andFirstPage__fromIdea() throws ServiceException {
        List<Idea> responses = ideaRepo.findAll(
                specificationBuilder.buildSpecifications(filterMapper.convertToFilters(new GetAllIdeasParamRequest())),
                PageRequest.of(0, defaultRecordPageCount)).getContent();

        assertEquals(8, responses.size());
    }

    @Test
    public void testFindAllWithSpecificationAndPagination__withNotDeletedStatus__withSmallRecordsPage__fromIdea() throws ServiceException {
        for(int page = 0; page * defaultRecordPageCount < defaultRecordPageCount; page++) {
            List<Idea> responses = ideaRepo.findAll(
                    specificationBuilder.buildSpecifications(filterMapper.convertToFilters(new GetAllIdeasParamRequest())),
                    PageRequest.of(page, 2)).getContent();
            assertEquals(2, responses.size());
        }
    }

    @Test
    public void testFindAllWithSpecificationAndPagination__withNotDeletedStatus__fromIdea__afterDelete() throws ServiceException {
        for(Idea idea : ideaRepo.findAll(
                specificationBuilder.buildSpecifications(filterMapper.convertToFilters(new GetAllIdeasParamRequest())),
                PageRequest.of(0, 3))) {
            ideaRepo.delete(idea);
        }

        assertEquals(5, ideaRepo.findAll(
                specificationBuilder.buildSpecifications(filterMapper.convertToFilters(new GetAllIdeasParamRequest())),
                PageRequest.of(0, defaultRecordPageCount)).getTotalElements());
    }

    @ParameterizedTest
    @MethodSource("filtersWithResult")
    public void testFindAllWithSpecificationAndPagination__withFilters__fromIdea(List<Filter> filters, int expectedCounts) throws ServiceException {
        assertEquals(expectedCounts, ideaRepo.findAll(specificationBuilder.buildSpecifications(filters), PageRequest.of(0, defaultRecordPageCount)).getContent().size());
    }

    @Test
    public void testFindAllWithSpecificationAndPagination__withEmptyFilters__withSmallRecordsPage__fromIdea__afterDelete() throws ServiceException {
        for(Idea idea : ideaRepo.findAll(
                specificationBuilder.buildSpecifications(filterMapper.convertToFilters(new GetAllIdeasParamRequest())), PageRequest.of(0, 3))) {
            ideaRepo.delete(idea);
        }

        int total = ideaRepo.findAll(
                specificationBuilder.buildSpecifications(filterMapper.convertToFilters(new GetAllIdeasParamRequest())),
                PageRequest.of(0, defaultRecordPageCount)).getContent().size();

        for(int page = 0; page * 3 < total; page++) {
            List<Idea> responses = ideaRepo.findAll(
                    specificationBuilder.buildSpecifications(filterMapper.convertToFilters(new GetAllIdeasParamRequest())), PageRequest.of(page, 2)).getContent();
            assertEquals(2, responses.size());
        }
    }

}

