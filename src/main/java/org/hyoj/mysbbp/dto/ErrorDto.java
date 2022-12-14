package org.hyoj.mysbbp.dto;

import org.hyoj.mysbbp.common.enums.ApiResultStatus;

public class ErrorDto implements BaseDto {

    private final String code;
    private final String message;
    private final String description;

    public ErrorDto(ApiResultStatus apiResultStatus, String description) {
        this.code = apiResultStatus.getCode();
        this.message = apiResultStatus.getMessage();
        this.description = description;
    }

    public ErrorDto(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
