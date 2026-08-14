package com.suhasm.ecommerce.mapper;

import com.suhasm.ecommerce.dto.OrderItemResponseDTO;
import com.suhasm.ecommerce.dto.OrderResponseDTO;
import com.suhasm.ecommerce.entity.Order;
import com.suhasm.ecommerce.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderItems", target = "items")
    OrderResponseDTO toResponse(Order order);

    OrderItemResponseDTO toResponse(OrderItem orderItem);
}
