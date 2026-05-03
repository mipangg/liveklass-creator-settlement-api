package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.controller;

import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementMonthlyReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementMonthlyReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.service.SettlementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("/monthly")
    @ResponseStatus(HttpStatus.OK)
    public SettlementMonthlyReadResponse readMonthlySettlement(
            @Valid @ModelAttribute SettlementMonthlyReadRequest req
    ) {
        return settlementService.findSettlement(req);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SettlementSummaryReadResponse readSettlementSummary(
            @Valid @ModelAttribute SettlementSummaryReadRequest req
    ) {
        return settlementService.getSettlementSummary(req);
    }

}
