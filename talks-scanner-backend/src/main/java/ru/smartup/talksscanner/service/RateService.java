package ru.smartup.talksscanner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.smartup.talksscanner.domain.*;
import ru.smartup.talksscanner.dto.requests.EntityType;
import ru.smartup.talksscanner.dto.requests.SaveRateDtoRequest;
import ru.smartup.talksscanner.dto.requests.UpdateRateDtoRequest;
import ru.smartup.talksscanner.dto.responses.RateDtoResponse;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.repos.IdeaRateRepo;
import ru.smartup.talksscanner.repos.IdeaRepo;
import ru.smartup.talksscanner.repos.TopicRateRepo;
import ru.smartup.talksscanner.repos.TopicRepo;
import ru.smartup.talksscanner.tools.RateMapper;

@Service
public class RateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateService.class);
    private final IdeaRepo ideaRepo;
    private final TopicRepo topicRepo;
    private final IdeaRateRepo ideaRateRepo;
    private final TopicRateRepo topicRateRepo;
    private final AuthUserService authUserService;
    private final RateMapper rateMapper;

    public RateService(IdeaRepo ideaRepo, TopicRepo topicRepo, AuthUserService authUserService, RateMapper rateMapper, IdeaRateRepo ideaRateRepo, TopicRateRepo topicRateRepo) {
        this.ideaRepo = ideaRepo;
        this.topicRepo = topicRepo;
        this.authUserService = authUserService;
        this.rateMapper = rateMapper;
        this.ideaRateRepo = ideaRateRepo;
        this.topicRateRepo = topicRateRepo;
    }

    public RateDtoResponse saveRate(long entityId, SaveRateDtoRequest request, String login, EntityType entityType) throws ServiceException {
        LOGGER.info("Rate Service: save rate with request {} by user with login {}", request, login);

        User user = authUserService.getUserByLogin(login);
        Rate rate;
        switch (entityType){
            case IDEA -> {
                Idea idea = ideaRepo.findById(entityId)
                        .orElseThrow(() -> new NotFoundEntityException(ErrorCode.IDEA_NOT_FOUND, String.format(ErrorCode.IDEA_NOT_FOUND.getTemplate(), entityId)));
                rate = ideaRateRepo.save(rateMapper.toModel(request, user, idea));
            }
            case TOPIC -> {
                Topic topic = topicRepo.findById(entityId)
                        .orElseThrow(() -> new NotFoundEntityException(ErrorCode.TOPIC_NOT_FOUND,
                                String.format(ErrorCode.TOPIC_NOT_FOUND.getTemplate(), entityId)));
                if(!topic.getStatus().equals(TopicStatus.DONE)){
                    throw new ServiceException(ErrorCode.TOPIC_CANT_BE_RATED, String.format(ErrorCode.TOPIC_CANT_BE_RATED.getTemplate(), entityId));
                }
                rate = topicRateRepo.save(rateMapper.toModel(request, user, topic));
            }
            default -> throw new ServiceException(ErrorCode.ENTITY_TYPE_NOT_HANDLE, String.format(ErrorCode.ENTITY_TYPE_NOT_HANDLE.getTemplate(), entityType));
        }
        return rateMapper.toDto(rate);
    }

    public RateDtoResponse updateRate(long entityId, long rateId, UpdateRateDtoRequest request, String login, EntityType entityType) throws ServiceException {
        LOGGER.info("Rate service: update rate with id {} and request {} by user with login {}", rateId, request, login);
        User user = authUserService.getUserByLogin(login);

        switch (entityType){
            case IDEA -> {
                IdeaRate ideaRate = findRateByIdAndUserAndIdeaId(rateId, user, entityId);
                rateMapper.updateRateByDto(ideaRate, request);
                return rateMapper.toDto(ideaRateRepo.save(ideaRate));
            }
            case TOPIC -> {
                TopicRate topicRate = findRateByIdAndUserAndTopicId(rateId, user, entityId);
                rateMapper.updateRateByDto(topicRate, request);
                return rateMapper.toDto(topicRateRepo.save(topicRate));
            }
            default -> throw new ServiceException(ErrorCode.ENTITY_TYPE_NOT_HANDLE, String.format(ErrorCode.ENTITY_TYPE_NOT_HANDLE.getTemplate(), entityType));
        }
    }

    public void deleteRate(long entityId, long rateId, String login, EntityType entityType) throws ServiceException {
        LOGGER.info("Rate service: delete rate with id {} by user with login {}", rateId, login);
        User user = authUserService.getUserByLogin(login);
        switch (entityType){
            case IDEA -> ideaRateRepo.delete(findRateByIdAndUserAndIdeaId(rateId, user, entityId));
            case TOPIC -> topicRateRepo.delete(findRateByIdAndUserAndTopicId(rateId, user, entityId));
            default -> throw new ServiceException(ErrorCode.ENTITY_TYPE_NOT_HANDLE, String.format(ErrorCode.ENTITY_TYPE_NOT_HANDLE.getTemplate(), entityType));
        }
    }

    public RateDtoResponse findRateByUser(long entityId, String login, EntityType entityType) throws ServiceException {
        LOGGER.info("Rate service: find rate by user with login {}", login);
        User user = authUserService.getUserByLogin(login);

        switch (entityType){
            case IDEA -> {
                return rateMapper.toDto(findRateByUserAndIdeaId(user, entityId));
            }
            case TOPIC -> {
                return rateMapper.toDto(findRateUserAndTopicId(user, entityId));
            }
            default -> throw new ServiceException(ErrorCode.ENTITY_TYPE_NOT_HANDLE, String.format(ErrorCode.ENTITY_TYPE_NOT_HANDLE.getTemplate(), entityType));
        }
    }

    public RateDtoResponse findRateById(long entityId, long rateId, String login, EntityType entityType) throws ServiceException {
        LOGGER.info("Rate service: find rate by user with login {}", login);
        User user = authUserService.getUserByLogin(login);

        switch (entityType){
            case IDEA -> {
                return rateMapper.toDto(findRateByIdAndUserAndIdeaId(rateId, user, entityId));
            }
            case TOPIC -> {
                return rateMapper.toDto(findRateByIdAndUserAndTopicId(rateId, user, entityId));
            }
            default -> throw new ServiceException(ErrorCode.ENTITY_TYPE_NOT_HANDLE, String.format(ErrorCode.ENTITY_TYPE_NOT_HANDLE.getTemplate(), entityType));
        }
    }

    private IdeaRate findRateByUserAndIdeaId(User user, long ideaId) {
        return ideaRateRepo.findByUserAndIdeaId(user, ideaId).orElse(null);
    }

    private TopicRate findRateUserAndTopicId(User user, long topicId) {
        return topicRateRepo.findByUserAndTopicId(user, topicId).orElse(null);
    }

    private IdeaRate findRateByIdAndUserAndIdeaId(long rateId, User user, long ideaId) throws NotFoundEntityException {
        return ideaRateRepo.findByIdAndUserAndIdeaId(rateId, user, ideaId).orElseThrow(
                () -> new NotFoundEntityException(ErrorCode.RATE_NOT_FOUND, String.format(ErrorCode.RATE_NOT_FOUND.getTemplate(), rateId))
        );
    }

    private TopicRate findRateByIdAndUserAndTopicId(long rateId, User user, long topicId) throws NotFoundEntityException {
        return topicRateRepo.findByIdAndUserAndTopicId(rateId, user, topicId).orElseThrow(
                () -> new NotFoundEntityException(ErrorCode.RATE_NOT_FOUND, String.format(ErrorCode.RATE_NOT_FOUND.getTemplate(), rateId))
        );
    }
}
