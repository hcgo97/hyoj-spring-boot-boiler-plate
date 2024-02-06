package org.hyoj.mysbbp.repository;

import org.hyoj.mysbbp.model.VendorRequestHistories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRequestHistoryRepository extends JpaRepository<VendorRequestHistories, Long> {

    List<VendorRequestHistories> findByRequestId(String requestId);

}
