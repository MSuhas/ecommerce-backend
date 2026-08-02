package com.suhasm.ecommerce.mapper;

import com.suhasm.ecommerce.dto.CategoryRequestDTO;
import com.suhasm.ecommerce.dto.CategoryResponseDTO;
import com.suhasm.ecommerce.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequestDTO categoryRequestDTO);

    CategoryResponseDTO toResponse(Category category);
}
