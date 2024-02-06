package org.hyoj.mysbbp.controller;

import org.hyoj.mysbbp.common.RequestIdGenerator;
import org.hyoj.mysbbp.dto.DataDto;
import org.hyoj.mysbbp.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class BaseController {

    @Autowired
    private RequestIdGenerator requestIdGenerator;

    @Value("${api.version}")
    private String API_VERSION;

    protected Response<DataDto> responseBuilder(Object data) {
        return Response.DataDtoContainer.builder()
                .apiVersion(this.API_VERSION)
                .requestId(requestIdGenerator.getRequestId())
                .data(data).build();
    }

}
