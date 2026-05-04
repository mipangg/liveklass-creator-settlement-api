package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import java.util.List;

public record SettlementSummaryReadResponse(
        List<CreatorSettlementSummary> creators,
        SettlementTotalSummary total
) {

}
