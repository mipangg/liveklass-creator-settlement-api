package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity;

import io.mipangg.liveklasscreatorsettlementapi.domain.common.BaseEntity;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.global.id.IdPrefix;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdPrefix("cancel")
@Getter
@Table(
        name = "cancel",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cancel_sale_record_canceled_at", // 제약 조건 이름
                        columnNames = {"sale_record_id", "canceled_at"} // 복합 유니크 키로 묶을 컬럼들
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cancel extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
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
