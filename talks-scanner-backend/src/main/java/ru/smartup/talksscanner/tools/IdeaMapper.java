package ru.smartup.talksscanner.tools;

import org.springframework.stereotype.Component;
import ru.smartup.talksscanner.domain.Idea;
import ru.smartup.talksscanner.domain.IdeaStatus;
import ru.smartup.talksscanner.dto.requests.SaveIdeaDtoRequest;
import ru.smartup.talksscanner.dto.responses.IdeaDtoResponse;

import java.time.LocalDateTime;

/**
 * Converts Idea model to dto and vice versa.
 */
@Component
public class IdeaMapper {

    public Idea convertToIdea(SaveIdeaDtoRequest request) {
        Idea idea = new Idea();

        idea.setId(0L);
        idea.setDescription(request.getDescription());
        idea.setTitle(request.getTitle());
        idea.setStatus(IdeaStatus.CREATED);

        LocalDateTime date = DateFormatter.dateTimeToFormat(LocalDateTime.now());
        idea.setCreationDate(date);
        idea.setLastUpdateDate(date);

        return idea;
    }

    public void updateIdeaByDto(SaveIdeaDtoRequest request, Idea idea) {
        idea.setTitle(request.getTitle());
        idea.setDescription(request.getDescription());
        LocalDateTime date = DateFormatter.dateTimeToFormat(LocalDateTime.now());

        idea.setLastUpdateDate(date);

    }

    public IdeaDtoResponse convertToDto(Idea idea) {
        IdeaDtoResponse response = new IdeaDtoResponse();

        response.setId(idea.getId());
        response.setTitle(idea.getTitle());
        response.setDescription(idea.getDescription());
        response.setUserId(idea.getUser().getId());
        response.setUserFirstName(idea.getUser().getFirstname());
        response.setUserLastName(idea.getUser().getLastname());
        response.setStatus(idea.getStatus());
        response.setCreationDate(idea.getCreationDate());
        response.setLastUpdateDate(idea.getLastUpdateDate());
        response.setRate(idea.getRate());

        return response;
    }

}
