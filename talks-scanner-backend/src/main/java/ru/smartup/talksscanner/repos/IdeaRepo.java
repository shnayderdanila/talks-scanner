package ru.smartup.talksscanner.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.smartup.talksscanner.domain.Idea;
import ru.smartup.talksscanner.domain.IdeaStatus;

import java.util.Optional;

@Repository
public interface IdeaRepo extends CrudRepository<Idea, Long> {
    Optional<Idea> findByIdAndUserIdAndStatusNot(Long id, Long userId, IdeaStatus status);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "user")
    Page<Idea> findAll(Specification<?> specification, Pageable p);

}
