package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity.Cancel;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelRepository extends JpaRepository<Cancel, Long> {

    List<Cancel> findBySaleRecordInAndCanceledAtBetween(
            List<SaleRecord> saleRecords,
            OffsetDateTime startDate,
            OffsetDateTime endDate
    );
}
