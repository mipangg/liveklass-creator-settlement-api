package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CancelCreateRequest(

        @NotBlank
        String saleId,

        @Min(0)
        @NotNull
        BigDecimal amount,

        @NotNull
        OffsetDateTime canceledAt
) {

}
