package org.hyoj.third_party.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class KakaoGeolocationService {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class KakaoAddressRequest {
        private String query;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class KakaoAddressResponse {
        private List<Documents> documents;
        private Meta meta;

        // 언더바(_) 뒤에 숫자가 붙는 케이스는 CamelCase 로 변환되지 않아서 JsonProperty 지정함
        @Getter
        @Setter
        @ToString
        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        public static class Documents {
            private Address address;
            private String addressName;
            private String addressType;
            private RoadAddress roadAddress;
            private String x;
            private String y;

            @Getter
            @Setter
            @ToString
            @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
            public static class Address {
                private String addressName;

                @JsonProperty("b_code")
                private String bCode;

                @JsonProperty("h_code")
                private String hCode;

                private String mainAddressNo;
                private String mountainYn;

                @JsonProperty("region_1depth_name")
                private String region1DepthName;
                @JsonProperty("region_2depth_name")
                private String region2DepthName;
                @JsonProperty("region_3depth_h_name")
                private String region3DepthHName;
                @JsonProperty("region_3depth_name")
                private String region3DepthName;

                private String subAddressNo;
                private String x;
                private String y;
            }

            @Getter
            @Setter
            @ToString
            @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
            public static class RoadAddress {
                private String addressName;
                private String buildingName;
                private String mainBuildingNo;

                @JsonProperty("region_1depth_name")
                private String region1DepthName;
                @JsonProperty("region_2depth_name")
                private String region2DepthName;
                @JsonProperty("region_3depth_name")
                private String region3DepthName;

                private String roadName;
                private String subBuildingNo;
                private String undergroundYn;
                private String x;
                private String y;
                private String zoneNo;
            }
        }

        @Getter
        @Setter
        @ToString
        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        public static class Meta {
            private Boolean isEnd;
            private Integer pageableCount;
            private Integer totalCount;
        }

    }

    @Value("${kakao.api-key}")
    private String KAKAO_API_KEY;
    @Value("${kakao.endpoint}")
    private String KAKAO_ENDPOINT;

    private final OkHttpClient client = new OkHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable(value = "common-location", key = "'location:kakaoAddress:' + #request.query")
    public KakaoAddressResponse execute(KakaoAddressRequest request) throws IOException {

        String baseUrl = KAKAO_ENDPOINT;

        HttpUrl httpUrl = HttpUrl.parse(baseUrl)
                .newBuilder()
                .addQueryParameter("query", request.getQuery())
                .build();

        log.debug(httpUrl.toString());

        okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                .addHeader("Authorization", KAKAO_API_KEY)
                .url(httpUrl)
                .build();

        okhttp3.Response response = client.newCall(httpRequest).execute();

        KakaoAddressResponse responseBody = new KakaoAddressResponse();

        try {
            if (response.isSuccessful() == true) {
                responseBody = objectMapper.readValue(response.body().string(), new TypeReference<>() {
                });
                return responseBody;

            } else {
                throw new KakaoException();
            }

        } catch (IOException e) {
            throw new IOException();
        }

    }

}
