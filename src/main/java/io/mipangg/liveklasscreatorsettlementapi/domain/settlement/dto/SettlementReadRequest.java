package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SettlementReadRequest(

        @NotBlank(message = "creatorId는 필수입니다.")
        String creatorId,

        @NotBlank(message = "settlementMonth는 필수입니다.")
        @Pattern(
                regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])$",
                message = "연월 형식이 올바르지 않습니다.(yyyy-MM)"
        )
        String settlementMonth

) {

}
