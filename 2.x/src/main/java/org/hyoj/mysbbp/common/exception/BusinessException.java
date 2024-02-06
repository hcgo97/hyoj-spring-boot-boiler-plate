package org.hyoj.mysbbp.common.exception;

import lombok.Getter;
import org.hyoj.mysbbp.common.enums.ApiResultStatus;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private String code;
    private String message;
    private String description;
    private HttpStatus httpStatus;

    public BusinessException() {}

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ApiResultStatus apiResultStatus) {
        this.code = apiResultStatus.getCode();
        this.message = apiResultStatus.getMessage();
        this.httpStatus = apiResultStatus.getHttpStatus();
    }

    public BusinessException(ApiResultStatus apiResultStatus, String description) {
        this(apiResultStatus);
        this.description = description;
    }

}
