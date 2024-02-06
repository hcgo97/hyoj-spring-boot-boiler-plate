package org.hyoj.mysbbp.common.exception;

import org.hyoj.mysbbp.common.enums.ApiResultStatus;

public class TokenException extends UnauthorizedException {

    public TokenException(String message) {
        super(message);
    }

    public TokenException(ApiResultStatus apiResultStatus) {
        super(apiResultStatus);
    }

    public TokenException(ApiResultStatus apiResultStatus, String description) {
        super(apiResultStatus, description);
    }

}
