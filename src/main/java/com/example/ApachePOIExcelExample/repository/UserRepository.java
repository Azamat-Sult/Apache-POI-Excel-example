package com.example.ApachePOIExcelExample.repository;

import com.example.ApachePOIExcelExample.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE (LOWER(u.email) LIKE LOWER(CONCAT('%',?1,'%')) OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%',?1,'%')))")
    List<UserEntity> findByEmailAndFullNameContainingIgnoreCase(String search);

}