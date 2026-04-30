package io.mipangg.liveklasscreatorsettlementapi.domain.commissionrate.entity;

import io.mipangg.liveklasscreatorsettlementapi.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CommissionRate extends BaseEntity {

    @Column(nullable = false)
    private BigDecimal rate;

    @Column(nullable = false)
    private LocalDateTime appliedFrom;

    private LocalDateTime appliedTo;

    @Builder
    public CommissionRate(BigDecimal rate, LocalDateTime appliedFrom) {
        this.rate = rate;
        this.appliedFrom = appliedFrom;
    }

    public void expireAt(LocalDateTime appliedTo) {
        this.appliedTo = appliedTo;
    }
}
