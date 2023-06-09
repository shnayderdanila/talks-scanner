package ru.smartup.talksscanner.test_data;

import ru.smartup.talksscanner.domain.*;
import ru.smartup.talksscanner.dto.requests.SaveRateDtoRequest;
import ru.smartup.talksscanner.dto.requests.UpdateRateDtoRequest;
import ru.smartup.talksscanner.dto.responses.RateDtoResponse;

import java.time.LocalDateTime;

public class RateTestData {

    //    Rate
    public static Rate rate(long id, int rate, LocalDateTime lastUpdateDate, LocalDateTime creationDate, User user){
        return new Rate(id, rate, lastUpdateDate, creationDate, user);
    }

    public static Rate rate(User user){
        LocalDateTime date = LocalDateTime.now();
        return rate(1, 4, date, date, user);
    }

    //    SaveRateDtoRequest
    public static SaveRateDtoRequest saveRateDtoRequest(int rate){
        return new SaveRateDtoRequest(rate);
    }

    public static SaveRateDtoRequest saveRateDtoRequest(){
        return saveRateDtoRequest(4);
    }

    //    UpdateRateDtoRequest
    public static UpdateRateDtoRequest updateRateDtoRequest(int rate){
        return new UpdateRateDtoRequest(rate);
    }

    public static UpdateRateDtoRequest updateRateDtoRequest(){
        return updateRateDtoRequest(2);
    }

    //    RateDtoResponse
    public static RateDtoResponse rateDtoResponse(long id, String authorFirstName, String authorLastName, int rate){
        return new RateDtoResponse(id, authorFirstName, authorLastName, rate);
    }

    public static RateDtoResponse rateDtoResponse(){
        return rateDtoResponse(1, "authorFirstName", "authorLastName", 4);
    }

    //    IdeaRate
    public static IdeaRate ideaRate(long id, int rate, LocalDateTime lastUpdateDate, LocalDateTime creationDate, User user, Idea idea){
        return new IdeaRate(id, rate, lastUpdateDate, creationDate, user, idea);
    }

    public static IdeaRate ideaRate(User user, Idea idea){
        LocalDateTime date = LocalDateTime.now();
        return ideaRate(1, 4, date, date, user, idea);
    }

    //   TopicRate
    //    IdeaRate
    public static TopicRate topicRate(long id, int rate, LocalDateTime lastUpdateDate, LocalDateTime creationDate, User user, Topic topic){
        return new TopicRate(id, rate, lastUpdateDate, creationDate, user, topic);
    }

    public static TopicRate topicRate(User user, Topic topic){
        LocalDateTime date = LocalDateTime.now();
        return topicRate(1, 4, date, date, user, topic);
    }
}
