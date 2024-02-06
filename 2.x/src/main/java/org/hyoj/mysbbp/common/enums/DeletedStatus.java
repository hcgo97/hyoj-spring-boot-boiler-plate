package org.hyoj.mysbbp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeletedStatus {

    DELETED("Y"),
    NOT_DELETED("N");

    private String code;
}
