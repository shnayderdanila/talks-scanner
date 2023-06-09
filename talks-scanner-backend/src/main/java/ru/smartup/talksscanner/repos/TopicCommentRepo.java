package ru.smartup.talksscanner.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.smartup.talksscanner.domain.TopicComment;
import ru.smartup.talksscanner.domain.User;

import java.util.Optional;

public interface TopicCommentRepo extends CrudRepository<TopicComment, Long> {
    Optional<TopicComment> findByIdAndUserAndTopicId(long id, User user, long topicId);

    Page<TopicComment> findAllByTopicId(long topicId, Pageable pageable);
}
