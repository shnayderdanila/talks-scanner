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
import ru.smartup.talksscanner.domain.IdeaStatus;
import ru.smartup.talksscanner.dto.requests.*;
import ru.smartup.talksscanner.dto.responses.CommentDtoResponse;
import ru.smartup.talksscanner.dto.responses.IdeaDtoResponse;
import ru.smartup.talksscanner.dto.responses.RateDtoResponse;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.handler.GlobalErrorHandler;
import ru.smartup.talksscanner.service.CommentService;
import ru.smartup.talksscanner.service.IdeaService;
import ru.smartup.talksscanner.service.RateService;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.smartup.talksscanner.test_data.CommentTestData.*;
import static ru.smartup.talksscanner.test_data.IdeaTestData.*;
import static ru.smartup.talksscanner.test_data.RateTestData.*;

@Import(WebSecurityConfiguration.class)
@WebMvcTest(IdeaController.class)
public class TestIdeaController {

    @MockBean
    private ApplicationUserService applicationUserService;

    @MockBean
    private IdeaService ideaService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private RateService rateService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private final String baseURL = "/api/v1/ideas/";

    private final String commentsURL = "/comments/";
    private final String ratesURL = "/rates/";

    public static Stream<Arguments> saveIdea__invalidData() {
        return Stream.of(
                Arguments.arguments(saveIdeaDtoRequest("", "")),
                Arguments.arguments(saveIdeaDtoRequest("title", "")),
                Arguments.arguments(saveIdeaDtoRequest("", "description")),
                Arguments.arguments(saveIdeaDtoRequest("           ", "description")),
                Arguments.arguments(saveIdeaDtoRequest("title", "              ")),
                Arguments.arguments(saveIdeaDtoRequest(null, "desc")),
                Arguments.arguments(saveIdeaDtoRequest("null", null))
        );
    }

    public static Stream<Arguments> getAllIdeasRequest() {
        return Stream.of(
                Arguments.of(makeGetAllIdeaWithParamDtoRequest("t", null)),
                Arguments.of(makeGetAllIdeaWithParamDtoRequest(null, "u")),
                Arguments.of(makeGetAllIdeaWithParamDtoRequest("t", "u"))
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

    @Test
    @WithMockUser(value="login", roles = {})
    public void testInsertIdea() throws Exception {
        SaveIdeaDtoRequest request = saveIdeaDtoRequest();
        IdeaDtoResponse response = ideaResponseDto();

        Mockito.when(ideaService.insertIdea(request, "login")).thenReturn(response);

        MvcResult result = postIdea(request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), IdeaDtoResponse.class));
    }

    @Test
    @WithMockUser(value="login", roles = {})
    public void testInsertIdea__sendOneRequestTwice__byOneUser() throws Exception {
        SaveIdeaDtoRequest request = saveIdeaDtoRequest();
        IdeaDtoResponse firstResponse = ideaResponseDto(1, "title", "description");
        IdeaDtoResponse secondResponse = ideaResponseDto(2, "title", "description");

        Mockito.when(ideaService.insertIdea(request, "login")).thenReturn(firstResponse).thenReturn(secondResponse);
        MvcResult firstResult = postIdea(request, status().isOk());
        MvcResult secondResult = postIdea(request, status().isOk());

        assertEquals(firstResponse, mapper.readValue(firstResult.getResponse().getContentAsString(), IdeaDtoResponse.class));
        assertEquals(secondResponse, mapper.readValue(secondResult.getResponse().getContentAsString(), IdeaDtoResponse.class));
    }

    @Test
    @WithMockUser(value="login", roles = {})
    public void testInsertIdea__sendOneRequestTwice__byOtherUsers() throws Exception {
        SaveIdeaDtoRequest request = saveIdeaDtoRequest();
        IdeaDtoResponse firstResponse = ideaResponseDto(1, "title", "description");
        IdeaDtoResponse secondResponse = ideaResponseDto(2, "title", "description");

        Mockito.when(ideaService.insertIdea(request, "login")).thenReturn(firstResponse).thenReturn(secondResponse);
        MvcResult firstResult = postIdea(request, status().isOk());
        MvcResult secondResult = postIdea(request, status().isOk());

        assertEquals(firstResponse, mapper.readValue(firstResult.getResponse().getContentAsString(), IdeaDtoResponse.class));
        assertEquals(secondResponse, mapper.readValue(secondResult.getResponse().getContentAsString(), IdeaDtoResponse.class));
    }

    @Test
    public void testInsertIdea__unauthorizedUser() throws Exception {
        postIdea(saveIdeaDtoRequest(), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("saveIdea__invalidData")
    @WithMockUser(value = "login", roles = {})
    public void testInsertIdea__notValidData(SaveIdeaDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse exception = mapper.readValue(postIdea(request, status().isBadRequest()).getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);
        exception.getErrorResponses().forEach(
                (error) -> assertTrue(error.getErrorCode().startsWith("WRONG"))
        );
    }

    @ParameterizedTest
    @MethodSource("saveIdea__invalidData")
    @WithMockUser(value = "login", roles = {})
    public void testUpdateIdea__notValidData(SaveIdeaDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse exception = mapper.readValue(updateIdea(1, request, status().isBadRequest()).getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);
        exception.getErrorResponses().forEach(
                (error) -> assertTrue(error.getErrorCode().startsWith("WRONG"))
        );
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testUpdateIdea() throws Exception {
        SaveIdeaDtoRequest request = saveIdeaDtoRequest();
        IdeaDtoResponse response = ideaResponseDto("titleUpdate", "descriptionUpdate");

        Mockito.when(ideaService.updateIdea(1, request, "login")).thenReturn(response);
        MvcResult result = updateIdea(1, request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), IdeaDtoResponse.class));

    }

    @Test
    public void testUpdateIdea__unauthorizedUser() throws Exception {
        updateIdea(1, saveIdeaDtoRequest(), status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testRemoveIdea__oneIdeaTwice() throws Exception {
        IdeaDtoResponse response = ideaResponseDto(IdeaStatus.DELETED);
        Mockito.when(ideaService.removeIdea(1, "login")).thenReturn(response).thenThrow(new NotFoundEntityException(ErrorCode.IDEA_NOT_FOUND, ""));

        MvcResult resultWithIdeaResponse = deleteIdea(1, status().isOk());
        MvcResult resultWithErrorResponse = deleteIdea(1, status().isNotFound());

        assertAll(
                () -> {
                    assertEquals(response, mapper.readValue(resultWithIdeaResponse.getResponse().getContentAsString(), IdeaDtoResponse.class));
                    assertEquals(ErrorCode.IDEA_NOT_FOUND.toString(), mapper.readValue(resultWithErrorResponse.getResponse().getContentAsString(), GlobalErrorHandler.ErrorResponse.class).getErrorCode());
                }
        );
    }

    @Test
    public void testRemoveIdea__unauthorizedUser() throws Exception {
        deleteIdea(1,  status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "owner", roles = {})
    public void testRemoveIdea__byNotOwner() throws Exception {
        Mockito.when(ideaService.removeIdea(1, "owner")).thenThrow(new NotFoundEntityException(ErrorCode.IDEA_NOT_FOUND, ""));

        MvcResult resultWithErrorResponse = deleteIdea(1, status().isNotFound());

        assertEquals(ErrorCode.IDEA_NOT_FOUND.toString(), mapper.readValue(resultWithErrorResponse.getResponse().getContentAsString(), GlobalErrorHandler.ErrorResponse.class).getErrorCode());
    }

    @ParameterizedTest
    @MethodSource("getAllIdeasRequest")
    @WithMockUser(username = "user", roles = {})
    public void testGetAllIdeas(GetAllIdeasParamRequest request) throws Exception {
        List<IdeaDtoResponse> expectedList = List.of(ideaResponseDto("t", "u"), ideaResponseDto("ti", "up"), ideaResponseDto("tit", "upd"));
        Page<IdeaDtoResponse> expected = new PageImpl<>(expectedList);

        Mockito.when(ideaService.getAllIdeas(request, PageRequest.of(0, 10))).thenReturn(expected);

        MvcResult result = getAllIdeas(request, status().isOk());

        TypeReference<RestPageImpl<IdeaDtoResponse>> type = new TypeReference<>() {};
        Page<IdeaDtoResponse> actual = mapper.readValue(result.getResponse().getContentAsString(), type);

        assertEquals(expected.getContent().size(), actual.getContent().size());
        assertTrue(expected.getContent().containsAll(actual.getContent()));
    }

    @Test
    public void testGetAllIdeas__unauthorizedUser() throws Exception {
        getAllIdeas(makeGetAllIdeaWithParamDtoRequest(null, null), status().isUnauthorized());
    }

    // COMMENTS TESTS
    @Test
    @WithMockUser(username = "login", roles = {})
    public void testInsertIdeaComment() throws Exception {
        SaveCommentDtoRequest request = saveCommentDtoRequest();
        CommentDtoResponse response = commentDtoResponse();
        Mockito.when(commentService.saveComment(1, request, "login", EntityType.IDEA)).thenReturn(response);

        MvcResult result = insertIdeaComment(1, request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), CommentDtoResponse.class));
    }

    @Test
    public void testInsertIdeaComment_unauthorizedUser() throws Exception {
        insertIdeaComment(1, saveCommentDtoRequest(), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongSaveCommentDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testInsertIdeaComment_wrongData(SaveCommentDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse exception = mapper.readValue(insertIdeaComment(1, request, status().isBadRequest()).getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);
        exception.getErrorResponses().forEach(
                (error) -> assertTrue(error.getErrorCode().startsWith("WRONG"))
        );
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testUpdateIdeaComment() throws Exception {
        UpdateCommentDtoRequest request = updateCommentDtoRequest();
        CommentDtoResponse response = commentDtoResponse();
        Mockito.when(commentService.updateComment(1, 1, request, "login", EntityType.IDEA)).thenReturn(response);

        MvcResult result = updateIdeaComment(1, 1, request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), CommentDtoResponse.class));
    }

    @Test
    public void testUpdateIdeaComment_unauthorizedUser() throws Exception {
        updateIdeaComment(1, 1, updateCommentDtoRequest(), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongUpdateCommentDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testUpdateIdeaComment_wrongData(UpdateCommentDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse exception = mapper.readValue(updateIdeaComment(1, 1, request, status().isBadRequest()).getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);
        exception.getErrorResponses().forEach(
                (error) -> assertTrue(error.getErrorCode().startsWith("WRONG"))
        );
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testDeleteIdeaComment() throws Exception {
        Mockito.doNothing().when(commentService).deleteComment(1, 1, "login", EntityType.IDEA);

        deleteIdeaComment(1, 1, status().isOk());
    }

    @Test
    public void testDeleteIdeaComment_unauthorizedUser() throws Exception {
        deleteIdeaComment(1, 1, status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testFindIdeaComment() throws Exception {
        CommentDtoResponse response = commentDtoResponse();
        Mockito.when(commentService.findCommentById(1, 1, "login", EntityType.IDEA)).thenReturn(response);

        MvcResult result = findIdeaComment(1, 1, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), CommentDtoResponse.class));
    }

    @Test
    public void testFindIdeaComment_unauthorizedUser() throws Exception {
        findIdeaComment(1, 1, status().isUnauthorized());
    }

    // RATES TESTS

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testInsertIdeaRate() throws Exception {
        SaveRateDtoRequest request = saveRateDtoRequest();
        RateDtoResponse response = rateDtoResponse();
        Mockito.when(rateService.saveRate(1, request, "login", EntityType.IDEA)).thenReturn(response);

        MvcResult result = insertIdeaRate(1, request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), RateDtoResponse.class));
    }

    @Test
    public void testInsertIdeaRate_unauthorizedUser() throws Exception {
        insertIdeaRate(1, saveRateDtoRequest(), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongSaveRateDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testInsertIdeaRate_wrongData(SaveRateDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse exception = mapper.readValue(insertIdeaRate(1, request, status().isBadRequest()).getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);
        exception.getErrorResponses().forEach(
                (error) -> assertTrue(error.getErrorCode().startsWith("WRONG"))
        );
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testUpdateIdeaRate() throws Exception {
        UpdateRateDtoRequest request = updateRateDtoRequest();
        RateDtoResponse response = rateDtoResponse();
        Mockito.when(rateService.updateRate(1, 1, request, "login", EntityType.IDEA)).thenReturn(response);

        MvcResult result = updateIdeaRate(1, 1, request, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), RateDtoResponse.class));
    }

    @Test
    public void testUpdateIdeaRate_unauthorizedUser() throws Exception {
        updateIdeaRate(1, 1, updateRateDtoRequest(), status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("makeWrongUpdateRateDtoRequest")
    @WithMockUser(username = "login", roles = {})
    public void testUpdateIdeaRate_wrongData(UpdateRateDtoRequest request) throws Exception {
        GlobalErrorHandler.ErrorsResponse exception = mapper.readValue(updateIdeaRate(1, 1, request, status().isBadRequest()).getResponse().getContentAsString(), GlobalErrorHandler.ErrorsResponse.class);
        exception.getErrorResponses().forEach(
                (error) -> assertTrue(error.getErrorCode().startsWith("WRONG"))
        );
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testDeleteIdeaRate() throws Exception {
        Mockito.doNothing().when(rateService).deleteRate(1, 1, "login", EntityType.IDEA);

        MvcResult result = deleteIdeaRate(1, 1, status().isOk());
    }

    @Test
    public void testDeleteIdeaRate_unauthorizedUser() throws Exception {
        deleteIdeaRate(1, 1, status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "login", roles = {})
    public void testFindIdeaRate() throws Exception {
        RateDtoResponse response = rateDtoResponse();
        Mockito.when(rateService.findRateById(1, 1, "login", EntityType.IDEA)).thenReturn(response);

        MvcResult result = findIdeaRate(1, 1, status().isOk());

        assertEquals(response, mapper.readValue(result.getResponse().getContentAsString(), RateDtoResponse.class));
    }

    @Test
    public void testFindIdeaRate_unauthorizedUser() throws Exception {
        findIdeaRate(1, 1, status().isUnauthorized());
    }

    private MvcResult postIdea(SaveIdeaDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(post(baseURL)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult updateIdea(long id, SaveIdeaDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(put(baseURL + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult deleteIdea(long id, ResultMatcher status) throws Exception {
        return mvc.perform(delete(baseURL + id)).andExpect(status).andReturn();
    }
    private MvcResult getAllIdeas(GetAllIdeasParamRequest request, PageRequest pageRequest, ResultMatcher status) throws Exception {
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();

        Map<String, String> map = mapper.convertValue(request, new TypeReference<>() {});

        map.forEach((key, value) -> { if(value != null) valueMap.add(key, value);});

        return valueMap.isEmpty() ?
                mvc.perform(get(baseURL))
                  .andExpect(status)
                  .andReturn()
                : mvc.perform(get(baseURL)
                    .param("page", String.valueOf(pageRequest.getPageNumber()))
                    .param("size", String.valueOf(pageRequest.getPageSize()))
                    .params(valueMap))
                    .andExpect(status)
                    .andReturn();

    }

    private MvcResult getAllIdeas(GetAllIdeasParamRequest request, ResultMatcher status) throws Exception {
        return getAllIdeas(request, PageRequest.of(0, 10), status);
    }

    private MvcResult insertIdeaComment(long entityId, SaveCommentDtoRequest request, ResultMatcher status) throws Exception{
        return mvc.perform(post(baseURL + entityId + commentsURL)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult updateIdeaComment(long entityId, long commentId, UpdateCommentDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(put(baseURL + entityId + commentsURL + commentId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult deleteIdeaComment(long entityId, long commentId, ResultMatcher status) throws Exception {
        return mvc.perform(delete(baseURL + entityId + commentsURL + commentId))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult findIdeaComment(long entityId, long commentId, ResultMatcher status) throws Exception {
        return mvc.perform(get(baseURL + entityId + commentsURL + commentId))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult insertIdeaRate(long entityId, SaveRateDtoRequest request, ResultMatcher status) throws Exception{
        return mvc.perform(post(baseURL + entityId + ratesURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult updateIdeaRate(long entityId, long rateId, UpdateRateDtoRequest request, ResultMatcher status) throws Exception {
        return mvc.perform(put(baseURL + entityId + ratesURL + rateId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult deleteIdeaRate(long entityId, long rateId, ResultMatcher status) throws Exception {
        return mvc.perform(delete(baseURL + entityId + ratesURL + rateId))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult findIdeaRate(long entityId, long rateId, ResultMatcher status) throws Exception {
        return mvc.perform(get(baseURL + entityId + ratesURL + rateId))
                .andExpect(status)
                .andReturn();
    }
}