package com.yeahbutstill.services.impl;

import com.yeahbutstill.entitys.Category;
import com.yeahbutstill.repositorys.CategoryRepository;
import com.yeahbutstill.services.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional // gampang emang,
    // tapi kalau dipanggil di 1 object yang sama udeh dah wasalam
    @Override
    public void create() {
        for (int i = 0; i < 100; i++) {
            Category category = new Category();
            category.setName("CATEGORY " + i);
            categoryRepository.save(category);
        }

        throw new RuntimeException("Ups rollback please");
    }

    public void test() {
        create();
    }

}
