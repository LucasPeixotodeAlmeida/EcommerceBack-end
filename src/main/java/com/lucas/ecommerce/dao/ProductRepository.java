package com.lucas.ecommerce.dao;


import com.lucas.ecommerce.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
