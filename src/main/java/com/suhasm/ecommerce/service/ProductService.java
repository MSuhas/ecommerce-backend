package com.suhasm.ecommerce.service;

import com.suhasm.ecommerce.dto.ProductRequestDTO;
import com.suhasm.ecommerce.dto.ProductResponseDTO;
import com.suhasm.ecommerce.entity.Category;
import com.suhasm.ecommerce.entity.Product;
import com.suhasm.ecommerce.exception.CategoryNotFoundException;
import com.suhasm.ecommerce.exception.ProductNotFoundException;
import com.suhasm.ecommerce.mapper.ProductMapper;
import com.suhasm.ecommerce.repository.CategoryRepository;
import com.suhasm.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductResponseDTO saveProduct(ProductRequestDTO productRequestDTO) {

        log.info("save product {}", productRequestDTO);

        Long categoryID = productRequestDTO.getCategoryId();
        Category category = categoryRepository.findById(categoryID)
                .orElseThrow(() -> new CategoryNotFoundException(categoryID));

        log.info("get  category {}", category);
        Product product = productMapper.toEntity(productRequestDTO);
        product.setCategory(category);
        product = productRepository.save(product);

       return productMapper.toResponse(product);
    }

    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public Page<ProductResponseDTO> findAll(Pageable pageable) {
        Page<Product> productPage =  productRepository.findAll(pageable);
        return productPage.map(productMapper::toResponse);
    }

    @Cacheable(value = "products", key = "#p0", sync = true)
    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long ID) {
        System.out.println(">>> DATABASE CALLED <<<");
        return productMapper.toResponse(findEntityById(ID));

    }

    public List<ProductResponseDTO> findByName(String name) {
        Optional<List<Product>>optionalProducts = Optional.
                ofNullable(productRepository.findByNameContainingIgnoreCase(name));

        return optionalProducts
                .map(products -> products.stream().map(productMapper::toResponse).toList())
                .orElseThrow(() -> new ProductNotFoundException(name));
    }

    public List<ProductResponseDTO> searchProducts(String name, BigDecimal price) {

        Optional<List<Product>>optionalProducts = Optional.
                ofNullable(productRepository.findByNameContainingIgnoreCaseAndPriceGreaterThan(name, price));

        return optionalProducts
                .map(products -> products.stream().map(productMapper::toResponse).toList())
                .orElseThrow(() -> new ProductNotFoundException(name, price));

    }

    @CacheEvict(value = "products", key = "#p1")
    @Transactional
    public ProductResponseDTO updateProduct(ProductRequestDTO product, Long ID) {
        Product mProduct = findEntityById(ID);

        mProduct.setDescription(product.getDescription());
        mProduct.setName(product.getName());
        mProduct.setPrice(product.getPrice());
        mProduct.setStock(product.getStock());

        return productMapper.toResponse(productRepository.save(mProduct));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "products", key = "#p0")
    @Transactional
    public void deleteProduct(Long ID) {
        Product mProduct = findEntityById(ID);

        productRepository.delete(mProduct);
    }

    private Product findEntityById(Long ID) {
        return productRepository.findById(ID).
                orElseThrow(() -> new ProductNotFoundException(ID));
    }

}
