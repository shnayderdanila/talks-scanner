package ru.smartup.talksscanner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.smartup.talksscanner.criteria.SpecificationBuilder;
import ru.smartup.talksscanner.domain.Sex;
import ru.smartup.talksscanner.domain.Topic;
import ru.smartup.talksscanner.domain.TopicStatus;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.responses.TopicDtoResponse;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.repos.TopicRepo;
import ru.smartup.talksscanner.repos.UserRepo;
import ru.smartup.talksscanner.tools.FilterMapper;
import ru.smartup.talksscanner.tools.TopicMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.smartup.talksscanner.test_data.TopicTestData.*;
import static ru.smartup.talksscanner.test_data.UserTestData.user;



public class TopicServiceTest {
    private UserRepo userRepo;
    private TopicRepo topicRepo;
    private TopicService topicService;
    private User user = user(1 ,"email", "login", "firstName", "lastName", Sex.MALE, "pathLogo");

    @BeforeEach
    public void init(){
        userRepo = mock(UserRepo.class);
        topicRepo = mock(TopicRepo.class);
        topicService = new TopicService(new TopicMapper(), topicRepo, userRepo, new SpecificationBuilder(), new FilterMapper());
    }


    @Test
    public void testInsertUserTopic(){
        User user = user(1 ,"email", "login", "firstName", "lastName", Sex.MALE, "pathLogo");

        Topic topic = makeTopic(0, "title", "author", "description", null, null,
                null, null, TopicStatus.ANNOUNCED, user);

        Topic savedTopic = makeTopic(1, "title", "author", "description", null, null,
                null, null, TopicStatus.ANNOUNCED, user);

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(topicRepo.save(topic)).thenReturn(savedTopic);

        TopicDtoResponse expected = new TopicDtoResponse(1, "title", "author", "description",
                null, null, null, null, TopicStatus.ANNOUNCED, user.getId(), 0.0);
        TopicDtoResponse actual = topicService.insertTopic(makeInsertTopicDtoRequest("title", "author", "description"), "login");

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUserTopic() throws ServiceException {
        Topic topic = makeTopic(1, "title", "author", "desc", null, null, null, null, TopicStatus.ANNOUNCED, user);

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));

        when(topicRepo.findByIdAndUserId(1, 1)).thenReturn(Optional.of(topic));
        when(topicRepo.save(topic)).thenReturn(topic);

        TopicDtoResponse expected = new TopicDtoResponse(1, "new_title", "author", "new_desc", null, null, null, null, TopicStatus.ANNOUNCED, 1, 0.0);
        TopicDtoResponse actual = topicService.updateTopic(makeUpdateAnnouncedTopicDtoRequest("new_title", "new_desc"), "login", 1);
        assertEquals(expected, actual);
    }



    @Test
    public void testUpdateUserTopic_updateNotFoundUserTopic() {
        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));

        when(topicRepo.findByIdAndUserId(1, 1)).thenReturn(Optional.empty());

        String expectedErrorCode = ErrorCode.TOPIC_NOT_FOUND.toString();

        ServiceException actualError = assertThrows(ServiceException.class, () -> topicService.updateTopic
                (makeUpdateAnnouncedTopicDtoRequest("new_title", "new_desc"), "login", 1));

        assertEquals(expectedErrorCode, actualError.getErrorCode().toString());
    }
}