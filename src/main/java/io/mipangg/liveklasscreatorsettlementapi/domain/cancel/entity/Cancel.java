package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity;

import io.mipangg.liveklasscreatorsettlementapi.domain.common.BaseEntity;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cancel extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String publicId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_record_id", nullable = false)
    private SaleRecord saleRecord;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime canceledAt;

    @Builder
    public Cancel(
            String publicId, SaleRecord saleRecord, BigDecimal amount, LocalDateTime canceledAt
    ) {
        this.publicId = publicId;
        this.saleRecord = saleRecord;
        this.amount = amount;
        this.canceledAt = canceledAt;
    }

}
