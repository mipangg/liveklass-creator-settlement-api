package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import java.math.BigDecimal;

public record CreatorSettlementSummary(
        String creatorId,
        BigDecimal pendingAmount,
        BigDecimal confirmedAmount,
        BigDecimal paidAmount
) {

}
