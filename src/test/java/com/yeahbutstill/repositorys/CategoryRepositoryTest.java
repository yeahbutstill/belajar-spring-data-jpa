package com.yeahbutstill.repositorys;

import com.yeahbutstill.entitys.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

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

    @Test
    void testExample1() {
        Category category = new Category();
        category.setName("GADGET MURAH");

        Example<Category> example = Example.of(category);

        List<Category> categories = categoryRepository.findAll(example);
        Assertions.assertEquals(1, categories.size());
    }

    @Test
    void testExample2() {
        Category category = new Category();
        category.setName("GADGET MURAH");
        category.setId(1L);

        Example<Category> example = Example.of(category);

        List<Category> categories = categoryRepository.findAll(example);
        Assertions.assertEquals(1, categories.size());
    }

    @Test
    void testExampleMatcher() {
        Category category = new Category();
        category.setName("gadget murah");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase();

        Example<Category> example = Example.of(category, matcher);

        List<Category> categories = categoryRepository.findAll(example);
        Assertions.assertEquals(1, categories.size());
    }



}