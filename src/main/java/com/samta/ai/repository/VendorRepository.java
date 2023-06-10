package com.samta.ai.repository;

import com.samta.ai.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor,Integer> {
    Optional<Vendor> findByEmail(String email);
}
