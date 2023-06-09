package ru.smartup.talksscanner.repos;

import org.springframework.data.repository.CrudRepository;
import ru.smartup.talksscanner.domain.TopicRate;
import ru.smartup.talksscanner.domain.User;

import java.util.Optional;

public interface TopicRateRepo extends CrudRepository<TopicRate, Long> {
    Optional<TopicRate> findByIdAndUserAndTopicId(long rateId, User user, long topicId);
    Optional<TopicRate> findByUserAndTopicId(User user, long topicId);

}
