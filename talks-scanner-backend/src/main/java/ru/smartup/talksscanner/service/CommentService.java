package ru.smartup.talksscanner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.smartup.talksscanner.domain.*;
import ru.smartup.talksscanner.dto.requests.EntityType;
import ru.smartup.talksscanner.dto.requests.SaveCommentDtoRequest;
import ru.smartup.talksscanner.dto.requests.UpdateCommentDtoRequest;
import ru.smartup.talksscanner.dto.responses.CommentDtoResponse;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.repos.IdeaCommentRepo;
import ru.smartup.talksscanner.repos.IdeaRepo;
import ru.smartup.talksscanner.repos.TopicCommentRepo;
import ru.smartup.talksscanner.repos.TopicRepo;
import ru.smartup.talksscanner.tools.CommentMapper;

import java.util.Optional;

/**
 * Comment endpoints processing service.
 */
@Service
public class CommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);
    private final IdeaRepo ideaRepo;
    private final TopicRepo topicRepo;
    private final IdeaCommentRepo ideaCommentRepo;
    private final TopicCommentRepo topicCommentRepo;
    private final AuthUserService authUserService;
    private final CommentMapper commentMapper;

    public CommentService(IdeaRepo ideaRepo, TopicRepo topicRepo, IdeaCommentRepo ideaCommentRepo, TopicCommentRepo topicCommentRepo, AuthUserService authUserService, CommentMapper commentMapper) {
        this.ideaRepo = ideaRepo;
        this.topicRepo = topicRepo;
        this.ideaCommentRepo = ideaCommentRepo;
        this.topicCommentRepo = topicCommentRepo;
        this.authUserService = authUserService;
        this.commentMapper = commentMapper;
    }

    public CommentDtoResponse saveComment(long entityId, SaveCommentDtoRequest request, String login, EntityType entityType) throws ServiceException {
        LOGGER.info("Comment service: save comment with request {} by user with login {}", request, login);

        User user = authUserService.getUserByLogin(login);
        Comment comment;
        switch (entityType) {
            case IDEA -> {
                Optional<Idea> optionalIdea = ideaRepo.findById(entityId);
                if (optionalIdea.isEmpty()) {
                    LOGGER.warn("Comment service: Idea with id {} not found", entityId);
                    throw new NotFoundEntityException(ErrorCode.IDEA_NOT_FOUND, String.format(ErrorCode.IDEA_NOT_FOUND.getTemplate(), entityId));
                }
                Idea idea = optionalIdea.get();
                IdeaComment ideaComment = commentMapper.toModel(request, user, idea);
                comment = ideaCommentRepo.save(ideaComment);
            }
            case TOPIC -> {
                Optional<Topic> optionalTopic = topicRepo.findById(entityId);
                if (optionalTopic.isEmpty()) {
                    LOGGER.warn("Comment service: Topic with id {} not found", entityId);
                    throw new NotFoundEntityException(ErrorCode.TOPIC_NOT_FOUND, String.format(ErrorCode.TOPIC_NOT_FOUND.getTemplate(), entityId));
                }
                Topic topic = optionalTopic.get();
                TopicComment topicComment = commentMapper.toModel(request, user, topic);
                comment = topicCommentRepo.save(topicComment);
            }
            default -> throw new ServiceException(ErrorCode.ENTITY_TYPE_NOT_HANDLE, String.format(ErrorCode.ENTITY_TYPE_NOT_HANDLE.getTemplate(), entityType));
        }
        return commentMapper.toDto(comment);
    }

    public void deleteComment(long commentId, long entityId, String login, EntityType entityType) throws ServiceException {
        LOGGER.info("Comment service: delete comment with id {} by user with login {}", commentId, login);
        User user = authUserService.getUserByLogin(login);

        switch (entityType){
            case IDEA -> {
                IdeaComment ideaComment = findCommentByIdAndUserAndIdeaId(commentId, user, entityId);
                ideaCommentRepo.delete(ideaComment);
            }
            case TOPIC -> {
                TopicComment topicComment = findCommentByIdAndUserAndTopicId(commentId, user, entityId);
                topicCommentRepo.delete(topicComment);
            }
            default -> throw new ServiceException(ErrorCode.ENTITY_TYPE_NOT_HANDLE, String.format(ErrorCode.ENTITY_TYPE_NOT_HANDLE.getTemplate(), entityType));
        }
    }


    public CommentDtoResponse updateComment(long commentId, long entityId, UpdateCommentDtoRequest request, String login, EntityType entityType) throws ServiceException {
        LOGGER.info("Comment service: update comment with id {}, type {} and request {} by user with login {}", commentId, entityType.toString(), request, login);

        User user = authUserService.getUserByLogin(login);

        switch (entityType){
            case IDEA -> {
                IdeaComment comment = findCommentByIdAndUserAndIdeaId(commentId, user, entityId);
                commentMapper.updateCommentByDto(request, comment);
                return commentMapper.toDto(ideaCommentRepo.save(comment));
            }
            case TOPIC -> {
                TopicComment comment = findCommentByIdAndUserAndTopicId(commentId, user, entityId);
                commentMapper.updateCommentByDto(request, comment);
                return commentMapper.toDto(topicCommentRepo.save(comment));
            }
            default -> throw new ServiceException(ErrorCode.ENTITY_TYPE_NOT_HANDLE, String.format(ErrorCode.ENTITY_TYPE_NOT_HANDLE.getTemplate(), entityType));
        }
    }


    public CommentDtoResponse findCommentById(long commentId, long entityId, String login, EntityType entityType) throws ServiceException {
        LOGGER.info("Comment service: update comment with id {} and type {} by user with login {}", commentId, entityType, login);

        switch (entityType){
            case IDEA -> {
                return commentMapper.toDto(findCommentByIdAndUserAndIdeaId(commentId, authUserService.getUserByLogin(login), entityId));
            }
            case TOPIC -> {
                return commentMapper.toDto(findCommentByIdAndUserAndTopicId(commentId, authUserService.getUserByLogin(login), entityId));
            }
            default -> throw new ServiceException(ErrorCode.ENTITY_TYPE_NOT_HANDLE, String.format(ErrorCode.ENTITY_TYPE_NOT_HANDLE.getTemplate(), entityType));
        }
    }

    public Page<CommentDtoResponse> getAllComments(long entityId, Pageable pageable, EntityType entityType) throws ServiceException {
        LOGGER.info("Comment service: get all Comments type {} by id {}", entityType, entityId);

        switch (entityType){
            case IDEA -> {
                return ideaCommentRepo.findAllByIdeaId(entityId, pageable).map(commentMapper::toDto);
            }
            case TOPIC -> {
                return topicCommentRepo.findAllByTopicId(entityId, pageable).map(commentMapper::toDto);
            }
            default -> throw new ServiceException(ErrorCode.ENTITY_TYPE_NOT_HANDLE, String.format(ErrorCode.ENTITY_TYPE_NOT_HANDLE.getTemplate(), entityType));
        }
    }

    private IdeaComment findCommentByIdAndUserAndIdeaId(long commentId, User user, long ideaId) throws NotFoundEntityException {
        Optional<IdeaComment> optionalComment = ideaCommentRepo.findByIdAndUserAndIdeaId(commentId, user, ideaId);
        if (optionalComment.isEmpty()) {
            throw new NotFoundEntityException(ErrorCode.COMMENT_NOT_FOUND, String.format(ErrorCode.COMMENT_NOT_FOUND.getTemplate(), commentId));
        }
        return optionalComment.get();
    }

    private TopicComment findCommentByIdAndUserAndTopicId(long commentId, User user, long topicId) throws NotFoundEntityException {
        Optional<TopicComment> optionalComment = topicCommentRepo.findByIdAndUserAndTopicId(commentId, user, topicId);

        if (optionalComment.isEmpty()) {
            throw new NotFoundEntityException(ErrorCode.COMMENT_NOT_FOUND, String.format(ErrorCode.COMMENT_NOT_FOUND.getTemplate(), commentId));
        }
        return optionalComment.get();
    }
}
