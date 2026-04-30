package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

}
