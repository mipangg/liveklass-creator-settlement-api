package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.controller;

import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordListReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordListReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.service.SaleRecordService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sale-records")
@RequiredArgsConstructor
public class SaleRecordController {

    private final SaleRecordService saleRecordService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createSaleRecord(
            @Valid @RequestBody SaleRecordCreateRequest req
    ) {

        saleRecordService.saveSaleRecord(req);

    }

    @GetMapping
    public List<SaleRecordListReadResponse> readSaleRecords(
            @Valid @ModelAttribute SaleRecordListReadRequest req
    ) {
        return saleRecordService.findSaleRecords(req);
    }
}
