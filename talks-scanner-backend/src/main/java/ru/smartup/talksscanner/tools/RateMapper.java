package ru.smartup.talksscanner.tools;

import org.springframework.stereotype.Component;
import ru.smartup.talksscanner.domain.*;
import ru.smartup.talksscanner.dto.requests.SaveRateDtoRequest;
import ru.smartup.talksscanner.dto.requests.UpdateRateDtoRequest;
import ru.smartup.talksscanner.dto.responses.RateDtoResponse;

import java.time.LocalDateTime;

@Component
public class RateMapper {
    public IdeaRate toModel(SaveRateDtoRequest request, User user, Idea idea){
        IdeaRate ideaRate = new IdeaRate();

        ideaRate.setRate(request.getRate());
        LocalDateTime now = LocalDateTime.now();
        ideaRate.setCreationDate(now);
        ideaRate.setLastUpdateDate(now);
        ideaRate.setUser(user);
        ideaRate.setIdea(idea);
        return ideaRate;
    }

    public TopicRate toModel(SaveRateDtoRequest request, User user, Topic topic){
        TopicRate topicRate = new TopicRate();

        topicRate.setRate(request.getRate());
        LocalDateTime now = LocalDateTime.now();
        topicRate.setCreationDate(now);
        topicRate.setLastUpdateDate(now);
        topicRate.setUser(user);
        topicRate.setTopic(topic);
        return topicRate;
    }

    public RateDtoResponse toDto(Rate rate){
        if (rate == null) {
            return new RateDtoResponse();
        }
        RateDtoResponse rateDtoResponse = new RateDtoResponse();

        rateDtoResponse.setId(rate.getId());
        rateDtoResponse.setRate(rate.getRate());
        rateDtoResponse.setAuthorFirstName(rate.getUser().getFirstname());
        rateDtoResponse.setAuthorLastName(rate.getUser().getLastname());
        return rateDtoResponse;
    }

    public void updateRateByDto(Rate rate, UpdateRateDtoRequest request){
        rate.setRate(request.getRate());
        rate.setLastUpdateDate(LocalDateTime.now());
    }
}
