package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import java.math.BigDecimal;

public record SettlementAmountsDto(
        BigDecimal netSaleAmount,
        BigDecimal commission,
        BigDecimal totalSettlementAmount
) {

}
