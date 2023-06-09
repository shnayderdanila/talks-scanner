package ru.smartup.talksscanner.tools;

import org.springframework.stereotype.Component;
import ru.smartup.talksscanner.domain.*;
import ru.smartup.talksscanner.dto.requests.SaveCommentDtoRequest;
import ru.smartup.talksscanner.dto.requests.UpdateCommentDtoRequest;
import ru.smartup.talksscanner.dto.responses.CommentDtoResponse;
import ru.smartup.talksscanner.exception.ServiceException;

import java.time.LocalDateTime;

/**
 * Converts Comments to dto and vice versa.
 */
@Component
public class CommentMapper {
    public IdeaComment toModel(SaveCommentDtoRequest request, User user, Idea idea) throws ServiceException {
        IdeaComment comment = new IdeaComment();

        comment.setId(0);
        LocalDateTime creationDate = LocalDateTime.now();
        comment.setCreationDate(creationDate);
        comment.setLastUpdateDate(creationDate);
        comment.setText(request.getText());
        comment.setUser(user);
        comment.setIdea(idea);
        return comment;
    }

    public TopicComment toModel(SaveCommentDtoRequest request, User user, Topic topic) throws ServiceException {
        TopicComment comment = new TopicComment();

        comment.setId(0);
        LocalDateTime creationDate = LocalDateTime.now();
        comment.setCreationDate(creationDate);
        comment.setLastUpdateDate(creationDate);
        comment.setText(request.getText());
        comment.setUser(user);
        comment.setTopic(topic);
        return comment;
    }


    public CommentDtoResponse toDto(Comment comment) {
        CommentDtoResponse commentDtoResponse = new CommentDtoResponse();

        commentDtoResponse.setId(comment.getId());
        commentDtoResponse.setText(comment.getText());
        commentDtoResponse.setAuthorFirstName(comment.getUser().getFirstname());
        commentDtoResponse.setAuthorLastName(comment.getUser().getLastname());
        commentDtoResponse.setUserId(comment.getUser().getId());
        return commentDtoResponse;
    }

    public void updateCommentByDto(UpdateCommentDtoRequest request, Comment comment) {
        comment.setText(request.getText());
        comment.setLastUpdateDate(LocalDateTime.now());
    }
}
