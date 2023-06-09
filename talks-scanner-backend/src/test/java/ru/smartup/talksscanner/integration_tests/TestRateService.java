package ru.smartup.talksscanner.integration_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.smartup.talksscanner.domain.*;
import ru.smartup.talksscanner.dto.requests.EntityType;
import ru.smartup.talksscanner.dto.responses.RateDtoResponse;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.repos.*;
import ru.smartup.talksscanner.service.AuthUserService;
import ru.smartup.talksscanner.service.RateService;
import ru.smartup.talksscanner.tools.RateMapper;
import ru.smartup.talksscanner.tools.UserMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.smartup.talksscanner.test_data.IdeaTestData.idea;
import static ru.smartup.talksscanner.test_data.RateTestData.*;
import static ru.smartup.talksscanner.test_data.TopicTestData.makeTopic;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
public class TestRateService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TopicRepo topicRepo;
    @Autowired
    private IdeaRepo ideaRepo;
    @Autowired
    private IdeaRateRepo ideaRateRepo;
    @Autowired
    private TopicRateRepo topicRateRepo;
    private AuthUserService authUserService;
    private RateMapper rateMapper;
    private RateService rateService;

    @BeforeEach
    public void setUp(){
        rateMapper = new RateMapper();
        authUserService = new AuthUserService(userRepo, new UserMapper(new BCryptPasswordEncoder()));
        rateService = new RateService(ideaRepo, topicRepo,
                authUserService, rateMapper, ideaRateRepo, topicRateRepo);
    }

    @Test
    public void testSaveIdeaRate() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "path"));
        LocalDateTime date = LocalDateTime.now();
        Idea savedIdea = ideaRepo.save(new Idea("t", "d", user, date, date, IdeaStatus.CREATED));

        RateDtoResponse response = rateService.saveRate(savedIdea.getId(), saveRateDtoRequest(4), "login", EntityType.IDEA);
        Optional<IdeaRate> savedOptionalRate = ideaRateRepo.findById(response.getId());
        assertFalse(savedOptionalRate.isEmpty());
    }

    @Test
    public void testSaveTopicRate() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "path"));
        Topic savedTopic = topicRepo.save(makeTopic(0, "title", "auth",
                "desc", "tags", LocalDateTime.now(), "presLink", "videoLink", TopicStatus.DONE, user));

        RateDtoResponse response = rateService.saveRate(savedTopic.getId(), saveRateDtoRequest(4), "login", EntityType.TOPIC);
        Optional<TopicRate> savedOptionalRate = topicRateRepo.findById(response.getId());
        assertFalse(savedOptionalRate.isEmpty());
    }

    @Test
    public void testUpdateIdeaComment() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "path"));
        LocalDateTime date = LocalDateTime.now();
        Idea savedIdea = ideaRepo.save(new Idea("t", "d", user, date, date, IdeaStatus.CREATED));
        IdeaRate savedRate = ideaRateRepo.save(ideaRate(user, savedIdea));

        RateDtoResponse response = rateService.updateRate(savedIdea.getId(), savedRate.getId(), updateRateDtoRequest(), "login", EntityType.IDEA);
        assertEquals(2, response.getRate());

        IdeaRate afterUpdateRate = ideaRateRepo.findById(response.getId()).get();
        assertEquals(2, afterUpdateRate.getRate());
        assertNotEquals(date, afterUpdateRate.getLastUpdateDate());
    }

    @Test
    public void testUpdateTopicComment() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "path"));
        LocalDateTime date = LocalDateTime.now();
        Topic savedTopic = topicRepo.save(makeTopic(user));
        TopicRate savedRate = topicRateRepo.save(topicRate(user, savedTopic));

        RateDtoResponse response = rateService.updateRate(savedTopic.getId(), savedRate.getId(), updateRateDtoRequest(), "login", EntityType.TOPIC);
        assertEquals(2, response.getRate());

        TopicRate afterUpdateRate = topicRateRepo.findById(response.getId()).get();
        assertEquals(2, afterUpdateRate.getRate());
        assertNotEquals(date, afterUpdateRate.getLastUpdateDate());
    }

    @Test
    public void testDeleteIdeaComment() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "pathLogo"));
        long savedIdeaId = ideaRepo.save(idea(0 ,user)).getId();
        long savedRateId = rateService.saveRate(savedIdeaId, saveRateDtoRequest(), "login", EntityType.IDEA).getId();

        rateService.deleteRate(savedIdeaId, savedRateId, "login", EntityType.IDEA);

        assertTrue(ideaRateRepo.findById(savedRateId).isEmpty());
    }

    @Test
    public void testDeleteTopicComment() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "pathLogo"));
        long savedTopicId = topicRepo.save(makeTopic(0, "title", "auth",
                "desc", "tags", LocalDateTime.now(), "presLink", "videoLink", TopicStatus.DONE, user)).getId();
        long savedRateId = rateService.saveRate(savedTopicId, saveRateDtoRequest(), "login", EntityType.TOPIC).getId();

        rateService.deleteRate(savedTopicId, savedRateId, "login", EntityType.TOPIC);

        assertTrue(topicRateRepo.findById(savedRateId).isEmpty());
    }

    @Test
    public void testFindTopicCommentById() throws ServiceException {
        User user = userRepo.save(new User("email","login","firstName", "lastName", Sex.MALE, "path"));
        Topic savedTopic = topicRepo.save(makeTopic(0, user));
        long rateId = topicRateRepo.save(topicRate(user, savedTopic)).getId();

        RateDtoResponse response = rateService.findRateById(savedTopic.getId(), rateId, "login", EntityType.TOPIC);

        assertAll(
                () -> assertEquals(rateId, response.getId()),
                () -> assertEquals(4, response.getRate()),
                () -> assertEquals("firstName", response.getAuthorFirstName()),
                () -> assertEquals("lastName", response.getAuthorLastName())
        );
    }

    @Test
    public void testFindIdeaCommentById() throws ServiceException {
        User user = userRepo.save(new User("email","login","firstName", "lastName", Sex.MALE, "path"));
        LocalDateTime date = LocalDateTime.now();
        Idea savedIdea = ideaRepo.save(idea(0, user));
        long rateId = ideaRateRepo.save(ideaRate(user, savedIdea)).getId();

        RateDtoResponse response = rateService.findRateById(savedIdea.getId(), rateId, "login", EntityType.IDEA);

        assertAll(
                () -> assertEquals(rateId, response.getId()),
                () -> assertEquals(4, response.getRate()),
                () -> assertEquals("firstName", response.getAuthorFirstName()),
                () -> assertEquals("lastName", response.getAuthorLastName())
        );
    }


}
