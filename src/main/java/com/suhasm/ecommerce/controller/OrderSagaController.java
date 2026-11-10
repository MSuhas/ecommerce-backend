package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.dto.StartOrderSagaRequest;
import com.suhasm.ecommerce.service.OrderSagaOrchestrator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sagas/orders")
@RequiredArgsConstructor
public class OrderSagaController {

    private final OrderSagaOrchestrator sagaOrchestrator;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String startSaga(@Valid @RequestBody StartOrderSagaRequest orderSagaRequest) {

        return sagaOrchestrator.startSaga(orderSagaRequest);
    }

}
