package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import java.math.BigDecimal;

public record SettlementTotalSummary(
        BigDecimal pendingAmount,
        BigDecimal confirmedAmount,
        BigDecimal paidAmount
) {

}
