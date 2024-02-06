package org.hyoj.mysbbp.common;

import lombok.NonNull;
import okhttp3.*;
import okio.Buffer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(value = SCOPE_PROTOTYPE)
public class EndPointHelper {

    @NonNull
    private Request originalRequest;
    private Response originalResponse;

    private String originalRequestBody;
    private String originalResponseBody;

    private OkHttpClient client = new OkHttpClient();

    public Request createRequest() {
        Request.Builder newRequestBuilder = originalRequest.newBuilder();

        if (originalRequest.body() != null) {
            newRequestBuilder = newRequestBuilder
                    .method(
                            originalRequest.method(),
                            RequestBody.create(originalRequestBody, originalRequest.body().contentType())
                    );
        }

        return newRequestBuilder.build();
    }

    public Response createResponse() {
        Response.Builder newResponseBuilder = originalResponse.newBuilder();
        if (originalResponse.body() != null) {
            newResponseBuilder = newResponseBuilder
                    .body(
                            ResponseBody.create(originalResponseBody, originalResponse.body().contentType())
                    );
        }

        return newResponseBuilder.build();
    }

    public Response execute(Request request) throws IOException {
        setRequest(request);
        originalResponse = client.newCall(createRequest()).execute();
        originalResponseBody = originalResponse.body().string();
        return createResponse();
    }

    public void setRequest(Request originalRequest) {
        this.originalRequest = originalRequest;

        final Buffer buffer = new Buffer();
        if (originalRequest.body() != null) {
            try {
                originalRequest.body().writeTo(buffer);
                originalRequestBody = buffer.readUtf8();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
