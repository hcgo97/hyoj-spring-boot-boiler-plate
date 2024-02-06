package org.hyoj.mysbbp.repository;

import org.hyoj.mysbbp.model.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiRepository extends JpaRepository<Api, Long> {
    Optional<Api> findByApiKey(String apiKey);
}
