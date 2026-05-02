package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.controller;

import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementReadResponse;
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
    public SettlementReadResponse readSettlement(
            @Valid @ModelAttribute SettlementReadRequest req
    ) {
        return settlementService.findSettlement(req);
    }

}
