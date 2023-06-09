package ru.smartup.talksscanner.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "idea_rate")
@SequenceGenerator(name = "seq_rate", sequenceName = "seq_idea_rate")
public class IdeaRate extends Rate {
    @ManyToOne(fetch = FetchType.LAZY)
    private Idea idea;

    public IdeaRate(long id, int rate, LocalDateTime lastUpdateDate, LocalDateTime creationDate, User user, Idea idea) {
        super(id, rate, lastUpdateDate, creationDate, user);
        this.idea = idea;
    }

    public IdeaRate() {
    }

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IdeaRate ideaRate = (IdeaRate) o;
        return Objects.equals(getId(), ideaRate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
