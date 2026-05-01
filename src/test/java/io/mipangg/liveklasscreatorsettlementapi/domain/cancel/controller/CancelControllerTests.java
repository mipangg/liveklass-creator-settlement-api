package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.controller;

import static io.mipangg.liveklasscreatorsettlementapi.global.TestUtil.genCancelCreateRequest;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.dto.CancelCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.service.CancelService;
import io.mipangg.liveklasscreatorsettlementapi.global.config.JacksonConfig;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CancelController.class)
@Import(JacksonConfig.class)
class CancelControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CancelService cancelService;

    @Test
    @DisplayName("취소 등록 API를 호출 할 수 있다")
    void createCancelSuccessTest() throws Exception {

        CancelCreateRequest req = genCancelCreateRequest();

        mockMvc.perform(post("/cancels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isCreated());

        verify(cancelService).saveCancel(req);

    }

    @Test
    @DisplayName("CancelCreateRequest 유효성 검증 실패로 취소 등록에 실패할 수 있다")
    void createCancelFailTest() throws Exception {

        CancelCreateRequest req = new CancelCreateRequest(
                "",
                BigDecimal.valueOf(80000),
                OffsetDateTime.parse("2025-03-21T09:00:00+09:00")
        );

        mockMvc.perform(post("/cancels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isBadRequest());

        verify(cancelService, never()).saveCancel(req);

    }

}