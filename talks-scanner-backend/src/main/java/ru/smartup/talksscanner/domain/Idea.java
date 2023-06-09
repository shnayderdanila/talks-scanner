package ru.smartup.talksscanner.domain;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Idea {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_idea"
    )
    private Long id;
    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name="creation_date")
    private LocalDateTime creationDate;
    @Column(name="last_update_date")
    private LocalDateTime lastUpdateDate;
    @Enumerated(EnumType.STRING)
    private IdeaStatus status;

    @Formula(value = "(select avg(ir.rate) from idea i join idea_rate ir on i.id = ir.idea_id where i.id = id)")
    private Double rate;

    public Idea(){}

    public Idea(Long id, String title, String description, User user, LocalDateTime creationDate, LocalDateTime lastUpdateDate, IdeaStatus status, Double rate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user = user;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.status = status;
        this.rate = rate;
    }

    public Idea(String title, String description, User user, LocalDateTime creationDate, LocalDateTime lastUpdateDate, IdeaStatus status) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.status = status;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public IdeaStatus getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setStatus(IdeaStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Idea idea = (Idea) o;
        return Objects.equals(getId(), idea.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
