package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.dto.ProductRequestDTO;
import com.suhasm.ecommerce.dto.ProductResponseDTO;
import com.suhasm.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> saveProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        log.info("Saving product request: {}", productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.saveProduct(productRequestDTO));
    }

   /* @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.findAll());
                .body(productService.findAll());
    }*/

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> findAll(Pageable pageable) {
        log.info("Find all called {}", pageable );
        return ResponseEntity.ok(productService.findAll(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> getProductByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @GetMapping("/searchProducts")
    public ResponseEntity<List<ProductResponseDTO>> getProductByNameAndPrice(@RequestParam String name,
                                                                             @RequestParam BigDecimal price) {
        return ResponseEntity.ok(productService.searchProducts(name, price));
    }



    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@RequestBody @Valid ProductRequestDTO product,
                                                 @PathVariable Long id) {
        return ResponseEntity.ok(productService.updateProduct(product, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
