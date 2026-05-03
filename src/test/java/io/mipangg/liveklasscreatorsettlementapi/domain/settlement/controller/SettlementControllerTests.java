package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.service.SettlementService;
import io.mipangg.liveklasscreatorsettlementapi.global.config.JacksonConfig;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SettlementController.class)
@Import(JacksonConfig.class)
class SettlementControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SettlementService settlementService;
    
    @Test
    @DisplayName("크리에이터별 월별 정산 조회 api를 호출할 수 있다")
    void readSettlementSuccessTest() throws Exception {

        SettlementReadResponse expected = new SettlementReadResponse(
                BigDecimal.valueOf(260000),
                BigDecimal.valueOf(110000),
                BigDecimal.valueOf(150000),
                BigDecimal.valueOf(30000),
                BigDecimal.valueOf(120000),
                4,
                2
        );

        when(settlementService.findSettlement(any(SettlementReadRequest.class)))
                .thenReturn(expected);

        mockMvc.perform(get("/settlements/monthly")
                        .param("creatorId", "creator-1")
                        .param("yearMonth", "2025-03")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSaleAmount").value(260000))
                .andExpect(jsonPath("$.totalCancelAmount").value(110000))
                .andExpect(jsonPath("$.netSaleAmount").value(150000))
                .andExpect(jsonPath("$.commission").value(30000))
                .andExpect(jsonPath("$.totalSettlementAmount").value(120000))
                .andExpect(jsonPath("$.saleCount").value(4))
                .andExpect(jsonPath("$.cancelCount").value(2));

    }

    @Test
    @DisplayName("SettlementReadRequest 유효성 검증에 실패하면 크리에비터별 월별 정산 조회 api 호출에 실패한다")
    void readSettlementFailTest() throws Exception {

        mockMvc.perform(get("/settlements/monthly")
                .param("creatorId", "creator-1")
                .param("yearMonth", "2027-03")
        ).andExpect(status().isBadRequest());

    }
}