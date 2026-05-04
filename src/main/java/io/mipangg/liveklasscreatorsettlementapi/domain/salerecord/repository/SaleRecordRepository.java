package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SaleRecordRepository extends JpaRepository<SaleRecord, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SaleRecord s where s.id = :id")
    Optional<SaleRecord> findByIdForUpdate(@Param("id") String id);

    @Query("select s from SaleRecord s "
            + "join fetch s.course "
            + "join fetch s.student "
            + "where (:creator is null or s.course.creator = :creator) "
            + "and (:startDate is null or s.paidAt between :startDate and :endDate)")
    List<SaleRecord> findSaleRecordsWithFilters(
            @Param("creator") Creator creator,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );

    @Query("select s from SaleRecord s "
            + "join fetch s.course "
            + "join fetch s.student "
            + "where s.course.creator = :creator "
            + "and s.paidAt between :startDate and :endDate")
    List<SaleRecord> findByCreatorAndPaidAtBetween(
            @Param("creator") Creator creator,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );
}
