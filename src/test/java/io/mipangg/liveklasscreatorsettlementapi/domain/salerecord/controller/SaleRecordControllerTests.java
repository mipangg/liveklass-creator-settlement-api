package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordListReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.service.SaleRecordService;
import io.mipangg.liveklasscreatorsettlementapi.global.TestUtil;
import io.mipangg.liveklasscreatorsettlementapi.global.config.JacksonConfig;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SaleRecordController.class)
@Import(JacksonConfig.class)
class SaleRecordControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SaleRecordService saleRecordService;

    @Test
    @DisplayName("판매 내역 등록 api를 성공적으로 호출할 수 있다")
    void createSaleRecordSuccessTest() throws Exception {

        SaleRecordCreateRequest req = TestUtil.genSaleRecordCreateRequest();

        mockMvc.perform(post("/sale-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isCreated());

        verify(saleRecordService).saveSaleRecord(req);

    }

    @Test
    @DisplayName("SaleRecordCreateRequest 유효성 검증 실패로 판매기록 생성에 실패할 수 있다")
    void createSaleRecordFailTest() throws Exception {

        SaleRecordCreateRequest req = new SaleRecordCreateRequest(
                "",
                "student-1",
                BigDecimal.valueOf(50000),
                OffsetDateTime.parse("2025-03-05T10:00:00+09:00")
        );

        mockMvc.perform(post("/sale-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isBadRequest());

        verify(saleRecordService, never()).saveSaleRecord(req);

    }

    @Test
    @DisplayName("판매 내역 목록 조회 api를 성공적으로 호출할 수 있다")
    void readSaleRecordsSuccessTest() throws Exception {

        List<SaleRecordListReadResponse> resp = TestUtil.genSaleRecordListReadResponses();

        when(saleRecordService.findSaleRecords(any())).thenReturn(resp);

        mockMvc.perform(get("/sale-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(7))
                .andExpect(jsonPath("$[0].id").value("sale-1"))
                .andExpect(jsonPath("$[0].courseId").value("course-1"))
                .andExpect(jsonPath("$[0].studentId").value("student-1"))
                .andExpect(jsonPath("$[0].amount").value(50000))
                .andExpect(jsonPath("$[0].paidAt").value("2025-03-05T10:00:00+09:00"));

    }

    @Test
    @DisplayName("SaleRecordListReadRequest 유효성 검증 실패로 판매 내역 목록 조회에 실패할 수 있다")
    void readSaleRecordsFailTest() throws Exception {

        mockMvc.perform(get("/sale-records")
                        .param("creatorId", "creator-1")
                        .param("startDate", "2027-03-01")
                        .param("endDate", "2027-03-10")
                )
                .andExpect(status().isBadRequest());

    }


}