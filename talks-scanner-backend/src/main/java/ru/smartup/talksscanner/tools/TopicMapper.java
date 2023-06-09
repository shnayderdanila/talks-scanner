package ru.smartup.talksscanner.tools;

import org.springframework.stereotype.Component;
import ru.smartup.talksscanner.domain.Topic;
import ru.smartup.talksscanner.domain.TopicStatus;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.*;
import ru.smartup.talksscanner.dto.responses.TopicDtoResponse;

import java.time.LocalDateTime;

/**
 * Converts topic model to dto and vice versa.
 */
@Component
public class TopicMapper {

    public Topic toModel(InsertTopicDtoRequest request, User user){
        Topic topic = new Topic();

        topic.setTitle(request.getTitle());
        topic.setAuthor(request.getAuthor());
        topic.setDescription(request.getDescription());
        topic.setUser(user);
        topic.setStatus(TopicStatus.ANNOUNCED);

        return topic;
    }

    public void updateModel(UpdateTopicDtoRequest request, Topic topic){
        TopicStatus status = topic.getStatus();

        if(request instanceof UpdateAnnouncedTopicDtoRequest && status.equals(TopicStatus.ANNOUNCED)){
            String title = ((UpdateAnnouncedTopicDtoRequest) request).getTitle();
            String description = ((UpdateAnnouncedTopicDtoRequest) request).getDescription();

            if(!title.equals(topic.getTitle())){
                topic.setTitle(title);
            }
            if(!description.equals(topic.getDescription())){
                topic.setDescription(description);
            }
            return;
        }

        if(request instanceof UpdateBeingPreparedTopicDtoRequest && status == TopicStatus.BEING_PREPARED) {
            String title = ((UpdateBeingPreparedTopicDtoRequest) request).getTitle();
            String description = ((UpdateBeingPreparedTopicDtoRequest) request).getDescription();
            String tags = ((UpdateBeingPreparedTopicDtoRequest) request).getTags();

            if(!title.equals(topic.getTitle())){
                topic.setTitle(title);
            }
            if(!description.equals(topic.getDescription())){
                topic.setDescription(description);
            }
            if(!tags.equals(topic.getTags())){
                topic.setTags(tags);
            }
            return;
        }

        if(request instanceof UpdateScheduledTopicDtoRequest && status.equals(TopicStatus.SCHEDULED)){
            LocalDateTime eventDate = ((UpdateScheduledTopicDtoRequest) request).getEventDate();

            if(!eventDate.equals(topic.getEventDate())){
                topic.setEventDate(eventDate);
            }
            return;
        }

        if(request instanceof UpdateDoneTopicDtoRequest && status.equals(TopicStatus.DONE)){
            String videoLink = ((UpdateDoneTopicDtoRequest) request).getVideoLink();
            String presentationLink = ((UpdateDoneTopicDtoRequest) request).getPresentationLink();

            if(!videoLink.equals(topic.getVideoLink())){
                topic.setVideoLink(videoLink);
            }
            if(!presentationLink.equals(topic.getPresentationLink())){
                topic.setPresentationLink(presentationLink);
            }
        }
    }


    public TopicDtoResponse toDto(Topic topic){
        TopicDtoResponse response = new TopicDtoResponse();

        response.setId(topic.getId());
        response.setTitle(topic.getTitle());
        response.setAuthor(topic.getAuthor());
        response.setDescription(topic.getDescription());
        response.setEventDate(topic.getEventDate());
        response.setTags(topic.getTags());
        response.setPresentationLink(topic.getPresentationLink());
        response.setVideoLink(topic.getVideoLink());
        response.setUserId(topic.getUser().getId());
        response.setStatus(topic.getStatus());
        response.setRate(topic.getRate());

        return response;
    }
}
