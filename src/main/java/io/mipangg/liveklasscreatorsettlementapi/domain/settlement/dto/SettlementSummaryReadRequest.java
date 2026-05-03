package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.YearMonth;

public record SettlementSummaryReadRequest(

        @JsonFormat(pattern = "yyyy-MM")
        @NotNull(message = "startMonth는 필수입니다.")
        @Past(message = "startMonth는 과거여야 합니다.")
        YearMonth startMonth,

        @JsonFormat(pattern = "yyyy-MM")
        @NotNull(message = "endMonth는 필수입니다.")
        @Past(message = "endMonth는 과거여야 합니다.")
        YearMonth endMonth
) {

    public SettlementSummaryReadRequest {
        if (startMonth.isAfter(endMonth)) {
            throw new CustomLogicException(ErrorCode.INVALID_DATE_RANGE);
        }
    }

}
