package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.controller;

import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.dto.CancelCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.service.CancelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cancels")
@RequiredArgsConstructor
public class CancelController {

    private final CancelService cancelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCancel(@Valid @RequestBody CancelCreateRequest req) {

        cancelService.saveCancel(req);

    }

}
