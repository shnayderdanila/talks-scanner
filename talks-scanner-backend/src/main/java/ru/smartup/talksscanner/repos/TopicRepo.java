package ru.smartup.talksscanner.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.smartup.talksscanner.domain.Topic;

import java.util.Optional;

@Repository
public interface TopicRepo extends CrudRepository<Topic, Long> {
    Optional<Topic> findByIdAndUserId(long topicId, long userId);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "user")
    Page<Topic> findAll(Pageable p);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "user")
    Page<Topic> findAll(Specification<?> specification, Pageable p);

}
