package org.hyoj.mysbbp.controller;

import org.hyoj.mysbbp.common.RequestIdGenerator;
import org.hyoj.mysbbp.dto.GoogleJsonStyleGuideDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class BaseController {

    @Autowired
    private RequestIdGenerator requestIdGenerator;

    @Value("${api.version}")
    private String apiVersion;

    protected GoogleJsonStyleGuideDto.DataDtoContainer.DataDtoContainerBuilder<Object> dataContainerBuilder;
    protected GoogleJsonStyleGuideDto.ErrorDtoContainer.ErrorDtoContainerBuilder<Object> errorContainerBuilder;

    public BaseController() {

        dataContainerBuilder = GoogleJsonStyleGuideDto.DataDtoContainer.builder().apiVersion(apiVersion).requestId(requestIdGenerator.getRequestId());
        errorContainerBuilder = GoogleJsonStyleGuideDto.ErrorDtoContainer.builder().apiVersion(apiVersion).requestId(requestIdGenerator.getRequestId());
    }

}
