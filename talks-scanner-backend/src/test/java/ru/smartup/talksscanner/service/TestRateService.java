package ru.smartup.talksscanner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.smartup.talksscanner.domain.Topic;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.EntityType;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.repos.*;
import ru.smartup.talksscanner.tools.RateMapper;
import ru.smartup.talksscanner.tools.UserMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.smartup.talksscanner.test_data.RateTestData.saveRateDtoRequest;
import static ru.smartup.talksscanner.test_data.RateTestData.updateRateDtoRequest;
import static ru.smartup.talksscanner.test_data.TopicTestData.makeTopic;
import static ru.smartup.talksscanner.test_data.UserTestData.user;

public class TestRateService {
    private AuthUserService authUserService;
    private UserRepo userRepo;
    private IdeaRepo ideaRepo;
    private TopicRepo topicRepo;
    private TopicRateRepo topicRateRepo;
    private IdeaRateRepo ideaRateRepo;
    private RateService rateService;
    private RateMapper rateMapper;

    @BeforeEach
    public void init(){
        userRepo = mock(UserRepo.class);
        ideaRepo = mock(IdeaRepo.class);
        topicRepo = mock(TopicRepo.class);
        topicRateRepo = mock(TopicRateRepo.class);
        ideaRateRepo = mock(IdeaRateRepo.class);

        rateMapper = new RateMapper();
        authUserService = new AuthUserService(userRepo, new UserMapper(new BCryptPasswordEncoder()));
        rateService = new RateService(ideaRepo, topicRepo,
                authUserService, rateMapper, ideaRateRepo, topicRateRepo);
    }

    @Test
    public void testSaveIdeaRate_userNotFound(){
        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService
                .saveRate(1, saveRateDtoRequest(), "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals("USER_NOT_FOUND", actualErrorCode);
    }

    @Test
    public void testSaveTopicRate_userNotFound(){
        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService
                .saveRate(1, saveRateDtoRequest(), "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals("USER_NOT_FOUND", actualErrorCode);
    }

    @Test
    public void testSaveTopicRate_topicNotFound() {
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(topicRepo.findById(1L)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.saveRate(1, saveRateDtoRequest(), "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.TOPIC_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testSaveTopicRate_whenTopicHaveNotDoneStatus(){
        User user = user();
        Topic topic = makeTopic(user);

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(topicRepo.findById(1L)).thenReturn(Optional.of(topic));

        String actualErrorCode = assertThrows(ServiceException.class, () -> rateService.saveRate(1, saveRateDtoRequest(), "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.TOPIC_CANT_BE_RATED.toString(), actualErrorCode);
    }

    @Test
    public void testSaveIdeaRate_ideaNotFound() {
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(ideaRepo.findById(1L)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.saveRate(1, saveRateDtoRequest(), "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.IDEA_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testDeleteIdeaRate_userNotFound(){

        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.deleteRate(1, 1, "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testDeleteTopicComment_userNotFound(){

        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.deleteRate(1, 1, "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testDeleteIdeaComment_rateNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(ideaRateRepo.findByIdAndUserAndIdeaId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.deleteRate(1, 1, "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.RATE_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testDeleteTopicRate_rateNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(topicRateRepo.findByIdAndUserAndTopicId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.deleteRate(1, 1, "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.RATE_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testUpdateRateComment_userNotFound(){
        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.updateRate(1, 1, updateRateDtoRequest(), "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testUpdateTopicRate_userNotFound(){
        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.updateRate(1, 1, updateRateDtoRequest(), "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testUpdateIdeaRate_rateNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(ideaRateRepo.findByIdAndUserAndIdeaId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.updateRate(1, 1, updateRateDtoRequest(), "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.RATE_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testUpdateTopicComment_rateNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(topicRateRepo.findByIdAndUserAndTopicId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.updateRate(1, 1, updateRateDtoRequest(), "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.RATE_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testFindIdeaRate_userNotFound(){
        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.findRateById(1, 1, "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testFindTopicRate_userNotFound(){
        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.findRateById(1, 1, "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testFindIdeaRate_rateNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(ideaRateRepo.findByIdAndUserAndIdeaId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.findRateById(1, 1, "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.RATE_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testFindTopicRate_rateNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(topicRateRepo.findByIdAndUserAndTopicId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> rateService.findRateById(1, 1, "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.RATE_NOT_FOUND.toString(), actualErrorCode);
    }
}
