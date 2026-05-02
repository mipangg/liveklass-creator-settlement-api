package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import java.math.BigDecimal;

public record SettlementReadResponse(

        BigDecimal totalSaleAmount,
        BigDecimal totalCancelAmount,
        BigDecimal netSaleAmount,
        BigDecimal commission,
        BigDecimal totalSettlementAmount,
        Integer saleCount,
        Integer cancelCount

) {

}
