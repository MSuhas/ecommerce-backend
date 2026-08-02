package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.dto.CategoryRequestDTO;
import com.suhasm.ecommerce.dto.CategoryResponseDTO;
import com.suhasm.ecommerce.entity.Category;
import com.suhasm.ecommerce.exception.CategoryAlreadyExistsException;
import com.suhasm.ecommerce.exception.CategoryNotFoundException;
import com.suhasm.ecommerce.mapper.CategoryMapper;
import com.suhasm.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryRequestDTO) {

        String name = categoryRequestDTO.getName().trim();

        if (categoryRepository.existsByNameIgnoreCase(name)) {

            throw new CategoryAlreadyExistsException(name);
        }

        Category category = categoryRepository.save(categoryMapper.toEntity(categoryRequestDTO));
        return categoryMapper.toResponse(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findCategoryById(Long id) {

        return categoryRepository.findById(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map( categoryMapper ::toResponse)
                .toList();
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        String name = categoryRequestDTO.getName().trim();

        if (categoryRepository.existsByNameIgnoreCase(name)) {
            return categoryMapper.toResponse(category);
        }

        category.setName(name);
        return categoryMapper.toResponse(category);

    }
}
