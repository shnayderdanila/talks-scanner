package ru.smartup.talksscanner.tools;

import org.junit.jupiter.api.Test;
import ru.smartup.talksscanner.domain.Sex;
import ru.smartup.talksscanner.domain.Topic;
import ru.smartup.talksscanner.domain.TopicStatus;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.requests.UpdateAnnouncedTopicDtoRequest;
import ru.smartup.talksscanner.dto.requests.UpdateBeingPreparedTopicDtoRequest;
import ru.smartup.talksscanner.dto.requests.UpdateDoneTopicDtoRequest;
import ru.smartup.talksscanner.dto.requests.UpdateScheduledTopicDtoRequest;
import ru.smartup.talksscanner.exception.ServiceException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.smartup.talksscanner.test_data.TopicTestData.*;
import static ru.smartup.talksscanner.test_data.UserTestData.user;

public class TopicMapperTest {

    private User user = user(1 ,"email", "login", "firstName", "lastName", Sex.MALE, "pathLogo");

    private TopicMapper topicMapper = new TopicMapper();

    @Test
    public void testUpdateUserTopic_whenTopicStatusAnnounced_updatesTitleAndDescription() throws ServiceException {
        UpdateAnnouncedTopicDtoRequest request = makeUpdateAnnouncedTopicDtoRequest("new_title", "new_description");

        Topic actual = makeTopic(1, "title", "author", "description", null, null, null, null, TopicStatus.ANNOUNCED, user);
        Topic expected = makeTopic(1, "new_title", "author", "new_description", null, null, null, null, TopicStatus.ANNOUNCED, user);

        topicMapper.updateModel(request, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUserTopic_whenTopicStatusNotAnnounced_tryUpdateTitleAndDescription(){
        UpdateAnnouncedTopicDtoRequest request = makeUpdateAnnouncedTopicDtoRequest("new_title", "new_description");
        LocalDateTime eventDate = LocalDateTime.now();

        Topic actual = makeTopic(1, "title", "author", "description", "tags", eventDate, null, null, TopicStatus.SCHEDULED, user);
        Topic expected = makeTopic(1, "title", "author", "description", "tags", eventDate, null, null, TopicStatus.SCHEDULED, user);

        topicMapper.updateModel(request, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUserTopic_whenTopicStatusBeingPrepared_updatesTitleDescriptionAndTags() throws ServiceException {
        UpdateBeingPreparedTopicDtoRequest request = makeUpdateBeingPreparedTopicDtoRequest("new_title", "new_description", "new_tags");

        Topic actual = makeTopic(1, "title", "author", "description", "tags", null, null, null, TopicStatus.BEING_PREPARED, user);
        Topic expected = makeTopic(1, "new_title", "author", "new_description", "new_tags", null, null, null, TopicStatus.BEING_PREPARED, user);

        topicMapper.updateModel(request, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUserTopic_whenTopicStatusNotBeingPrepared_tryUpdateTitleDescriptionAndTags(){
        UpdateBeingPreparedTopicDtoRequest request = makeUpdateBeingPreparedTopicDtoRequest("new_title", "new_description", "new_tags");
        LocalDateTime eventDate = LocalDateTime.now();

        Topic actual = makeTopic(1, "title", "author", "description", "tags", eventDate, null, null, TopicStatus.SCHEDULED, user);
        Topic expected = makeTopic(1, "title", "author", "description", "tags", eventDate, null, null, TopicStatus.SCHEDULED, user);

        topicMapper.updateModel(request, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUserTopic_whenTopicStatusScheduled_updatesEventDate() throws ServiceException {
        LocalDateTime prevEventDate = LocalDateTime.now();
        LocalDateTime newEventDate = LocalDateTime.now();
        UpdateScheduledTopicDtoRequest request = makeUpdateScheduledTopicDtoRequest(newEventDate);

        Topic actual = makeTopic(1, "title", "author", "description", "tags", prevEventDate, null, null, TopicStatus.SCHEDULED, user);
        Topic expected = makeTopic(1, "title", "author", "description", "tags", newEventDate, null, null, TopicStatus.SCHEDULED, user);

        topicMapper.updateModel(request, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUserTopic_whenTopicStatusNotScheduled_tryUpdateEventDate(){
        LocalDateTime eventDate = LocalDateTime.now();
        UpdateScheduledTopicDtoRequest request = makeUpdateScheduledTopicDtoRequest(eventDate);

        Topic actual = makeTopic(1, "title", "author", "description", "tags", null, null, null, TopicStatus.BEING_PREPARED, user);
        Topic expected = makeTopic(1, "title", "author", "description", "tags", null, null, null, TopicStatus.BEING_PREPARED, user);

        topicMapper.updateModel(request, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUserTopic_whenTopicStatusDone_updatesVideoLinkAndPresentationLink() throws ServiceException {
        LocalDateTime eventDate = LocalDateTime.now();

        UpdateDoneTopicDtoRequest request = makeUpdateDoneTopicDtoRequest("new_presentationLink", "new_videoLink");

        Topic actual = makeTopic(1, "title", "author", "description", "tags", eventDate, "presentationLink", "videoLink", TopicStatus.DONE, user);
        Topic expected = makeTopic(1, "title", "author", "description", "tags", eventDate, "new_presentationLink", "new_videoLink", TopicStatus.DONE, user);

        topicMapper.updateModel(request, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUserTopic_whenTopicStatusNotDone_tryUpdatePresentationLinkAndVideoLink(){
        UpdateDoneTopicDtoRequest request = makeUpdateDoneTopicDtoRequest("new_presentationLink", "new_videoLink");

        Topic actual = makeTopic(1, "title", "author", "description", "tags", null, null, null, TopicStatus.BEING_PREPARED, user);
        Topic expected = makeTopic(1, "title", "author", "description", "tags", null, null, null, TopicStatus.BEING_PREPARED, user);

        topicMapper.updateModel(request, actual);
        assertEquals(expected, actual);
    }
}