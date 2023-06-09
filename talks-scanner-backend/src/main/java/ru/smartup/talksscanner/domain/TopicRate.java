package ru.smartup.talksscanner.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "topic_rate")
@SequenceGenerator(name = "seq_rate", sequenceName = "seq_topic_rate")
public class TopicRate extends Rate {
    @ManyToOne(fetch = FetchType.LAZY)
    private Topic topic;

    public TopicRate(long id, int rate, LocalDateTime lastUpdateDate, LocalDateTime creationDate, User user, Topic topic) {
        super(id, rate, lastUpdateDate, creationDate, user);
        this.topic = topic;
    }

    public TopicRate() {
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
        TopicRate topicRate = (TopicRate) o;
        return Objects.equals(getId(), topicRate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
