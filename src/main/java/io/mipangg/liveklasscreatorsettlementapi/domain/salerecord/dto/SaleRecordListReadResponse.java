package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SaleRecordListReadResponse(

        String id,
        String courseId,
        String studentId,
        BigDecimal amount,
        OffsetDateTime paidAt

) {

}
