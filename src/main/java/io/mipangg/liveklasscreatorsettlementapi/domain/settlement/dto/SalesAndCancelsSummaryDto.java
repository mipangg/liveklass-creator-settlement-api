package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import java.math.BigDecimal;

public record SalesAndCancelsSummaryDto(
        BigDecimal totalSaleAmount,
        BigDecimal totalCancelAmount,
        int saleCount,
        int cancelCount
) {

}
