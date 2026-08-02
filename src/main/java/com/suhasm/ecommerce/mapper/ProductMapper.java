package com.suhasm.ecommerce.mapper;

import com.suhasm.ecommerce.dto.ProductRequestDTO;
import com.suhasm.ecommerce.dto.ProductResponseDTO;
import com.suhasm.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDTO dto);

    ProductResponseDTO toResponse(Product product);

}
