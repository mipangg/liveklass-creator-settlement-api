package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class CreatorSettlementSummary {

    private final String creatorId;
    private BigDecimal pendingAmount = BigDecimal.ZERO;
    private BigDecimal confirmedAmount = BigDecimal.ZERO;
    private BigDecimal paidAmount = BigDecimal.ZERO;

    public CreatorSettlementSummary(String creatorId) {
        this.creatorId = creatorId;
    }

    public void addPendingAmount(BigDecimal amount) {
        this.pendingAmount = this.pendingAmount.add(amount);
    }

    public void addConfirmedAmount(BigDecimal amount) {
        this.confirmedAmount = this.confirmedAmount.add(amount);
    }

    public void addPaidAmount(BigDecimal amount) {
        this.paidAmount = this.paidAmount.add(amount);
    }

}
