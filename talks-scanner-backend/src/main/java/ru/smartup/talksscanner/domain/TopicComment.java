package ru.smartup.talksscanner.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "topic_comment")
@SequenceGenerator(name = "seq_comment", sequenceName = "seq_topic_comment")
public class TopicComment extends Comment{
    @ManyToOne(fetch = FetchType.LAZY)
    private Topic topic;

    public TopicComment(long id, String text, LocalDateTime lastUpdateDate, LocalDateTime creationDate, User user, Topic topic) {
        super(id, text, lastUpdateDate, creationDate, user);
        this.topic = topic;
    }

    public TopicComment() {
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TopicComment that = (TopicComment) o;
        return Objects.equals(super.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
