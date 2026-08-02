package com.suhasm.ecommerce.dto;

import com.suhasm.ecommerce.entity.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryResponseDTO {

    private Long id;

    private String name;

    private List<Product> productDetails;

}




