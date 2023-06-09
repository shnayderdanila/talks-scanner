package ru.smartup.talksscanner.integration_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.smartup.talksscanner.domain.*;
import ru.smartup.talksscanner.dto.requests.EntityType;
import ru.smartup.talksscanner.dto.responses.CommentDtoResponse;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.repos.*;
import ru.smartup.talksscanner.service.AuthUserService;
import ru.smartup.talksscanner.service.CommentService;
import ru.smartup.talksscanner.tools.CommentMapper;
import ru.smartup.talksscanner.tools.UserMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.smartup.talksscanner.test_data.CommentTestData.*;
import static ru.smartup.talksscanner.test_data.IdeaTestData.idea;
import static ru.smartup.talksscanner.test_data.TopicTestData.makeTopic;




@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
public class TestCommentService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TopicRepo topicRepo;
    @Autowired
    private IdeaRepo ideaRepo;
    @Autowired
    private IdeaCommentRepo ideaCommentRepo;
    @Autowired
    private TopicCommentRepo topicCommentRepo;
    private AuthUserService authUserService;
    private CommentMapper commentMapper;
    private CommentService commentService;


    @BeforeEach
    public void setUp(){
        commentMapper = new CommentMapper();
        authUserService = new AuthUserService(userRepo, Mockito.mock(UserMapper.class));
        commentService = new CommentService(ideaRepo, topicRepo,
                ideaCommentRepo, topicCommentRepo, authUserService, commentMapper);
    }

    @Test
    public void testSaveIdeaComment() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "path"));
        LocalDateTime date = LocalDateTime.now();
        Idea savedIdea = ideaRepo.save(new Idea("t", "d", user, date, date, IdeaStatus.CREATED));

        CommentDtoResponse response = commentService.saveComment(savedIdea.getId(), saveCommentDtoRequest("text"), "login", EntityType.IDEA);
        Optional<IdeaComment> savedOptionalComment = ideaCommentRepo.findById(response.getId());
        assertFalse(savedOptionalComment.isEmpty());
    }

    @Test
    public void testSaveTopicComment() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "path"));
        Topic savedTopic = topicRepo.save(makeTopic(user));

        CommentDtoResponse response = commentService.saveComment(savedTopic.getId(), saveCommentDtoRequest("text"), "login", EntityType.TOPIC);
        Optional<TopicComment> savedOptionalComment = topicCommentRepo.findById(response.getId());
        assertFalse(savedOptionalComment.isEmpty());
    }

    @Test
    public void testUpdateIdeaComment() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "path"));
        LocalDateTime date = LocalDateTime.now();
        Idea savedIdea = ideaRepo.save(new Idea("t", "d", user, date, date, IdeaStatus.CREATED));
        IdeaComment savedComment = ideaCommentRepo.save(ideaComment(user, savedIdea));

        CommentDtoResponse response = commentService.updateComment(savedComment.getId(), savedIdea.getId(), updateCommentDtoRequest(), "login", EntityType.IDEA);
        assertEquals("new_text", response.getText());

        IdeaComment afterUpdateComment = ideaCommentRepo.findById(response.getId()).get();
        assertEquals("new_text", afterUpdateComment.getText());
        assertNotEquals(date, afterUpdateComment.getLastUpdateDate());
    }

    @Test
    public void testUpdateTopicComment() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "path"));
        LocalDateTime date = LocalDateTime.now();
        Topic savedTopic = topicRepo.save(makeTopic(user));
        TopicComment savedComment = topicCommentRepo.save(topicComment(user, savedTopic));

        CommentDtoResponse response = commentService.updateComment(savedComment.getId(), savedTopic.getId(), updateCommentDtoRequest(), "login", EntityType.TOPIC);
        assertEquals("new_text", response.getText());

        TopicComment afterUpdateComment = topicCommentRepo.findById(response.getId()).get();
        assertEquals("new_text", afterUpdateComment.getText());
        assertNotEquals(date, afterUpdateComment.getLastUpdateDate());
    }

    @Test
    public void testDeleteIdeaComment() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "pathLogo"));
        long savedIdeaId = ideaRepo.save(idea(0 ,user)).getId();
        long savedCommentId = commentService.saveComment(savedIdeaId, saveCommentDtoRequest("text"), "login", EntityType.IDEA).getId();

        commentService.deleteComment(savedCommentId, savedIdeaId, "login", EntityType.IDEA);

        assertTrue(ideaCommentRepo.findById(savedCommentId).isEmpty());
    }

    @Test
    public void testDeleteTopicComment() throws ServiceException {
        User user = userRepo.save(new User("email","login","first", "last", Sex.MALE, "pathLogo"));
        long savedTopicId = topicRepo.save(makeTopic(0 ,user)).getId();
        long savedCommentId = commentService.saveComment(savedTopicId, saveCommentDtoRequest("text"), "login", EntityType.TOPIC).getId();

        commentService.deleteComment(savedCommentId, savedTopicId, "login", EntityType.TOPIC);

        assertTrue(topicCommentRepo.findById(savedCommentId).isEmpty());
    }

    @Test
    public void testFindTopicCommentById() throws ServiceException {
        User user = userRepo.save(new User("email","login","firstName", "lastName", Sex.MALE, "path"));
        LocalDateTime date = LocalDateTime.now();
        Topic savedTopic = topicRepo.save(makeTopic(0, user));
        long commentId = topicCommentRepo.save(topicComment(user, savedTopic, date)).getId();

        CommentDtoResponse response = commentService.findCommentById(commentId, savedTopic.getId(), "login", EntityType.TOPIC);

        assertAll(
                () -> assertEquals(commentId, response.getId()),
                () -> assertEquals("text", response.getText()),
                () -> assertEquals("firstName", response.getAuthorFirstName()),
                () -> assertEquals("lastName", response.getAuthorLastName())
        );
    }

    @Test
    public void testFindIdeaCommentById() throws ServiceException {
        User user = userRepo.save(new User("email","login","firstName", "lastName", Sex.MALE, "path"));
        LocalDateTime date = LocalDateTime.now();
        Idea savedIdea = ideaRepo.save(idea(0, user));
        long commentId = ideaCommentRepo.save(ideaComment(user, savedIdea, date)).getId();

        CommentDtoResponse response = commentService.findCommentById(commentId, savedIdea.getId(), "login", EntityType.IDEA);

        assertAll(
                () -> assertEquals(commentId, response.getId()),
                () -> assertEquals("text", response.getText()),
                () -> assertEquals("firstName", response.getAuthorFirstName()),
                () -> assertEquals("lastName", response.getAuthorLastName())
        );
    }

}
