package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.YearMonth;

public record SettlementSummaryReadRequest(

        @JsonFormat(pattern = "yyyy-MM")
        @NotNull(message = "startMonth는 필수입니다.")
        @PastOrPresent(message = "startMonth는 과거 혹은 현재여야 합니다.")
        YearMonth startMonth,

        @JsonFormat(pattern = "yyyy-MM")
        @NotNull(message = "endMonth는 필수입니다.")
        @PastOrPresent(message = "endMonth는 과거 혹은 현재여야 합니다.")
        YearMonth endMonth
) {

    public SettlementSummaryReadRequest {
        if (startMonth.isAfter(endMonth)) {
            throw new CustomLogicException(ErrorCode.INVALID_DATE_RANGE);
        }
    }

}
