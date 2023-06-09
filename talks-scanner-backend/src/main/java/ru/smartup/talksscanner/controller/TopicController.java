package ru.smartup.talksscanner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.smartup.talksscanner.dto.requests.*;
import ru.smartup.talksscanner.dto.responses.CommentDtoResponse;
import ru.smartup.talksscanner.dto.responses.RateDtoResponse;
import ru.smartup.talksscanner.dto.responses.TopicDtoResponse;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.service.CommentService;
import ru.smartup.talksscanner.service.RateService;
import ru.smartup.talksscanner.service.TopicService;

import javax.validation.Valid;

/**
 * Rest controller for topic endpoints.
 */
@RestController
@RequestMapping("/api/v1/topics")
@Tag(name = "Topic API")
public class TopicController {

    private final TopicService topicService;

    private final CommentService commentService;
    private final RateService rateService;

    public TopicController(TopicService topicService, CommentService commentService, RateService rateService) {
        this.topicService = topicService;
        this.commentService = commentService;
        this.rateService = rateService;
    }

    @Operation(summary = "Method insert Topic for User")
    @ApiResponse(responseCode = "200", description = "Topic created")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TopicDtoResponse insertTopic(@RequestBody @Valid InsertTopicDtoRequest request, Authentication data) {
        return topicService.insertTopic(request, data.getName());
    }

    @Operation(summary = "Method updates user topic when topic status is ANNOUNCED")
    @ApiResponse(responseCode = "200", description = "Topic updated")
    @PutMapping(value = "/{topicId}/announced", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TopicDtoResponse updateTopicWhenStatusAnnounced(@RequestBody @Valid UpdateAnnouncedTopicDtoRequest request, Authentication data,
                                                           @PathVariable("topicId") long topicId) throws ServiceException {
        return topicService.updateTopic(request, data.getName(), topicId);
    }

    @Operation(summary = "Method updates user topic when topic status is BEING_PREPARED")
    @ApiResponse(responseCode = "200", description = "Topic updated")
    @PutMapping(value = "/{topicId}/being-prepared", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TopicDtoResponse updateTopicWhenStatusAnnounced(@RequestBody @Valid UpdateBeingPreparedTopicDtoRequest request, Authentication data,
                                                           @PathVariable("topicId") long topicId) throws ServiceException {
        return topicService.updateTopic(request, data.getName(), topicId);
    }

    @Operation(summary = "Method updates user topic when topic status is SCHEDULED")
    @ApiResponse(responseCode = "200", description = "Topic updated")
    @PutMapping(value = "/{topicId}/scheduled", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TopicDtoResponse updateTopicWhenStatusAnnounced(@RequestBody @Valid UpdateScheduledTopicDtoRequest request, Authentication data,
                                                           @PathVariable("topicId") long topicId) throws ServiceException {
        return topicService.updateTopic(request, data.getName(), topicId);
    }

    @Operation(summary = "Method updates user topic when topic status is DONE")
    @ApiResponse(responseCode = "200", description = "Topic updated")
    @PutMapping(value = "/{topicId}/done", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TopicDtoResponse updateTopicWhenStatusAnnounced(@RequestBody @Valid UpdateDoneTopicDtoRequest request, Authentication data,
                                                           @PathVariable("topicId") long topicId) throws ServiceException {
        return topicService.updateTopic(request, data.getName(), topicId);
    }

    @Operation(summary = "Method updates user topic status")
    @ApiResponse(responseCode = "200", description = "Topic updated")
    @PutMapping(value = "/{topicId}/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TopicDtoResponse changeTopicStatus(@RequestBody @Valid ChangeTopicStatusDtoRequest request, Authentication data, @PathVariable("topicId") long topicId) throws ServiceException {
        return topicService.changeTopicStatus(request, data.getName(), topicId);
    }

    @Operation(summary = "Method get all Topics by param")
    @ApiResponse(responseCode = "200", description = "Get all Topics by tags and status phase")
    @GetMapping
    public Page<TopicDtoResponse> getAllTopics(@AuthenticationPrincipal UserDetails userDetails, Pageable pageable, GetAllTopicsParamRequest request) throws ServiceException {
        return topicService.getAllTopics(request, pageable);
    }

    @Operation(summary = "Method get user idea comment")
    @ApiResponse(responseCode = "200", description = "Idea comment found")
    @GetMapping(value = "/{topicId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<CommentDtoResponse> getAllIdeaComments(Pageable pageable,
                                                       @PathVariable("topicId") long topicId,
                                                       Authentication data) throws ServiceException {
        return commentService.getAllComments(topicId, pageable, EntityType.TOPIC);
    }

    @Operation(summary = "Method get Topics by Id")
    @ApiResponse(responseCode = "200", description = "Get Topics by Id")
    @GetMapping("/{topicId}")
    public TopicDtoResponse getTopicById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("topicId") long topicId) throws ServiceException {
        return topicService.getTopicById(topicId);
    }

    @Operation(summary = "Method posts user comment to topic")
    @ApiResponse(responseCode = "200", description = "Topic commented")
    @PostMapping(value = "/{topicId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentDtoResponse insertTopicComment(@PathVariable("topicId") long topicId, @RequestBody @Valid SaveCommentDtoRequest request, Authentication data) throws ServiceException {
        return commentService.saveComment(topicId, request, data.getName(), EntityType.TOPIC);
    }

    @Operation(summary = "Method updates user topic comment")
    @ApiResponse(responseCode = "200", description = "Topic comment updated")
    @PutMapping(value = "/{topicId}/comments/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentDtoResponse updateTopicComment(@PathVariable("commentId") long commentId,
                                                 @PathVariable("topicId") long topicId,
                                                 @RequestBody @Valid UpdateCommentDtoRequest request, Authentication data) throws ServiceException {
        return commentService.updateComment(commentId, topicId, request, data.getName(), EntityType.TOPIC);
    }

    @Operation(summary = "Method deletes user topic comment")
    @ApiResponse(responseCode = "200", description = "Topic comment deleted")
    @DeleteMapping(value = "/{topicId}/comments/{commentId}")
    public void deleteTopicComment(@PathVariable("commentId") long commentId,
                                   @PathVariable("topicId") long topicId,
                                   Authentication data) throws ServiceException {
        commentService.deleteComment(commentId, topicId, data.getName(), EntityType.TOPIC);
    }

    @Operation(summary = "Method get user topic comment")
    @ApiResponse(responseCode = "200", description = "Topic comment found")
    @GetMapping(value = "/{topicId}/comments/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentDtoResponse findTopicComment(@PathVariable("commentId") long commentId,
                                               @PathVariable("topicId") long topicId,
                                               Authentication data) throws ServiceException {
        return commentService.findCommentById(commentId, topicId, data.getName(), EntityType.TOPIC);
    }

    @Operation(summary = "Method adds rate to topic")
    @ApiResponse(responseCode = "200", description = "Topic rate found")
    @PostMapping(value = "/{topicId}/rates", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RateDtoResponse saveTopicRate(@PathVariable("topicId") long topicId,
                                         @RequestBody @Valid SaveRateDtoRequest request,
                                         Authentication data) throws ServiceException {
        return rateService.saveRate(topicId, request, data.getName(), EntityType.TOPIC);
    }

    @Operation(summary = "Method deletes rate from topic")
    @ApiResponse(responseCode = "200", description = "Topic rate deleted")
    @DeleteMapping(value = "/{topicId}/rates/{rateId}")
    public void deleteTopicRate(@PathVariable("topicId") long topicId,
                                @PathVariable("rateId") long rateId,
                                Authentication data) throws ServiceException {
        rateService.deleteRate(topicId, rateId, data.getName(), EntityType.TOPIC);
    }

    @Operation(summary = "Method update topic rate")
    @ApiResponse(responseCode = "200", description = "Topic rate updated")
    @PutMapping(value = "/{topicId}/rates/{rateId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RateDtoResponse updateTopicRate(@PathVariable("topicId") long topicId,
                                           @PathVariable("rateId") long rateId,
                                           @RequestBody @Valid UpdateRateDtoRequest request,
                                           Authentication data) throws ServiceException {
        return rateService.updateRate(topicId, rateId, request, data.getName(), EntityType.TOPIC);
    }

    @Operation(summary = "Method find idea rate")
    @ApiResponse(responseCode = "200", description = "Idea rate found")
    @GetMapping(value = "/{topicId}/rates", produces = MediaType.APPLICATION_JSON_VALUE)
    public RateDtoResponse findIdeaRateUser(@PathVariable("topicId") long topicId,
                                        Authentication data) throws ServiceException {
        return rateService.findRateByUser(topicId, data.getName(), EntityType.TOPIC);
    }

    @Operation(summary = "Method find topic rate")
    @ApiResponse(responseCode = "200", description = "Topic rate found")
    @GetMapping(value = "/{topicId}/rates/{rateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RateDtoResponse findTopicRate(@PathVariable("topicId") long topicId,
                                         @PathVariable("rateId") long rateId,
                                         Authentication data) throws ServiceException {
        return rateService.findRateById(topicId, rateId, data.getName(), EntityType.TOPIC);
    }

}
