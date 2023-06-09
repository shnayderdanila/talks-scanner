package ru.smartup.talksscanner.test_data;

import ru.smartup.talksscanner.domain.Idea;
import ru.smartup.talksscanner.domain.IdeaStatus;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.GetAllIdeasParamRequest;
import ru.smartup.talksscanner.dto.requests.SaveIdeaDtoRequest;
import ru.smartup.talksscanner.dto.responses.IdeaDtoResponse;
import ru.smartup.talksscanner.dto.responses.UserSimpleDtoResponse;

import java.time.LocalDateTime;

public class IdeaTestData {
    public static Idea idea(long id, String title, String description, User user, LocalDateTime creationDate, LocalDateTime lastUpdateDate, IdeaStatus status) {
        Idea idea = new Idea(title, description, user, creationDate, lastUpdateDate, status);
        idea.setId(id);
        return idea;
    }

    public static Idea idea(long id, User user, IdeaStatus status) {
        return idea(id, "title", "description", user, LocalDateTime.now(), LocalDateTime.now(), status);
    }

    public static Idea idea(long id, User user) {
        return idea(id, "title", "description", user, LocalDateTime.now(), LocalDateTime.now(), IdeaStatus.CREATED);
    }

    public static SaveIdeaDtoRequest saveIdeaDtoRequest(String title, String description) {
        return new SaveIdeaDtoRequest(title, description);
    }

    public static SaveIdeaDtoRequest saveIdeaDtoRequest() {
        return saveIdeaDtoRequest("title", "description");
    }

    public static IdeaDtoResponse ideaResponseDto(long id, String title, String description, long userId, LocalDateTime createTime, LocalDateTime lastUpdateTime, IdeaStatus status) {
        UserSimpleDtoResponse user = new UserSimpleDtoResponse();
        user.setUserId(userId);
        return new IdeaDtoResponse(id, title, description, 0, user, createTime, lastUpdateTime, status);
    }

    public static IdeaDtoResponse ideaResponseDto() {
        return ideaResponseDto(1, "title", "description", 1, LocalDateTime.now(), LocalDateTime.now(), IdeaStatus.CREATED);
    }

    public static IdeaDtoResponse ideaResponseDto(String title, String description) {
        return ideaResponseDto(1, title, description, 1, LocalDateTime.now(), LocalDateTime.now(), IdeaStatus.CREATED);
    }

    public static IdeaDtoResponse ideaResponseDto(long id, String title, String description) {
        return ideaResponseDto(id, title, description, 1, LocalDateTime.now(), LocalDateTime.now(), IdeaStatus.CREATED);
    }
    public static IdeaDtoResponse ideaResponseDto(IdeaStatus status) {
        return ideaResponseDto(1, "title", "description", 1, LocalDateTime.now(), LocalDateTime.now(), status);
    }

    public static GetAllIdeasParamRequest makeGetAllIdeaWithParamDtoRequest(String titleStartWith, String descriptionStartWith) {
        return new GetAllIdeasParamRequest(titleStartWith, descriptionStartWith);
    }
}
