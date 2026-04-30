package io.mipangg.liveklasscreatorsettlementapi.domain.commissionrate.entity;

import io.mipangg.liveklasscreatorsettlementapi.domain.common.BaseEntity;
import io.mipangg.liveklasscreatorsettlementapi.global.id.IdPrefix;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdPrefix("commission")
@Getter
@NoArgsConstructor
public class CommissionRate extends BaseEntity {

    @Column(nullable = false)
    private BigDecimal rate;

    @Column(nullable = false)
    private OffsetDateTime appliedFrom;

    private OffsetDateTime appliedTo;

    @Builder
    public CommissionRate(BigDecimal rate, OffsetDateTime appliedFrom) {
        this.rate = rate;
        this.appliedFrom = appliedFrom;
    }

    public void expireAt(OffsetDateTime appliedTo) {
        this.appliedTo = appliedTo;
    }
}
