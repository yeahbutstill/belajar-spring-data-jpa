package com.yeahbutstill.repositorys;

import com.yeahbutstill.entitys.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Query Method
    Optional<Category> findFirstByNameEquals(String name);

    List<Category> findAllByNameLike(String name);

}
