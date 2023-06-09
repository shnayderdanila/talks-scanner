package ru.smartup.talksscanner.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.exception.ServiceException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorHandler.class);

    /**
     * Handle not valid in client request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponse handleValidation(MethodArgumentNotValidException exc) {
        LOGGER.warn("Handle not valid arguments exception with message : {}", exc.getMessage());
        final ErrorsResponse errResponse = new ErrorsResponse();
        exc.getBindingResult().getFieldErrors()
                .forEach(
                        fieldError -> errResponse.getErrorResponses().add(
                                new ErrorResponse((
                                    String.format("WRONG_%s", fieldError.getField().toUpperCase())),
                                    fieldError.getDefaultMessage()))
                );

        return errResponse;
    }


    /**
     * Handle not valid URL query param
     * */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponse handlePathException(BindException exception) {
        final ErrorsResponse errResponse = new ErrorsResponse();
        LOGGER.info("Handle exception in query param with message : {}", exception.getMessage());

        exception.getBindingResult().getFieldErrors()
                .forEach(
                        fieldError -> errResponse.getErrorResponses().add(
                                new ErrorResponse((
                                        String.format("WRONG_%s", fieldError.getField().toUpperCase())),
                                        fieldError.getDefaultMessage()))
                );

        return errResponse;
    }


    /**
     * Handle exception {@link  NotFoundEntityException}
     */
    @ExceptionHandler(NotFoundEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundEntityException ex) {
        LOGGER.warn("Handle NotFoundEntity exception with error code: {}, message: {}", ex.getErrorCode(), ex.getMessage());
        return new ErrorResponse(ex.getErrorCode().toString(), ex.getMessage());
    }

    /**
     * Handle exception {@link  ServiceException}
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServiceException(ServiceException ex) {
        LOGGER.warn("Handle Service exception with error code: {}, message: {}", ex.getErrorCode(), ex.getMessage());
        return new ErrorResponse(ex.getErrorCode().toString(), ex.getMessage());
    }

    /**
     * Handle a database error leading to conflict of existing application data.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrity(DataIntegrityViolationException ex) {
        LOGGER.warn("Handle Database exception with error code:{}, message:{}",ErrorCode.DATABASE_DATA_CONFLICT, ex.getCause().getCause().getMessage());

        return new ErrorResponse(ErrorCode.DATABASE_DATA_CONFLICT.toString(),
                                 String.format(ErrorCode.DATABASE_DATA_CONFLICT.getTemplate(), ex.getCause().getCause().getMessage())
        );
    }

    /**
     * Handle exception working with database
     * */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDataAccessException(DataAccessException ex) {
        LOGGER.warn("Handle exception from database with code:{}, message:{}",ErrorCode.DATABASE_FAILURE, ex.getMessage());
        return new ErrorResponse(ErrorCode.DATABASE_FAILURE.toString(),
                                 String.format(ErrorCode.DATABASE_FAILURE.getTemplate(), ex.getMessage()));
    }

    public static class ErrorsResponse implements Serializable {
        private final List<ErrorResponse> errorResponses = new ArrayList<>();

        public List<ErrorResponse> getErrorResponses() {
            return errorResponses;
        }
    }

    public static class ErrorResponse implements Serializable {

        private String errorCode;
        private String message;

        public ErrorResponse(String errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}


