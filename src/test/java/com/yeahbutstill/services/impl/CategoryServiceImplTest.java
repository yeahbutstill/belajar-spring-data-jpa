package com.yeahbutstill.services.impl;

import com.yeahbutstill.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    @Qualifier("categoryServiceImpl")
    private CategoryService categoryService;

    @Test
    void testSuccess() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.create();
        });
    }

    @Test
    void testFail() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.test();
        });
    }

    @Test
    void testProgrammatic() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.createCategoryProgrammaticTransaction();
        });
    }

    @Test
    void testManual() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.createCategoryManualPlatfromTransactionManager();
        });
    }

}