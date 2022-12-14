package org.hyoj.mysbbp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {

    ADMIN_USER("admin"),
    GENERAL_USER("general");

    private final String value;

}
