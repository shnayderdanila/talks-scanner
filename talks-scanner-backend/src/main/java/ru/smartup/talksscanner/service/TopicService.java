package ru.smartup.talksscanner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smartup.talksscanner.criteria.Filter;
import ru.smartup.talksscanner.criteria.SpecificationBuilder;
import ru.smartup.talksscanner.domain.Topic;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.ChangeTopicStatusDtoRequest;
import ru.smartup.talksscanner.dto.requests.GetAllTopicsParamRequest;
import ru.smartup.talksscanner.dto.requests.InsertTopicDtoRequest;
import ru.smartup.talksscanner.dto.requests.UpdateTopicDtoRequest;
import ru.smartup.talksscanner.dto.responses.TopicDtoResponse;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.repos.TopicRepo;
import ru.smartup.talksscanner.repos.UserRepo;
import ru.smartup.talksscanner.tools.FilterMapper;
import ru.smartup.talksscanner.tools.TopicMapper;

import java.util.List;
import java.util.Optional;

/**
 * Topic endpoints processing service
 */
@Service
public class TopicService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TopicService.class);
    private final UserRepo userRepo;
    private final TopicRepo topicRepo;
    private final SpecificationBuilder specificationBuilder;
    private final FilterMapper filterMapper;
    private final TopicMapper topicMapper;

    public TopicService(TopicMapper topicMapper, TopicRepo topicRepo, UserRepo userRepo, SpecificationBuilder specificationBuilder, FilterMapper filterMapper) {
        this.topicMapper = topicMapper;
        this.topicRepo = topicRepo;
        this.userRepo = userRepo;
        this.specificationBuilder = specificationBuilder;
        this.filterMapper = filterMapper;
    }

    public TopicDtoResponse insertTopic(InsertTopicDtoRequest request, String login) {
        LOGGER.info("Topic service: insert topic {} by user with login {}", request, login);

        User user = userRepo.findByLogin(login).get();
        Topic topic = topicMapper.toModel(request, user);
        return topicMapper.toDto(topicRepo.save(topic));
    }

    @Transactional(readOnly = true)
    public Page<TopicDtoResponse> getAllTopics(GetAllTopicsParamRequest request, Pageable pageable) throws ServiceException {
        LOGGER.info("get Topics by params : {}", request);

        List<Filter> filters = filterMapper.convertToFilters(request);

        return filters.isEmpty() ?
                topicRepo.findAll(pageable).map(topicMapper::toDto) :
                topicRepo.findAll(specificationBuilder.buildSpecifications(filters), pageable).map(topicMapper::toDto);
    }

    @Transactional(readOnly = true)
    public TopicDtoResponse getTopicById(long topicId) throws ServiceException {
        LOGGER.info("get Topic by Id : {}", topicId);

        return topicMapper.toDto(topicRepo.findById(topicId).orElseThrow(() -> new NotFoundEntityException(ErrorCode.TOPIC_NOT_FOUND, String.format(ErrorCode.TOPIC_NOT_FOUND.getTemplate(), topicId))));
    }

    public TopicDtoResponse updateTopic(UpdateTopicDtoRequest request, String login, long topicId) throws ServiceException {
        LOGGER.info("Topic service: update topic with id {} by user with login {}", topicId, login);

        User user = userRepo.findByLogin(login).get();
        long userId = user.getId();
        Optional<Topic> optionalTopic = topicRepo.findByIdAndUserId(topicId, userId);
        if (optionalTopic.isEmpty()) {
            LOGGER.warn("Topic service: Topic with id {} for user with id {} not found", topicId, userId);
            throw new NotFoundEntityException(ErrorCode.TOPIC_NOT_FOUND, String.format(ErrorCode.TOPIC_NOT_FOUND.getTemplate(), topicId));
        }

        Topic topic = optionalTopic.get();
        topicMapper.updateModel(request, topic);
        return topicMapper.toDto(topicRepo.save(topic));
    }

    public TopicDtoResponse changeTopicStatus(ChangeTopicStatusDtoRequest request, String login, long topicId) throws NotFoundEntityException {
        LOGGER.info("Topic service: change status of Topic with id {} by User {}", topicId, login);

        User user = userRepo.findByLogin(login).get();
        Topic topic = topicRepo.findByIdAndUserId(topicId, user.getId()).orElseThrow(
                () -> new NotFoundEntityException(ErrorCode.TOPIC_NOT_FOUND, String.format(ErrorCode.TOPIC_NOT_FOUND.getTemplate(), topicId))
        );

        if (request.getStatus().getOrder() - topic.getStatus().getOrder() == 1) {
            topic.setStatus(request.getStatus());
            topicRepo.save(topic);
        }

        return topicMapper.toDto(topic);
    }
}
