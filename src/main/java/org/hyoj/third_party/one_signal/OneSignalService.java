package org.hyoj.third_party.one_signal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import org.hyoj.mysbbp.common.EndPointHelper;
import org.hyoj.mysbbp.common.EndPointHelperFactory;
import org.hyoj.mysbbp.common.enums.ApiResultStatus;
import org.hyoj.mysbbp.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class OneSignalService {

    @Value("${one_signal.notification-url}")
    private String ONE_SIGNAL_NOTIFICATION_URL;

    @Value("${one_signal.app-key}")
    private String ONE_SIGNAL_APP_KEY;

    @Value("${one_signal.api-key}")
    private String ONE_SIGNAL_API_KEY;

    private final EndPointHelperFactory endPointHelperFactory;


    /**
     * PUSH 메세지 보내는 서비스
     */
    @Async
    public PushDto.Response createNotification(PushDto.NotificationBody notificationBody) {
        if (notificationBody.getIncludedSegments() != null && notificationBody.getIncludedSegments().size() <= 0) {
            return null;
        }

        try {
            EndPointHelper endPointHelper = endPointHelperFactory.factory();
            ObjectMapper objectMapper = new ObjectMapper();

            HttpUrl httpUrl = HttpUrl.parse(ONE_SIGNAL_NOTIFICATION_URL)
                    .newBuilder()
                    .build();

            notificationBody.setAppId(ONE_SIGNAL_APP_KEY);

            String requestBodyContent = objectMapper.writeValueAsString(notificationBody);

            log.debug("[notification url] {}", ONE_SIGNAL_NOTIFICATION_URL);
            log.debug("[notification Body] {}", requestBodyContent.toString());

            okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                    .url(httpUrl)
                    .addHeader("Authorization", String.format("Basic %s", ONE_SIGNAL_API_KEY))
                    .post(RequestBody.create(requestBodyContent, okhttp3.MediaType.parse("application/json")))
                    .build();

            okhttp3.Response httpResponse = endPointHelper.execute(httpRequest);

            PushDto.Response response = objectMapper.readValue(httpResponse.body().string(), new TypeReference<>() {
            });
            if (response.getErrors() != null) {
                log.error("OneSignal Warning: {}", response.getErrors().toString());
            }
            return response;

        } catch (IOException e) {
            throw new BusinessException(ApiResultStatus.SERVICE_UNAVAILABLE);
        }
    }

}
