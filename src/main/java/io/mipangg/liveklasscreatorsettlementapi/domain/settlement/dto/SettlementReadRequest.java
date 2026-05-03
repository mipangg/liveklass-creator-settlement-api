package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.YearMonth;

public record SettlementReadRequest(

        @NotBlank(message = "creatorId는 필수입니다.")
        String creatorId,

        @JsonFormat(pattern = "yyyy-MM")
        @NotNull(message = "yearMonth는 필수입니다.")
        @PastOrPresent(message = "yearMonth는 과거 혹은 현재여야 합니다.")
        YearMonth yearMonth

) {

}
