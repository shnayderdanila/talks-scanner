package ru.smartup.talksscanner.repos;

import org.springframework.data.repository.CrudRepository;
import ru.smartup.talksscanner.domain.IdeaRate;
import ru.smartup.talksscanner.domain.User;

import java.util.Optional;

public interface IdeaRateRepo extends CrudRepository<IdeaRate, Long> {
    Optional<IdeaRate> findByIdAndUserAndIdeaId(long rateId, User user, long ideaId);
    Optional<IdeaRate> findByUserAndIdeaId(User user, long ideaId);
}
