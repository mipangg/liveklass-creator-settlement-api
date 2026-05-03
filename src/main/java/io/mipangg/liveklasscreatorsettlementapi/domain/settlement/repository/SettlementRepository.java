package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryRow;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.entity.Settlement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    @Query("select s from Settlement s "
            + "where s.creator = :creator "
            + "and s.settlementMonth = :firstDayOfMonth")
    Optional<Settlement> findByCreatorAndYearMonth(
            @Param("creator") Creator creator,
            @Param("firstDayOfMonth") LocalDate firstDayOfMonth
    );

    @Query("""
            select new io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryRow(
                s.creator.id,
                s.status,
                sum(s.totalSettlementAmount)
            )
            from Settlement s
            where s.settlementMonth between :startDate and :endDate
            group by s.creator.id, s.status
    """)
    List<SettlementSummaryRow> findBySettlementMonthBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
