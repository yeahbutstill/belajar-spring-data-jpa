package com.yeahbutstill.repositorys;

import com.yeahbutstill.entitys.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testCreate() {
        Category category = new Category();
        category.setName("GADGET");
        categoryRepository.save(category);

        Assertions.assertNotNull(category.getId());
    }

    @Test
    void testUpdate() {
        Category category = categoryRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(category);

        category.setName("GADGET MURAH");
        categoryRepository.save(category);

        category = categoryRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(category);
        Assertions.assertEquals("GADGET MURAH", category.getName());
    }

    @Test
    void testQueryMethod() {
        Category category = categoryRepository.findFirstByNameEquals("GADGET MURAH").orElse(null);
        Assertions.assertNotNull(category);
        Assertions.assertEquals("GADGET MURAH", category.getName());

        List<Category> categoryList = categoryRepository.findAllByNameLike("%GADGET%");
        Assertions.assertNotNull(categoryList);
        Assertions.assertEquals(5, categoryList.size());
        Assertions.assertEquals("GADGET MURAH", categoryList.get(0).getName());
    }

    @Test
    void testAudit() {
        Category category = categoryRepository.findById(3004L).orElse(null);
        Assertions.assertNotNull(category);
        category.setName("GADGET COPETAN");
        categoryRepository.save(category);
        Assertions.assertNotNull(category.getLastModifiedDate());
    }

    @Test
    void testAudit2() {
        Category category = new Category();
        category.setName("Sample Audit");
        categoryRepository.save(category);

        Assertions.assertNotNull(category.getId());
        Assertions.assertNotNull(category.getCreatedDate());
        Assertions.assertNotNull(category.getLastModifiedDate());
    }

}