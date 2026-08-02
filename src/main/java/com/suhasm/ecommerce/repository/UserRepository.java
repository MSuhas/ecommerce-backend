package com.suhasm.ecommerce.repository;

import com.suhasm.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query("""
        select u
        from User u
        join fetch u.role
        where u.email = :email
        """)
    Optional<User> findByEmail(@Param("email") String email);
}
