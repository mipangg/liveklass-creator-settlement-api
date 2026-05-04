package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity.Cancel;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CancelRepository extends JpaRepository<Cancel, Long> {

    @Query("select c from Cancel c "
            + "join fetch c.saleRecord sr "
            + "join fetch sr.course co "
            + "join fetch co.creator cr "
            + "where c.saleRecord.course.creator = :creator "
            + "and c.canceledAt between :startDate and :endDate")
    List<Cancel> findByCreatorAndCanceledAtBetween(
            @Param("creator") Creator creator,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );
}
