package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.dto.OrderResponseDTO;
import com.suhasm.ecommerce.dto.OrderStatusUpdateRequestDTO;
import com.suhasm.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private  final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOder() {
        log.info("Placing orders :");

        return ResponseEntity.ok(orderService.placeOrder());
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> findOrder(Pageable pageable) {
        log.info("Getting orders :");

        return ResponseEntity.ok(orderService.findOrders(pageable));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> findOrder(@PathVariable Long orderId){
        log.info("Getting order by id :");

        return ResponseEntity.ok(orderService.findOrder(orderId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long orderId,
                                                              @Valid @RequestBody OrderStatusUpdateRequestDTO orderStatusUpdateRequestDTO){
        log.info("Update orderStatus order by id : {} id  {}", orderStatusUpdateRequestDTO, orderId);

        return ResponseEntity.ok(orderService.updateOrderStatus(
                orderId,
                orderStatusUpdateRequestDTO.getStatus()));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrderStatus(@PathVariable Long orderId){
        log.info("Cancel orderStatus order by id :{}", orderId);

        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}
