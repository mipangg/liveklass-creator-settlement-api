package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SaleRecordRepository extends JpaRepository<SaleRecord, Long> {

    Optional<SaleRecord> findById(String id);

    @Query("select s from SaleRecord s "
            + "join fetch s.course "
            + "join fetch s.student "
            + "where (:creatorId is null or s.course.creator.id = :creatorId) "
            + "and (:startDate is null or s.paidAt between :startDate and :endDate)")
    List<SaleRecord> findSaleRecords(
            @Param("creatorId") String creatorId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );
}
