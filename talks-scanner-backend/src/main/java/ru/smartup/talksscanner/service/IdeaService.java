package ru.smartup.talksscanner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smartup.talksscanner.criteria.SpecificationBuilder;
import ru.smartup.talksscanner.domain.Idea;
import ru.smartup.talksscanner.domain.IdeaStatus;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.GetAllIdeasParamRequest;
import ru.smartup.talksscanner.dto.requests.SaveIdeaDtoRequest;
import ru.smartup.talksscanner.dto.responses.IdeaDtoResponse;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.repos.IdeaRepo;
import ru.smartup.talksscanner.tools.DateFormatter;
import ru.smartup.talksscanner.tools.FilterMapper;
import ru.smartup.talksscanner.tools.IdeaMapper;

import java.time.LocalDateTime;

/**
 * Idea endpoints processing service.
 */
@Service
public class IdeaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaService.class);
    private final IdeaRepo    ideaRepo;
    private final FilterMapper filterMapper;
    private final IdeaMapper  ideaMapper;
    private final AuthUserService authUserService;

    private final SpecificationBuilder specificationBuilder;

    public IdeaService(IdeaRepo ideaRepo, FilterMapper filterMapper, IdeaMapper ideaMapper, AuthUserService authUserService, SpecificationBuilder specificationBuilder) {
        this.ideaRepo = ideaRepo;
        this.filterMapper = filterMapper;
        this.ideaMapper = ideaMapper;
        this.authUserService = authUserService;
        this.specificationBuilder = specificationBuilder;
    }

    public IdeaDtoResponse insertIdea(SaveIdeaDtoRequest request, String login) throws NotFoundEntityException {

        User user = authUserService.getUserByLogin(login);
        Idea idea = ideaMapper.convertToIdea(request);

        LOGGER.info("insert Idea : {} for User : {} ", idea, user);
        idea.setUser(user);

        return ideaMapper.convertToDto(ideaRepo.save(idea));

    }

    public IdeaDtoResponse updateIdea(long idIdea, SaveIdeaDtoRequest request, String login) throws NotFoundEntityException {
        User user = authUserService.getUserByLogin(login);
        Idea idea = getNotDeleteIdeaById(idIdea, user.getId());

        LOGGER.info("update Idea : {} by param : {}", idea, request);
        ideaMapper.updateIdeaByDto(request, idea);

        return ideaMapper.convertToDto( ideaRepo.save(idea) );
    }

    public IdeaDtoResponse removeIdea(long idIdea, String login) throws NotFoundEntityException {
        User user = authUserService.getUserByLogin(login);
        Idea idea = getNotDeleteIdeaById(idIdea, user.getId());

        LOGGER.info("remove Idea : {} by user : {}", idea.getId(), user.getId());
        idea.setStatus(IdeaStatus.DELETED);
        idea.setLastUpdateDate(DateFormatter.dateTimeToFormat(LocalDateTime.now()));

        return ideaMapper.convertToDto( ideaRepo.save(idea) );
    }

    @Transactional(readOnly = true)
    public Page<IdeaDtoResponse> getAllIdeas(GetAllIdeasParamRequest request, Pageable pageable) throws ServiceException {
        LOGGER.info("get Ideas by params : {}", request);

        return ideaRepo.findAll(specificationBuilder.buildSpecifications(filterMapper.convertToFilters(request)), pageable).map(ideaMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public IdeaDtoResponse getIdeaById(long ideaId) throws NotFoundEntityException {
        LOGGER.info("get Idea by id : {}", ideaId);
        return ideaMapper.convertToDto(ideaRepo.findById(ideaId).orElseThrow(
                () -> new NotFoundEntityException(
                        ErrorCode.IDEA_NOT_FOUND,
                        String.format(ErrorCode.IDEA_NOT_FOUND.getTemplate(), ideaId)
                )
            )
        );
    }

    private Idea getNotDeleteIdeaById(Long id, Long userid) throws NotFoundEntityException {
        LOGGER.debug("get Idea with id : {}, by user {}", id, userid);
        return ideaRepo.findByIdAndUserIdAndStatusNot(id, userid, IdeaStatus.DELETED).orElseThrow(
                () -> new NotFoundEntityException(
                        ErrorCode.IDEA_NOT_FOUND,
                        String.format(ErrorCode.IDEA_NOT_FOUND.getTemplate(), id)
                )
        );
    }
}
