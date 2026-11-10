package com.suhasm.ecommerce.entity;

public enum OrderSagaStatus {

    STARTED,

    STOCK_RESERVED,

    ORDER_CREATED,

    COMPLETED,

    ORDER_CREATION_FAILED,

    STOCK_RELEASED,

    COMPENSATED
}
