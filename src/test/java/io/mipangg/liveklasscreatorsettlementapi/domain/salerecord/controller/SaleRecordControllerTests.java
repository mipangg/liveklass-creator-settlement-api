package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.service.SaleRecordService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SaleRecordController.class)
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

        SaleRecordCreateRequest req = new SaleRecordCreateRequest(
                "course-1",
                "student-1",
                BigDecimal.valueOf(50000),
                LocalDateTime.of(2025, 3, 5, 10, 0)
        );

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
                LocalDateTime.of(2025, 3, 5, 10, 0)
        );

        mockMvc.perform(post("/sale-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isBadRequest());

        verify(saleRecordService, never()).saveSaleRecord(req);
    
    }

}