package org.hyoj.mysbbp.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyoj.mysbbp.common.enums.ApiResultStatus;
import org.hyoj.mysbbp.common.exception.UnauthorizedException;
import org.hyoj.mysbbp.model.Api;
import org.hyoj.mysbbp.repository.ApiRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;


@AllArgsConstructor
@Component
@Slf4j
@Service
public class ApiServiceClient {

    private final ApiRepository apiRepository;

    /**
     * get ApiKey
     *
     * @param apiKey
     * @return String apiKey
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "common-api-client", key = "'apiClient:apiKey:' + #apiKey")
    public String findByApiKey(String apiKey) {

        Api apiEntity = apiRepository.findByApiKey(apiKey).orElseThrow(() -> new EntityNotFoundException());
        return apiEntity.getApiKey();
    }

    /**
     * get ApiEntity
     *
     * @param apiKey
     * @return ApiEntity
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "common-api-client", key = "'apiClient:apiEntity:' + #apiKey")
    public Api findByApiEntity(String apiKey) {

        Api apiEntity = apiRepository.findByApiKey(apiKey).orElseThrow(() -> new UnauthorizedException(ApiResultStatus.API_KEY_IS_INVALID));
        return apiEntity;
    }

}
