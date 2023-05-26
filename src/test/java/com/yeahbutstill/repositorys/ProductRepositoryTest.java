package com.yeahbutstill.repositorys;

import com.yeahbutstill.entitys.Category;
import com.yeahbutstill.entitys.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testCreateProduct() {
        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        {
            Product product = new Product();
            product.setName("Apple Iphone 14 Pro Max");
            product.setPrice(25_000_000L);
            product.setCategory(category);
            productRepository.save(product);
            assertNotNull(product);
        }

        {
            Product product = new Product();
            product.setName("Apple Iphone 13 Pro Max");
            product.setPrice(18_000_000L);
            product.setCategory(category);
            productRepository.save(product);
            assertNotNull(product);
        }

    }

    @Test
    void testFindProduct() {
        List<Product> products = productRepository.findAllByCategory_Name("GADGET MURAH");

        assertEquals(2, products.size());
        assertEquals("Apple Iphone 14 Pro Max", products.get(0).getName());
        assertEquals("Apple Iphone 13 Pro Max", products.get(1).getName());
    }

    @Test
    void testFindProductByNameSort() {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        List<Product> products = productRepository.findAllByCategory_Name("GADGET MURAH", sort);

        assertEquals(2, products.size());
        assertEquals("Apple Iphone 13 Pro Max", products.get(0).getName());
        assertEquals("Apple Iphone 14 Pro Max", products.get(1).getName());
    }

    @Test
    void testFindProductByNamePageable() {
        // page 0
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")));
        Page<Product> products = productRepository.findAllByCategory_Name("GADGET MURAH", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Apple Iphone 13 Pro Max", products.getContent().get(0).getName());

        // page 1
        pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")));
        products = productRepository.findAllByCategory_Name("GADGET MURAH", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(1, products.getNumber());
        assertEquals(2, products.getTotalElements());
        assertEquals(2, products.getTotalPages());
        assertEquals("Apple Iphone 14 Pro Max", products.getContent().get(0).getName());

    }

    @Test
    void testCount() {
        Long count = productRepository.count();
        assertEquals(2L, count);

        count = productRepository.countByCategory_Name("GADGET MURAH");
        assertEquals(2L, count);

        count = productRepository.countByCategory_Name("MURAH AJA");
        assertEquals(0L, count);
    }

}