package ru.smartup.talksscanner.exception;

/**
 * Types of application error.
 * Contains of string shortcut code and template for error message.
 * */
public enum ErrorCode {

    USER_NOT_FOUND("User %s not found."),
    IDEA_NOT_FOUND("Idea %d not found."),
    DATABASE_DATA_CONFLICT("Server can not save your data. %s"),
    TOPIC_NOT_FOUND("Topic %d not found."),
    EMPTY_FILTER("Filters can not be empty."),
    COMMENT_NOT_FOUND("Comment %d not found."),
    RATE_NOT_FOUND("Rate %d not found."),
    TOPIC_CANT_BE_RATED("Topic %d can't be rated."),
    ENTITY_TYPE_NOT_HANDLE("Entity type %s can't be handled"),
    DATABASE_FAILURE("Error in Database layer. Debug message:%s");
    private final String template;

    ErrorCode(String message) {
        this.template = message;
    }

    public String getTemplate() {
        return template;
    }
}
