package org.hyoj.mysbbp.repository;

import org.hyoj.mysbbp.model.VendorRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRequestHistoryRepository extends JpaRepository<VendorRequestHistory, Long> {

    List<VendorRequestHistory> findByRequestId(String requestId);

}
