package com.suhasm.ecommerce.mapper;

import com.suhasm.ecommerce.dto.PaymentResponseDTO;
import com.suhasm.ecommerce.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "id", target = "paymentId")
    @Mapping(source = "order.id", target = "orderId")
    PaymentResponseDTO toResponse(Payment payment);
}
