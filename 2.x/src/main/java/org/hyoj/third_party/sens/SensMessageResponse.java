package org.hyoj.third_party.sens;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SensMessageResponse {

    private String statusCode;
    private String statusName;
    private String requestId;
    private String requestTime;

}
