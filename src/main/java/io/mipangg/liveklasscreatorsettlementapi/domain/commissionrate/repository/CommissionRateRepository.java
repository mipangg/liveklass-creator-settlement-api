package io.mipangg.liveklasscreatorsettlementapi.domain.commissionrate.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.commissionrate.entity.CommissionRate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommissionRateRepository extends JpaRepository<CommissionRate, Long> {

    @Query("select cr from CommissionRate cr where cr.appliedTo IS NULL")
    Optional<CommissionRate> getCurrentCommissionRate();

}
