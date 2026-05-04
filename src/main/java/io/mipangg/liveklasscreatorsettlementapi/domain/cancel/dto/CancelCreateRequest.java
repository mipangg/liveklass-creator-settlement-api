package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CancelCreateRequest(

        @NotBlank(message = "saleId는 필수입니다.")
        String saleId,

        @NotNull(message = "amount는 필수입니다.")
        @DecimalMin(value = "0.00", message = "amount는 최소 0.00 이상이어야 합니다.")
        BigDecimal amount,

        @NotNull(message = "canceledAt은 필수입니다.")
        @PastOrPresent(message = "canceledAt은 과거 또는 현재여야 합니다.")
        OffsetDateTime canceledAt
) {

}
