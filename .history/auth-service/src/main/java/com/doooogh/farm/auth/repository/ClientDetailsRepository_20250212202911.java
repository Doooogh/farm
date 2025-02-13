package com.doooogh.farm.auth.repository;

import com.doooogh.farm.auth.entity.ClientDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDetailsRepository extends JpaRepository<ClientDetailsEntity, String> {
} 