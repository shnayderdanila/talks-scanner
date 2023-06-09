package ru.smartup.talksscanner.domain;

public enum TopicStatus {
    ANNOUNCED(1),
    BEING_PREPARED(2),
    SCHEDULED(3),
    DONE(4);

    private final int order;

    TopicStatus(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
