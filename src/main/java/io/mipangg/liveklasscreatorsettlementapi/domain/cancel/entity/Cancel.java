package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity;

import io.mipangg.liveklasscreatorsettlementapi.domain.common.BaseEntity;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.global.id.IdPrefix;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdPrefix("cancel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cancel extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_record_id", nullable = false)
    private SaleRecord saleRecord;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private OffsetDateTime canceledAt;

    @Builder
    public Cancel(SaleRecord saleRecord, BigDecimal amount, OffsetDateTime canceledAt) {
        this.saleRecord = saleRecord;
        this.amount = amount;
        this.canceledAt = canceledAt;
    }

}
