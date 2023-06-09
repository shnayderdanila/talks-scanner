package ru.smartup.talksscanner.test_data;

import ru.smartup.talksscanner.domain.*;
import ru.smartup.talksscanner.dto.requests.SaveCommentDtoRequest;
import ru.smartup.talksscanner.dto.requests.UpdateCommentDtoRequest;
import ru.smartup.talksscanner.dto.responses.CommentDtoResponse;

import java.time.LocalDateTime;

public class CommentTestData {

    //    comment
    public static Comment makeComment(long id, String text, LocalDateTime lastUpdateDate, LocalDateTime creationDate, User user){
        return new Comment(id, text, lastUpdateDate, creationDate, user);
    }

    public static Comment makeComment(User user){
        LocalDateTime date = LocalDateTime.now();
        return makeComment(0, "text", date, date, user);
    }

    public static Comment makeComment(User user, LocalDateTime date){
        return makeComment(0, "text", date, date, user);
    }

    //    saveCommentDtoRequest

    public static SaveCommentDtoRequest saveCommentDtoRequest(String text){
        return new SaveCommentDtoRequest(text);
    }


    public static SaveCommentDtoRequest saveCommentDtoRequest(){
        return saveCommentDtoRequest("text");
    }


    //     commentDtoResponse
    public static CommentDtoResponse commentDtoResponse(long id, String authorFirstName, String authorLastName, String text){
        return new CommentDtoResponse(id, authorFirstName, authorLastName, text, 1);
    }

    public static CommentDtoResponse commentDtoResponse(){
        return commentDtoResponse(1, "authorFirstName", "authorLastName", "text");
    }


    // updateCommentDtoRequest
    public static UpdateCommentDtoRequest updateCommentDtoRequest(String text){
        return new UpdateCommentDtoRequest(text);
    }

    public static UpdateCommentDtoRequest updateCommentDtoRequest(){
        return updateCommentDtoRequest("new_text");
    }



    //ideaComment
    public static IdeaComment ideaComment(long id, String text, LocalDateTime lastUpdateDate, LocalDateTime creationDate, User user, Idea idea){
        return new IdeaComment(id, text, lastUpdateDate, creationDate, user, idea);
    }

    public static IdeaComment ideaComment(User user, Idea idea){
        LocalDateTime date = LocalDateTime.now();
        return new IdeaComment(0, "text", date, date, user, idea);
    }

    public static IdeaComment ideaComment(User user, Idea idea, LocalDateTime date){
        return new IdeaComment(0, "text", date, date, user, idea);
    }

    // topicComment
    public static TopicComment topicComment(long id, String text, LocalDateTime lastUpdateDate, LocalDateTime creationDate, User user, Topic topic){
        return new TopicComment(id, text, lastUpdateDate, creationDate, user, topic);
    }

    public static TopicComment topicComment(User user, Topic topic){
        LocalDateTime date = LocalDateTime.now();
        return topicComment(0, "text", date, date, user, topic);
    }

    public static TopicComment topicComment(User user, Topic topic, LocalDateTime date){
        return new TopicComment(0, "text", date, date, user, topic);
    }
}
