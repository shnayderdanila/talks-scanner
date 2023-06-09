package ru.smartup.talksscanner.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
public class Rate {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_rate"
    )
    private long id;
    @Column(nullable = false)
    private int rate;
    @Column(name = "last_update_date", nullable = false)
    private LocalDateTime lastUpdateDate;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Rate(long id, int rate, LocalDateTime lastUpdateDate, LocalDateTime creationDate, User user) {
        this.id = id;
        this.rate = rate;
        this.lastUpdateDate = lastUpdateDate;
        this.creationDate = creationDate;
        this.user = user;
    }

    public Rate() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate1 = (Rate) o;
        return getId() == rate1.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
