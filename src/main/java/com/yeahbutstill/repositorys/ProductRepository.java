package com.yeahbutstill.repositorys;

import com.yeahbutstill.entitys.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            value = "SELECT p FROM Product p WHERE p.name LIKE :name or p.category.name LIKE :name",
            countQuery = "SELECT count(p) FROM Product p WHERE p.name LIKE :name or p.category.name LIKE :name"
    )
    Page<Product> searchProduct(@Param("name") String name, Pageable pageable);

    List<Product> searchProductUsingName(@Param("name") String name, Pageable pageable);

    @Transactional
    Integer deleteByName(String name);

    Boolean existsByName(String name);

    Long countByCategory_Name(String name);

    List<Product> findAllByCategory_Name(String name);

    List<Product> findAllByCategory_Name(String name, Sort sort);

    Page<Product> findAllByCategory_Name(String name, Pageable pageable);

}
