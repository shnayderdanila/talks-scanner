package ru.smartup.talksscanner.test_data;

import ru.smartup.talksscanner.domain.Topic;
import ru.smartup.talksscanner.domain.TopicStatus;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.*;
import ru.smartup.talksscanner.dto.responses.TopicDtoResponse;

import java.time.LocalDateTime;

public class TopicTestData {

    public static InsertTopicDtoRequest makeInsertTopicDtoRequest (String title, String author, String description){
        return new InsertTopicDtoRequest(title, author, description);
    }

    public static TopicDtoResponse makeTopicDtoResponse (long id, String title, String author, String
            description, String tags, LocalDateTime eventDate,
                                                         String presentationLink, String videoLink, TopicStatus status, long userID)
    {
        return new TopicDtoResponse(id, title, author, description, tags, eventDate, presentationLink, videoLink, status, userID, 0.0);
    }

    public static UpdateAnnouncedTopicDtoRequest makeUpdateAnnouncedTopicDtoRequest (String title, String
            description){
        return new UpdateAnnouncedTopicDtoRequest(title, description);
    }

    public static UpdateBeingPreparedTopicDtoRequest makeUpdateBeingPreparedTopicDtoRequest (String title, String
            description, String tags){
        return new UpdateBeingPreparedTopicDtoRequest(title, description, tags);
    }

    public static UpdateScheduledTopicDtoRequest makeUpdateScheduledTopicDtoRequest (LocalDateTime eventDate){
        return new UpdateScheduledTopicDtoRequest(eventDate);
    }

    public static UpdateDoneTopicDtoRequest makeUpdateDoneTopicDtoRequest (String presentationLink, String videoLink)
    {
        return new UpdateDoneTopicDtoRequest(presentationLink, videoLink);
    }

    public static Topic makeTopic (long id, String title, String author, String description, String
            tags, LocalDateTime eventDate,
                                   String presentationLink, String videoLink, TopicStatus status, User user){
        return new Topic(id, title, author, description, tags, eventDate, presentationLink, videoLink, status, user, 0.0);
    }

    public static Topic makeTopic(User user){
        return makeTopic(0, "title", "author", "desc", "tags", LocalDateTime.now(),
                "presLink", "videoLink", TopicStatus.ANNOUNCED, user);
    }
    public static Topic makeTopic(long id, User user){
        return makeTopic(id, "title", "author", "desc", "tags", LocalDateTime.now(),
                "presLink", "videoLink", TopicStatus.ANNOUNCED, user);
    }
}
