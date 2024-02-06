package org.hyoj.mysbbp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AesEncodeTypeEnum {

    BASE64("BASE64"),
    HEX("HEX");

    private final String value;

}
