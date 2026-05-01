package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto;

import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record SaleRecordListReadRequest(

        String creatorId,

        @PastOrPresent
        LocalDate startDate,

        @PastOrPresent
        LocalDate endDate

) {

}
