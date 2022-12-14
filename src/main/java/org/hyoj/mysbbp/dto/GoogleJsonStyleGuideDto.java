package org.hyoj.mysbbp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GoogleJsonStyleGuideDto<T extends BaseDto> {
    protected String apiVersion;
    protected String requestId;

    @Getter
    public static class ErrorDtoContainer<T> extends GoogleJsonStyleGuideDto<ErrorDto> {
        private final T error;

        @Builder
        public ErrorDtoContainer(String apiVersion, String requestId, T error) {
            super(apiVersion, requestId);
            this.error = error;
        }
    }

    @Getter
    public static class DataDtoContainer<T> extends GoogleJsonStyleGuideDto<DataDto> {
        private final T data;

        @Builder
        public DataDtoContainer(String apiVersion, String requestId, T data) {
            super(apiVersion, requestId);
            this.data = data;
        }
    }

}
