package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.CreatorSettlementSummary;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementMonthlyReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementMonthlyReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementTotalSummary;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.service.SettlementService;
import io.mipangg.liveklasscreatorsettlementapi.global.config.JacksonConfig;
import java.math.BigDecimal;
import java.util.List;
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
    void readMonthlySettlementSuccessTest() throws Exception {

        SettlementMonthlyReadResponse expected =
                new SettlementMonthlyReadResponse(
                        BigDecimal.valueOf(260000),
                        BigDecimal.valueOf(110000),
                        BigDecimal.valueOf(150000),
                        BigDecimal.valueOf(30000),
                        BigDecimal.valueOf(120000),
                        4,
                        2
                );

        when(settlementService.findSettlement(any(SettlementMonthlyReadRequest.class)))
                .thenReturn(expected);

        mockMvc.perform(get("/settlements/monthly")
                        .param("creatorId", "creator-1")
                        .param("settlementMonth", "2025-03")
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
    @DisplayName("SettlementMonthlyReadRequest 유효성 검증에 실패하면 크리에비터별 월별 정산 조회 api 호출에 실패한다")
    void readSettlementFailTest() throws Exception {

        mockMvc.perform(get("/settlements/monthly")
                .param("creatorId", "creator-1")
                .param("settlementMonth", "2027-03")
        ).andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("기간 내 전체 크리에이터 정산 현황 목록 조회 api를 호출할 수 있다")
    void readSettlementSummarySuccessTest() throws Exception {

        SettlementSummaryReadResponse expected =
                new SettlementSummaryReadResponse(
                        List.of(
                                new CreatorSettlementSummary("creator-1"),
                                new CreatorSettlementSummary("creator-2")
                        ),
                        new SettlementTotalSummary(
                                BigDecimal.valueOf(15000),
                                BigDecimal.valueOf(20000),
                                BigDecimal.valueOf(10000)
                        )
                );

        when(settlementService.getSettlementSummary(any(SettlementSummaryReadRequest.class)))
                .thenReturn(expected);

        mockMvc.perform(get("/settlements")
                .param("startMonth", "2025-03")
                .param("endMonth", "2025-04"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creators.size()").value(2))
                .andExpect(jsonPath("$.creators[0].creatorId").value("creator-1"))
                .andExpect(jsonPath("$.total.pendingAmount").value(15000))
                .andExpect(jsonPath("$.total.confirmedAmount").value(20000))
                .andExpect(jsonPath("$.total.paidAmount").value(10000));

    }

    @Test
    @DisplayName("SettlementSummaryReadRequest 유효성 검증에 실패하면 전체 크리에이터 정산 목록 조회 api 호출에 실패한다")
    void readSettlementSummaryFailTest() throws Exception {

        mockMvc.perform(get("/settlements")
                        .param("startMonth", "2026-01")
                        .param("endMonth", "2026-05")) // 현재 월은 조회 불가
                .andExpect(status().isBadRequest());

    }
}