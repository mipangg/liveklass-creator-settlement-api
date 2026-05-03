package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto;

import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record SaleRecordListReadRequest(

        String creatorId,

        @PastOrPresent(message = "startDate는 과거 또는 현재여야 합니다.")
        LocalDate startDate,

        @PastOrPresent(message = "endDate는 과거 또는 현재여야 합니다.")
        LocalDate endDate

) {

    public SaleRecordListReadRequest {
        if ((startDate == null && endDate != null) || (startDate != null && endDate == null)) {
            throw new CustomLogicException(ErrorCode.DATE_RANGE_REQUIRED);
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new CustomLogicException(ErrorCode.INVALID_DATE_RANGE);
        }
    }
}
