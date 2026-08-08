package com.suhasm.ecommerce.mapper;

import com.suhasm.ecommerce.dto.CartItemResponseDTO;
import com.suhasm.ecommerce.dto.CartResponseDTO;
import com.suhasm.ecommerce.entity.Cart;
import com.suhasm.ecommerce.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "cartItems", target = "items")
    @Mapping(source = "id", target = "cartId")
    CartResponseDTO toResponse(Cart cart);

    @Mapping(source = "id", target = "cartItemId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    CartItemResponseDTO toResponse(CartItem cartItem);
}
