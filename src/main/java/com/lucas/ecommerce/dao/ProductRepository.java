package com.lucas.ecommerce.dao;


import com.lucas.ecommerce.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:4200")
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {


    //end-point: "http://localhost:8090/api/productEntities/search/findByCategoryId?id=#"
    Page<ProductEntity> findByCategoryId(@Param("id") Long id, Pageable pageable);
}
