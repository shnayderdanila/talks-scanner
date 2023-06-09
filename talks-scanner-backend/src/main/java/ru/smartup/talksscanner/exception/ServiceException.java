package ru.smartup.talksscanner.exception;

/**
 * Throw this exception on a business layer error in general.
 * */
public class ServiceException extends Exception {
    private final ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
