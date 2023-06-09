package ru.smartup.talksscanner.exception;

/**
 * Throw this exception when entity not found.
 * */
public class NotFoundEntityException extends ServiceException{
    public NotFoundEntityException(ErrorCode errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
