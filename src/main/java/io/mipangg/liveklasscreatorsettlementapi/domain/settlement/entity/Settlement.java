package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.entity;

import io.mipangg.liveklasscreatorsettlementapi.domain.commissionrate.entity.CommissionRate;
import io.mipangg.liveklasscreatorsettlementapi.domain.common.BaseEntity;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.global.id.IdPrefix;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdPrefix("settlement")
@Getter
@Table(
        name = "settlement",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_settlement_creator_id_settlement_month", // 제약 조건 이름
                        columnNames = {"creator_id", "settlement_month"} // 복합 유니크 키로 묶을 컬럼들
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Creator creator;

    @Column(nullable = false)
    private LocalDate yearMonth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus status;

    @Column(nullable = false)
    private BigDecimal totalSaleAmount;

    @Column(nullable = false)
    private BigDecimal totalCancelAmount;

    @Column(nullable = false)
    private BigDecimal netSaleAmount;

    @Column(nullable = false)
    private BigDecimal commission;

    @Column(nullable = false)
    private BigDecimal totalSettlementAmount;

    @Column(nullable = false)
    private Integer saleCount;

    @Column(nullable = false)
    private Integer cancelCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_rate_id", nullable = false)
    private CommissionRate commissionRate;

    @Builder
    public Settlement(
            Creator creator,
            YearMonth yearMonth,
            BigDecimal totalSaleAmount,
            BigDecimal totalCancelAmount,
            BigDecimal netSaleAmount,
            BigDecimal commission,
            BigDecimal totalSettlementAmount,
            Integer saleCount,
            Integer cancelCount,
            CommissionRate commissionRate
    ) {
        this.creator = creator;
        this.yearMonth = yearMonth.atDay(1); // YearMonth 입력 시 LocalDate로 저장(월 1일)
        this.status = SettlementStatus.PENDING;
        this.totalSaleAmount = totalSaleAmount;
        this.totalCancelAmount = totalCancelAmount;
        this.netSaleAmount = netSaleAmount;
        this.commission = commission;
        this.totalSettlementAmount = totalSettlementAmount;
        this.saleCount = saleCount;
        this.cancelCount = cancelCount;
        this.commissionRate = commissionRate;
    }

}
