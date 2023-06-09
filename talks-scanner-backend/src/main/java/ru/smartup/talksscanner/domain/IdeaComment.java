package ru.smartup.talksscanner.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "idea_comment")
@SequenceGenerator(name = "seq_comment", sequenceName = "seq_idea_comment")
public class IdeaComment extends Comment{

    public IdeaComment(long id, String text, LocalDateTime lastUpdateDate, LocalDateTime creationDate, User user, Idea idea) {
        super(id, text, lastUpdateDate, creationDate, user);
        this.idea = idea;
    }

    public IdeaComment() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Idea idea;

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
        IdeaComment that = (IdeaComment) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
