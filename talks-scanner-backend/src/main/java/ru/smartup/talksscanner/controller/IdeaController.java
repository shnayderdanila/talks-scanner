package ru.smartup.talksscanner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.smartup.talksscanner.dto.requests.*;
import ru.smartup.talksscanner.dto.responses.CommentDtoResponse;
import ru.smartup.talksscanner.dto.responses.IdeaDtoResponse;
import ru.smartup.talksscanner.dto.responses.RateDtoResponse;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.service.CommentService;
import ru.smartup.talksscanner.service.IdeaService;
import ru.smartup.talksscanner.service.RateService;

import javax.validation.Valid;

/**
 * Rest controller for Idea endpoints
 */
@RestController
@RequestMapping(value = "/api/v1/ideas", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Idea API")
public class IdeaController {
    private final static Logger LOGGER = LoggerFactory.getLogger(IdeaController.class);
    private final IdeaService ideaService;

    private final CommentService commentService;

    private final RateService rateService;

    public IdeaController(IdeaService ideaService, CommentService commentService, RateService rateService) {
        this.ideaService = ideaService;
        this.commentService = commentService;
        this.rateService = rateService;
    }

    @Operation(summary = "Method insert Idea for User")
    @ApiResponse(responseCode = "200", description = "Idea saved")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public IdeaDtoResponse insertUserIdea(@RequestBody @Valid SaveIdeaDtoRequest ideaDtoRequest, @AuthenticationPrincipal UserDetails userDetails) throws  ServiceException {
        LOGGER.info("insert User Idea with request : {}", ideaDtoRequest);
        return ideaService.insertIdea(ideaDtoRequest, userDetails.getUsername());
    }

    @Operation(summary = "Method update Idea by id")
    @ApiResponse(responseCode = "200", description = "Idea updated")
    @PutMapping("/{idIdea}")
    public IdeaDtoResponse updateUserIdea(@PathVariable("idIdea") long idIdea,
                                          @RequestBody @Valid SaveIdeaDtoRequest request,
                                          @AuthenticationPrincipal UserDetails userDetails) throws ServiceException {
        LOGGER.info("update User Idea with id : {}. By request : {}", idIdea, request);
        return ideaService.updateIdea(idIdea, request, userDetails.getUsername());
    }

    @Operation(summary = "Method remove Idea by id")
    @ApiResponse(responseCode = "200", description = "Idea updated")
    @DeleteMapping("/{idIdea}")
    public IdeaDtoResponse removeIdea(@PathVariable("idIdea") long idIdea, @AuthenticationPrincipal UserDetails userDetails) throws ServiceException {
        LOGGER.info("remove Idea with id : {}", idIdea);
        return ideaService.removeIdea(idIdea, userDetails.getUsername());
    }

    @Operation(summary = "Method get all Idea by param")
    @ApiResponse(responseCode = "200", description = "Get all Idea with requesting param")
    @GetMapping
    public Page<IdeaDtoResponse> getAllIdeas(@AuthenticationPrincipal UserDetails userDetails, Pageable pageable, GetAllIdeasParamRequest request) throws ServiceException {
        LOGGER.info("get Ideas with params : {} by User : {}. Page number : {} page size : {}", request, userDetails.getUsername(), pageable.getPageNumber(), pageable.getPageSize());
        return ideaService.getAllIdeas(request, pageable);
    }

    @Operation(summary = "Method get all Idea by param")
    @ApiResponse(responseCode = "200", description = "Get all Idea with requesting param")
    @GetMapping("/{ideaId}")
    public IdeaDtoResponse getIdeaById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("ideaId") long ideaId) throws ServiceException {
        LOGGER.info("get Idea with id : {} by User : {}.", ideaId, userDetails.getUsername());
        return ideaService.getIdeaById(ideaId);
    }

    @Operation(summary = "Method posts user comment to idea")
    @ApiResponse(responseCode = "200", description = "Idea commented")
    @PostMapping(value = "/{ideaId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentDtoResponse insertIdeaComment(@RequestBody @Valid SaveCommentDtoRequest request,
                                                @PathVariable("ideaId") long ideaId,
                                                Authentication data) throws ServiceException {
        return commentService.saveComment(ideaId, request, data.getName(), EntityType.IDEA);
    }

    @Operation(summary = "Method updates user idea comment")
    @ApiResponse(responseCode = "200", description = "Idea comment updated")
    @PutMapping(value = "/{ideaId}/comments/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentDtoResponse updateIdeaComment(@PathVariable("commentId") long commentId,
                                                @PathVariable("ideaId") long ideaId,
                                                @RequestBody @Valid UpdateCommentDtoRequest request, Authentication data) throws ServiceException {
        return commentService.updateComment(commentId, ideaId, request, data.getName(), EntityType.IDEA);
    }

    @Operation(summary = "Method deletes user idea comment")
    @ApiResponse(responseCode = "200", description = "Idea comment deleted")
    @DeleteMapping(value = "/{ideaId}/comments/{commentId}")
    public void deleteIdeaComment(@PathVariable("commentId") long commentId,
                                  @PathVariable("ideaId") long ideaId,
                                  Authentication data) throws ServiceException {
        commentService.deleteComment(commentId, ideaId, data.getName(), EntityType.IDEA);
    }

    @Operation(summary = "Method get user idea comment")
    @ApiResponse(responseCode = "200", description = "Idea comment found")
    @GetMapping(value = "/{ideaId}/comments/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentDtoResponse findIdeaComment(@PathVariable("commentId") long commentId,
                                              @PathVariable("ideaId") long ideaId,
                                              Authentication data) throws ServiceException {
        return commentService.findCommentById(commentId, ideaId, data.getName(), EntityType.IDEA);
    }

    @Operation(summary = "Method get user idea comment")
    @ApiResponse(responseCode = "200", description = "Idea comment found")
    @GetMapping(value = "/{ideaId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<CommentDtoResponse> getAllIdeaComments(Pageable pageable,
                                              @PathVariable("ideaId") long ideaId,
                                              Authentication data) throws ServiceException {
        return commentService.getAllComments(ideaId, pageable, EntityType.IDEA);
    }


    @Operation(summary = "Method adds rate to idea")
    @ApiResponse(responseCode = "200", description = "Idea rate found")
    @PostMapping(value = "/{ideaId}/rates", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RateDtoResponse saveIdeaRate(@PathVariable("ideaId") long ideaId,
                                         @RequestBody @Valid SaveRateDtoRequest request,
                                         Authentication data) throws ServiceException {
        return rateService.saveRate(ideaId, request, data.getName(), EntityType.IDEA);
    }

    @Operation(summary = "Method deletes idea rate")
    @ApiResponse(responseCode = "200", description = "Idea rate deleted")
    @DeleteMapping(value = "/{ideaId}/rates/{rateId}")
    public void deleteIdeaRate(@PathVariable("ideaId") long ideaId,
                                @PathVariable("rateId") long rateId,
                                Authentication data) throws ServiceException {
        rateService.deleteRate(ideaId, rateId, data.getName(), EntityType.IDEA);
    }

    @Operation(summary = "Method update idea rate")
    @ApiResponse(responseCode = "200", description = "Idea rate updated")
    @PutMapping(value = "/{ideaId}/rates/{rateId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RateDtoResponse updateIdeaRate(@PathVariable("ideaId") long ideaId,
                                           @PathVariable("rateId") long rateId,
                                           @RequestBody @Valid UpdateRateDtoRequest request,
                                           Authentication data) throws ServiceException {
        return rateService.updateRate(ideaId, rateId, request, data.getName(), EntityType.IDEA);
    }

    @Operation(summary = "Method find idea rate")
    @ApiResponse(responseCode = "200", description = "Idea rate found")
    @GetMapping(value = "/{ideaId}/rates/{rateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RateDtoResponse findIdeaRate(@PathVariable("ideaId") long ideaId,
                                         @PathVariable("rateId") long rateId,
                                         Authentication data) throws ServiceException {
        return rateService.findRateById(ideaId, rateId, data.getName(), EntityType.IDEA);
    }

    @Operation(summary = "Method find idea rate")
    @ApiResponse(responseCode = "200", description = "Idea rate found")
    @GetMapping(value = "/{ideaId}/rates", produces = MediaType.APPLICATION_JSON_VALUE)
    public RateDtoResponse findIdeaRateUser(@PathVariable("ideaId") long ideaId,
                                        Authentication data) throws ServiceException {
        return rateService.findRateByUser(ideaId, data.getName(), EntityType.IDEA);
    }
}
