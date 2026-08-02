package com.suhasm.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "role")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;
    @Column(nullable = false)
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
    @NotBlank
    private String phone;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @Column(nullable = false)
    private boolean active = true;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id" , nullable = false)
    private Role role;

}
