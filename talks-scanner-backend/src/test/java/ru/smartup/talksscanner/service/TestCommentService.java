package ru.smartup.talksscanner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.EntityType;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.repos.*;
import ru.smartup.talksscanner.tools.CommentMapper;
import ru.smartup.talksscanner.tools.UserMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.smartup.talksscanner.test_data.CommentTestData.saveCommentDtoRequest;
import static ru.smartup.talksscanner.test_data.CommentTestData.updateCommentDtoRequest;
import static ru.smartup.talksscanner.test_data.UserTestData.user;



public class TestCommentService {
    private AuthUserService authUserService;
    private UserRepo userRepo;
    private IdeaRepo ideaRepo;
    private TopicRepo topicRepo;
    private TopicCommentRepo topicCommentRepo;
    private IdeaCommentRepo ideaCommentRepo;
    private CommentService commentService;
    private CommentMapper commentMapper;

    @BeforeEach
    public void init(){
        userRepo = mock(UserRepo.class);
        ideaRepo = mock(IdeaRepo.class);
        topicRepo = mock(TopicRepo.class);
        topicCommentRepo = mock(TopicCommentRepo.class);
        ideaCommentRepo = mock(IdeaCommentRepo.class);

        commentMapper = new CommentMapper();
        authUserService = new AuthUserService(userRepo, Mockito.mock(UserMapper.class));
        commentService = new CommentService(ideaRepo, topicRepo,
                ideaCommentRepo, topicCommentRepo, authUserService, commentMapper);
    }

    @Test
    public void testSaveComment_userNotFound(){
        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService
                .saveComment(1, saveCommentDtoRequest(), "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals("USER_NOT_FOUND", actualErrorCode);
    }

    @Test
    public void testSaveTopicComment_topicNotFound() {
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(topicRepo.findById(1L)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.saveComment(1, saveCommentDtoRequest(), "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.TOPIC_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testSaveIdeaComment_ideaNotFound() {
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(ideaRepo.findById(1L)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.saveComment(1, saveCommentDtoRequest(), "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.IDEA_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testDeleteIdeaComment_userNotFound(){

        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.deleteComment(1, 1, "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testDeleteTopicComment_userNotFound(){

        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.deleteComment(1, 1, "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testDeleteIdeaComment_commentNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(ideaCommentRepo.findByIdAndUserAndIdeaId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.deleteComment(1, 1, "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.COMMENT_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testDeleteTopicComment_commentNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(topicCommentRepo.findByIdAndUserAndTopicId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.deleteComment(1, 1, "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.COMMENT_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testUpdateIdeaComment_userNotFound(){
        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.updateComment(1, 1, updateCommentDtoRequest(), "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testUpdateTopicComment_userNotFound(){
        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.updateComment(1, 1, updateCommentDtoRequest(), "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testUpdateIdeaComment_commentNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(ideaCommentRepo.findByIdAndUserAndIdeaId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.updateComment(1, 1, updateCommentDtoRequest(), "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.COMMENT_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testUpdateTopicComment_commentNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(topicCommentRepo.findByIdAndUserAndTopicId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.updateComment(1, 1, updateCommentDtoRequest(), "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.COMMENT_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testFindIdeaComment_userNotFound(){
        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.findCommentById(1, 1, "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testFindTopicComment_userNotFound(){
        when(userRepo.findByLogin("login")).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.findCommentById(1, 1, "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.USER_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testFindIdeaComment_commentNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(ideaCommentRepo.findByIdAndUserAndIdeaId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.findCommentById(1, 1, "login", EntityType.IDEA)).getErrorCode().toString();

        assertEquals(ErrorCode.COMMENT_NOT_FOUND.toString(), actualErrorCode);
    }

    @Test
    public void testFindTopicComment_commentNotFound(){
        User user = user();

        when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        when(topicCommentRepo.findByIdAndUserAndTopicId(1, user, 1)).thenReturn(Optional.empty());

        String actualErrorCode = assertThrows(NotFoundEntityException.class, () -> commentService.findCommentById(1, 1, "login", EntityType.TOPIC)).getErrorCode().toString();

        assertEquals(ErrorCode.COMMENT_NOT_FOUND.toString(), actualErrorCode);
    }
}
