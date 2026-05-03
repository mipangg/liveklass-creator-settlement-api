package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.entity.SettlementStatus;
import java.math.BigDecimal;

public record SettlementSummaryRow(
        String creatorId,
        SettlementStatus status,
        BigDecimal amount
) {

}
