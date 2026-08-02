package com.suhasm.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String profileImageUrl;

    private String role;

    private Boolean active;
}
