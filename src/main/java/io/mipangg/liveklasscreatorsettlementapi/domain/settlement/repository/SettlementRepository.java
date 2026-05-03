package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.entity.Settlement;
import java.time.LocalDate;
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
}
