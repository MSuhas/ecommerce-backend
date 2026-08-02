package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.dto.CategoryRequestDTO;
import com.suhasm.ecommerce.dto.CategoryResponseDTO;
import com.suhasm.ecommerce.service.CategoryService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private  final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> saveCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        log.info("Saving category request: {}", categoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.saveCategory(categoryRequestDTO));
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<CategoryResponseDTO> searchCategoryById(@PathVariable Long id) {

        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CategoryResponseDTO>> findAllCategories() {

        return  ResponseEntity.ok(categoryService.findAllCategories());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id,
                                               @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok(
                categoryService.updateCategory(id, categoryRequestDTO)
        );
    }
}


