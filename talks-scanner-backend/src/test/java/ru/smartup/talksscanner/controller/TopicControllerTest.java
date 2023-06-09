package ru.smartup.talksscanner.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.smartup.talksscanner.RestPageImpl;
import ru.smartup.talksscanner.config.ApplicationUserService;
import ru.smartup.talksscanner.config.WebSecurityConfiguration;
import ru.smartup.talksscanner.domain.TopicStatus;
import ru.smartup.talksscanner.dto.requests.*;
import ru.smartup.talksscanner.dto.responses.CommentDtoResponse;
import ru.smartup.talksscanner.dto.responses.RateDtoResponse;
import ru.smartup.talksscanner.dto.responses.TopicDtoResponse;
import ru.smartup.talksscanner.handler.GlobalErrorHandler;
import ru.smartup.talksscanner.service.CommentService;
import ru.smartup.talksscanner.service.RateService;
import ru.smartup.talksscanner.service.TopicService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.smartup.talksscanner.test_data.CommentTestData.*;
import static ru.smartup.talksscanner.test_data.RateTestData.*;
import static ru.smartup.talksscanner.test_data.TopicTestData.*;


@Import(WebSecurityConfiguration.class)
@WebMvcTest(TopicController.class)
public class TopicControllerTest {
    @MockBean
    private ApplicationUserService applicationUserService;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private TopicService topicService;

    @MockBean
    private RateService rateService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper mapper;

    private final String baseURL = "/api/v1/topics/";

    private final String commentsURL = "/comments/";
    private final String ratesURL = "/rates/";


    public static Stream<Arguments> getAllTopicsRequest() {
        return Stream.of(
                Arguments.of(new GetAllTopicsParamRequest(null, TopicStatus.ANNOUNCED)),
                Arguments.of(new GetAllTopicsParamRequest(null, null)),
                Arguments.of(new GetAllTopicsParamRequest("java", null)),
                Arguments.of(new GetAllTopicsParamRequest("java", TopicStatus.ANNOUNCED))
        );
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testInsertTopic() throws Exception {
        InsertTopicDtoRequest request = makeInsertTopicDtoRequest("title", "author", "description");
        TopicDtoResponse response = makeTopicDtoResponse(1, "title", "author", "description", null, null, null, null, TopicStatus.ANNOUNCED, 1);

        Mockito.when(topicService.insertTopic(request, "login")).thenReturn(response);
        MvcResult result = post_insertTopic(request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), TopicDtoResponse.class));
    }

    @Test
    public void testInsertTopic_notAuthorized() throws Exception {
        post_insertTopic(makeInsertTopicDtoRequest("title", "author", "description"), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongInsertTopicDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testInsertTopic_wrongData(InsertTopicDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse errors = mapper.readValue(post_insertTopic(request, status().isBadRequest())
                .getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);

        errors.getErrorResponses().forEach((error) -> assertTrue(error.getErrorCode().startsWith("WRONG")));
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testUpdateAnnouncedTopic() throws Exception {
        UpdateAnnouncedTopicDtoRequest updateTopic = makeUpdateAnnouncedTopicDtoRequest("new_title", "new_description");
        TopicDtoResponse updateTopicResponse = makeTopicDtoResponse(1, "new_title", "author", "new_description",
                null, null, null, null, TopicStatus.ANNOUNCED, 1);

        Mockito.when(topicService.updateTopic(updateTopic, "login", 1)).thenReturn(updateTopicResponse);
        MvcResult result = put_updateAnnouncedTopic(updateTopic, status().isOk());

        assertEquals(updateTopicResponse, mapper.readValue(result.getResponse().getContentAsString(), TopicDtoResponse.class));
    }

    @Test
    public void testUpdateAnnouncedTopic_notAuthorized() throws Exception {
        put_updateAnnouncedTopic(makeUpdateAnnouncedTopicDtoRequest("title", "desc"), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongUpdateAnnouncedTopicDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testUpdateAnnouncedTopic_wrongData(UpdateAnnouncedTopicDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse errors = mapper.readValue(put_updateAnnouncedTopic(request, status().isBadRequest())
                .getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);

        errors.getErrorResponses().forEach((error) -> assertTrue(error.getErrorCode().startsWith("WRONG")));
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testUpdateBeingPreparedTopic() throws Exception {
        UpdateBeingPreparedTopicDtoRequest updateTopic = makeUpdateBeingPreparedTopicDtoRequest("new_title", "new_description", "new_tags");
        TopicDtoResponse updateTopicResponse = makeTopicDtoResponse(1, "new_title", "author", "new_description",
                "new_tags", null, null, null, TopicStatus.ANNOUNCED, 1);

        Mockito.when(topicService.updateTopic(updateTopic, "login", 1)).thenReturn(updateTopicResponse);
        MvcResult result = put_updateBeingPreparedTopic(updateTopic, status().isOk());

        assertEquals(updateTopicResponse, mapper.readValue(result.getResponse().getContentAsString(), TopicDtoResponse.class));
    }

    @Test
    public void testUpdateBeingPreparedTopic_notAuthorized() throws Exception {
        put_updateBeingPreparedTopic(makeUpdateBeingPreparedTopicDtoRequest("title", "desc", "tags"), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongUpdateBeingPreparedTopicDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testUpdateBeingPreparedTopic_wrongData(UpdateBeingPreparedTopicDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse errors = mapper.readValue(put_updateBeingPreparedTopic(request, status().isBadRequest())
                .getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);

        errors.getErrorResponses().forEach((error) -> assertTrue(error.getErrorCode().startsWith("WRONG")));
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testUpdateScheduledTopic() throws Exception {
        LocalDateTime newEventDate = LocalDateTime.parse("1986-04-08 12:30:43", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        UpdateScheduledTopicDtoRequest updateTopic = makeUpdateScheduledTopicDtoRequest(newEventDate);
        TopicDtoResponse updateTopicResponse = makeTopicDtoResponse(1, "title", "author", "description",
                "tags", newEventDate, null, null, TopicStatus.ANNOUNCED, 1);

        Mockito.when(topicService.updateTopic(updateTopic, "login", 1)).thenReturn(updateTopicResponse);
        MvcResult result = put_updateScheduledTopic(updateTopic, status().isOk());

        assertEquals(updateTopicResponse, mapper.readValue(result.getResponse().getContentAsString(), TopicDtoResponse.class));
    }

    @Test
    public void testUpdateScheduledTopic_notAuthorized() throws Exception {
        put_updateScheduledTopic(makeUpdateScheduledTopicDtoRequest(LocalDateTime.now()), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongUpdateScheduledTopicDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testUpdateScheduledTopic_wrongData(UpdateScheduledTopicDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse errors = mapper.readValue(put_updateScheduledTopic(request, status().isBadRequest())
                .getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);

        errors.getErrorResponses().forEach((error) -> assertTrue(error.getErrorCode().startsWith("WRONG")));
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testUpdateDoneTopic() throws Exception {
        UpdateDoneTopicDtoRequest updateTopic = makeUpdateDoneTopicDtoRequest("new_presentationLink", "new_videoLink");
        TopicDtoResponse updateTopicResponse = makeTopicDtoResponse(1, "title", "author", "description",
                "tags", LocalDateTime.now(), null, null, TopicStatus.ANNOUNCED, 1);

        Mockito.when(topicService.updateTopic(updateTopic, "login", 1)).thenReturn(updateTopicResponse);
        MvcResult result = put_updateDoneTopic(updateTopic, status().isOk());

        assertEquals(updateTopicResponse, mapper.readValue(result.getResponse().getContentAsString(), TopicDtoResponse.class));
    }

    @Test
    public void testUpdateDoneTopic_notAuthorized() throws Exception {
        put_updateDoneTopic(makeUpdateDoneTopicDtoRequest("presLink", "videoLink"), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongUpdateDoneTopicDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testUpdateDoneTopic_wrongData(UpdateDoneTopicDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse errors = mapper.readValue(put_updateDoneTopic(request, status().isBadRequest())
                .getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);

        errors.getErrorResponses().forEach((error) -> assertTrue(error.getErrorCode().startsWith("WRONG")));
    }

    @ParameterizedTest
    @MethodSource("getAllTopicsRequest")
    @WithMockUser(username = "user", roles = {})
    public void testGetAllIdeas(GetAllTopicsParamRequest request) throws Exception {
        List<TopicDtoResponse> expectedList = List.of(
                makeTopicDtoResponse(1L, null, null, null, "java", null, null, null, TopicStatus.ANNOUNCED, 1L),
                makeTopicDtoResponse(2L, null, null, null, "java", null, null, null, TopicStatus.ANNOUNCED, 1L),
                makeTopicDtoResponse(3L, null, null, null, "java", null, null, null, TopicStatus.ANNOUNCED, 1L)
        );
        Page<TopicDtoResponse> expected = new PageImpl<>(expectedList);

        Mockito.when(topicService.getAllTopics(request, PageRequest.of(0, 10))).thenReturn(expected);

        MvcResult result = getAllTopics(request, status().isOk());

        TypeReference<RestPageImpl<TopicDtoResponse>> type = new TypeReference<>() {};
        Page<TopicDtoResponse> actual = mapper.readValue(result.getResponse().getContentAsString(), type);

        assertEquals(expected.getContent().size(), actual.getContent().size());
        assertTrue(expected.getContent().containsAll(actual.getContent()));
    }

    @Test
    public void testGetAllTopics__unauthorizedUser() throws Exception {
        getAllTopics(new GetAllTopicsParamRequest(null, null), status().isUnauthorized());
    }

    //COMMENTS TESTS
    @Test
    @WithMockUser(username = "login", roles = {})
    public void testInsertTopicComment() throws Exception {
        SaveCommentDtoRequest request = saveCommentDtoRequest();
        CommentDtoResponse response = commentDtoResponse();
        Mockito.when(commentService.saveComment(1, request, "login", EntityType.TOPIC)).thenReturn(response);

        MvcResult result = insertTopicComment(1, request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), CommentDtoResponse.class));
    }

    @Test
    public void testInsertTopicComment_unauthorizedUser() throws Exception {
        insertTopicComment(1, saveCommentDtoRequest(), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongSaveCommentDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testInsertTopicComment_wrongData(SaveCommentDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse exception = mapper.readValue(insertTopicComment(1, request, status().isBadRequest()).getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);
        exception.getErrorResponses().forEach(
                (error) -> assertTrue(error.getErrorCode().startsWith("WRONG"))
        );
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testUpdateTopicComment() throws Exception {
        UpdateCommentDtoRequest request = updateCommentDtoRequest();
        CommentDtoResponse response = commentDtoResponse();
        Mockito.when(commentService.updateComment(1, 1, request, "login", EntityType.TOPIC)).thenReturn(response);

        MvcResult result = updateTopicComment(1, 1, request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), CommentDtoResponse.class));
    }

    @Test
    public void testUpdateTopicComment_unauthorizedUser() throws Exception {
        updateTopicComment(1, 1, updateCommentDtoRequest(), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongUpdateCommentDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testUpdateTopicComment_wrongData(UpdateCommentDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse exception = mapper.readValue(updateTopicComment(1, 1, request, status().isBadRequest()).getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);
        exception.getErrorResponses().forEach(
                (error) -> assertTrue(error.getErrorCode().startsWith("WRONG"))
        );
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testDeleteTopicComment() throws Exception {
        Mockito.doNothing().when(commentService).deleteComment(1, 1, "login", EntityType.IDEA);

        MvcResult result = deleteTopicComment(1, 1, status().isOk());
    }

    @Test
    public void testDeleteIdeaComment_unauthorizedUser() throws Exception {
        deleteTopicComment(1, 1, status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testFindTopicComment() throws Exception {
        CommentDtoResponse response = commentDtoResponse();
        Mockito.when(commentService.findCommentById(1, 1, "login", EntityType.TOPIC)).thenReturn(response);

        MvcResult result = findTopicComment(1, 1, status().isOk());

        System.out.println(result.getResponse().getContentAsString());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), CommentDtoResponse.class));
    }

    @Test
    public void testFindTopicComment_unauthorizedUser() throws Exception {
        findTopicComment(1, 1, status().isUnauthorized());
    }

    // RATES TESTS

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testInsertTopicRate() throws Exception {
        SaveRateDtoRequest request = saveRateDtoRequest();
        RateDtoResponse response = rateDtoResponse();
        Mockito.when(rateService.saveRate(1, request, "login", EntityType.TOPIC)).thenReturn(response);

        MvcResult result = insertTopicRate(1, request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), RateDtoResponse.class));
    }

    @Test
    public void testInsertTopicRate_unauthorizedUser() throws Exception {
        insertTopicRate(1, saveRateDtoRequest(), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongSaveRateDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testInsertIdeaRate_wrongData(SaveRateDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse exception = mapper.readValue(insertTopicRate(1, request, status().isBadRequest()).getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);
        exception.getErrorResponses().forEach(
                (error) -> assertTrue(error.getErrorCode().startsWith("WRONG"))
        );
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testUpdateIdeaRate() throws Exception {
        UpdateRateDtoRequest request = updateRateDtoRequest();
        RateDtoResponse response = rateDtoResponse();
        Mockito.when(rateService.updateRate(1, 1, request, "login", EntityType.TOPIC)).thenReturn(response);

        MvcResult result = updateTopicRate(1, 1, request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), RateDtoResponse.class));
    }

    @Test
    public void testUpdateTopicRate_unauthorizedUser() throws Exception {
        updateTopicRate(1, 1, updateRateDtoRequest(), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongUpdateRateDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testUpdateIdeaRate_wrongData(UpdateRateDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse exception = mapper.readValue(updateTopicRate(1, 1, request, status().isBadRequest()).getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);
        exception.getErrorResponses().forEach(
                (error) -> assertTrue(error.getErrorCode().startsWith("WRONG"))
        );
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testDeleteIdeaRate() throws Exception {
        Mockito.doNothing().when(rateService).deleteRate(1, 1, "login", EntityType.TOPIC);

        deleteTopicRate(1, 1, status().isOk());
    }

    @Test
    public void testDeleteTopicRate_unauthorizedUser() throws Exception {
        deleteTopicRate(1, 1, status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testFindIdeaRate() throws Exception {
        RateDtoResponse response = rateDtoResponse();
        Mockito.when(rateService.findRateById(1, 1, "login", EntityType.TOPIC)).thenReturn(response);

        MvcResult result = findTopicRate(1, 1, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), RateDtoResponse.class));
    }

    @Test
    public void testFindTopicRate_unauthorizedUser() throws Exception {
        findTopicRate(1, 1, status().isUnauthorized());
    }


    private MvcResult post_insertTopic(InsertTopicDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult put_updateAnnouncedTopic(UpdateAnnouncedTopicDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(put( baseURL + "1/announced")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult put_updateBeingPreparedTopic(UpdateBeingPreparedTopicDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(put(baseURL + "1/being-prepared")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult put_updateScheduledTopic(UpdateScheduledTopicDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(put(baseURL + "1/scheduled")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult put_updateDoneTopic(UpdateDoneTopicDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(put(baseURL + "1/done")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult getAllTopics(GetAllTopicsParamRequest request, PageRequest pageRequest, ResultMatcher status) throws Exception {
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();

        Map<String, String> map = mapper.convertValue(request, new TypeReference<>() {});

        map.forEach((key, value) -> { if(value != null) valueMap.add(key, value);});

        return mvc.perform(get(baseURL)
                        .param("page", String.valueOf(pageRequest.getPageNumber()))
                        .param("size", String.valueOf(pageRequest.getPageSize()))
                        .params(valueMap))
                .andExpect(status)
                .andReturn();

    }

    private MvcResult getAllTopics(GetAllTopicsParamRequest request, ResultMatcher status) throws Exception {
        return getAllTopics(request, PageRequest.of(0, 10), status);
    }

    private MvcResult insertTopicComment(long entityId, SaveCommentDtoRequest request, ResultMatcher status) throws Exception{
        return mvc.perform(post(baseURL + entityId + commentsURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult updateTopicComment(long entityId, long id, UpdateCommentDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(put(baseURL + entityId + commentsURL + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult deleteTopicComment(long entityId, long id, ResultMatcher status) throws Exception {
        return mvc.perform(delete(baseURL + entityId + commentsURL + id))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult findTopicComment(long entityid, long id, ResultMatcher status) throws Exception {
        return mvc.perform(get(baseURL + entityid + commentsURL + id))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult insertTopicRate(long entityId, SaveRateDtoRequest request, ResultMatcher status) throws Exception{
        return mvc.perform(post(baseURL + entityId + ratesURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult updateTopicRate(long entityId, long rateId, UpdateRateDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(put(baseURL + entityId + ratesURL + rateId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult deleteTopicRate(long entityId, long rateId, ResultMatcher status) throws Exception {
        return mvc.perform(delete(baseURL + entityId + ratesURL + rateId))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult findTopicRate(long entityId, long rateId, ResultMatcher status) throws Exception {
        return mvc.perform(get(baseURL + entityId + ratesURL + rateId))
                .andExpect(status)
                .andReturn();
    }

    private static Stream<Arguments> makeWrongInsertTopicDtoRequest() {
        return Stream.of(Arguments.arguments(makeInsertTopicDtoRequest("", "author", "desc")),
                Arguments.arguments(makeInsertTopicDtoRequest("title", "", "desc")),
                Arguments.arguments(makeInsertTopicDtoRequest("title", "author", "")),
                Arguments.arguments(makeInsertTopicDtoRequest(null, "author", "desc")),
                Arguments.arguments(makeInsertTopicDtoRequest("title", null, "desc")),
                Arguments.arguments(makeInsertTopicDtoRequest("title", "author", null)));
    }

    private static Stream<Arguments> makeWrongUpdateAnnouncedTopicDtoRequest() {
        return Stream.of(
                        Arguments.arguments(makeUpdateAnnouncedTopicDtoRequest("", "desc")),
                        Arguments.arguments(makeUpdateAnnouncedTopicDtoRequest("title", ""),
                        Arguments.arguments(makeUpdateAnnouncedTopicDtoRequest(null, "desc")),
                        Arguments.arguments(makeUpdateAnnouncedTopicDtoRequest("title", null))));
    }

    private static Stream<Arguments> makeWrongUpdateBeingPreparedTopicDtoRequest() {
        return Stream.of(
                        Arguments.arguments(makeUpdateBeingPreparedTopicDtoRequest("", "desc", "tags")),
                        Arguments.arguments(makeUpdateBeingPreparedTopicDtoRequest("title", "", "tags"),
                        Arguments.arguments(makeUpdateBeingPreparedTopicDtoRequest("title", "desc", "")),
                        Arguments.arguments(makeUpdateBeingPreparedTopicDtoRequest(null, "desc", "tags")),
                        Arguments.arguments(makeUpdateBeingPreparedTopicDtoRequest("title", null, "tags")),
                        Arguments.arguments(makeUpdateBeingPreparedTopicDtoRequest("title", "desc", null))));
    }

    private static Stream<Arguments> makeWrongUpdateScheduledTopicDtoRequest(){
        return Stream.of(
                        Arguments.arguments(makeUpdateScheduledTopicDtoRequest(null))
        );
    }

    private static Stream<Arguments> makeWrongUpdateDoneTopicDtoRequest(){
        return Stream.of(
                Arguments.arguments(makeUpdateDoneTopicDtoRequest("", "videoLink")),
                Arguments.arguments(makeUpdateDoneTopicDtoRequest("presentationLink", "")),
                Arguments.arguments(makeUpdateDoneTopicDtoRequest(null, "videoLink")),
                Arguments.arguments(makeUpdateDoneTopicDtoRequest("presentationLink", null))
        );
    }

    public static Stream<Arguments> makeWrongSaveCommentDtoRequest(){
        return Stream.of(
                Arguments.arguments(saveCommentDtoRequest(null)),
                Arguments.arguments(saveCommentDtoRequest(""))
        );
    }

    public static Stream<Arguments> makeWrongUpdateCommentDtoRequest(){
        return Stream.of(
                Arguments.arguments(updateCommentDtoRequest(null)),
                Arguments.arguments(updateCommentDtoRequest( ""))
        );
    }

    public static Stream<Arguments> makeWrongSaveRateDtoRequest(){
        return Stream.of(
                Arguments.arguments(saveRateDtoRequest(0)),
                Arguments.arguments(saveRateDtoRequest(6))
        );
    }

    public static Stream<Arguments> makeWrongUpdateRateDtoRequest(){
        return Stream.of(
                Arguments.arguments(updateRateDtoRequest(0)),
                Arguments.arguments(updateRateDtoRequest(6))
        );
    }
}