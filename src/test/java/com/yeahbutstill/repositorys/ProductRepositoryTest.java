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
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionOperations transactionOperations;

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

        assertEquals(10, products.size());
        assertEquals("Apple Iphone 14 Pro Max", products.get(0).getName());
        assertEquals("Apple Iphone 13 Pro Max", products.get(1).getName());
    }

    @Test
    void testFindProductByNameSort() {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        List<Product> products = productRepository.findAllByCategory_Name("GADGET MURAH", sort);

        assertEquals(10, products.size());
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
        assertEquals(10, products.getTotalElements());
        assertEquals(10, products.getTotalPages());
        assertEquals("Apple Iphone 13 Pro Max", products.getContent().get(0).getName());

        // page 1
        pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")));
        products = productRepository.findAllByCategory_Name("GADGET MURAH", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(1, products.getNumber());
        assertEquals(10, products.getTotalElements());
        assertEquals(10, products.getTotalPages());
        assertEquals("Apple Iphone 14 Pro Max", products.getContent().get(0).getName());

    }

    @Test
    void testCount() {
        Long count = productRepository.count();
        assertEquals(10L, count);

        count = productRepository.countByCategory_Name("GADGET MURAH");
        assertEquals(10L, count);

        count = productRepository.countByCategory_Name("MURAH AJA");
        assertEquals(0L, count);
    }

    @Test
    void testExistsByName() {
        Boolean exists = productRepository.existsByName("Apple Iphone 14 Pro Max");
        assertEquals(true, exists);

        // test not exists
        exists = productRepository.existsByName("Apple Iphone 15 Pro Max");
        assertEquals(false, exists);
    }

    @Test
    void testOldDeleteByName() {
        transactionOperations.executeWithoutResult(transactionStatus -> { // transaksi 1
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Product product = new Product();
            product.setName("Samsung Galaxy S9");
            product.setPrice(16_000_000L);
            product.setCategory(category);
            productRepository.save(product); // transaksi 1

            Integer samsungGalaxyS9 = productRepository.deleteByName("Samsung Galaxy S9"); // transaksi 1
            assertEquals(1, samsungGalaxyS9);

            // test not exists
            samsungGalaxyS9 = productRepository.deleteByName("Samsung Galaxy S9"); // transaksi 1
            assertEquals(0, samsungGalaxyS9);
        });
    }

    @Test
    void testDeleteByName() {
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Product product = new Product();
            product.setName("Samsung Galaxy S9");
            product.setPrice(16_000_000L);
            product.setCategory(category);
            productRepository.save(product); // transaksi 1

            Integer samsungGalaxyS9 = productRepository.deleteByName("Samsung Galaxy S9");
            assertEquals(1, samsungGalaxyS9); // transaksi 2

            // test not exists
            samsungGalaxyS9 = productRepository.deleteByName("Samsung Galaxy S9");
            assertEquals(0, samsungGalaxyS9); // transaksi 3
    }

    @Test
    void testProductNamedQuery() {
        Pageable pageable = PageRequest.of(0, 8);
        List<Product> products = productRepository.searchProductUsingName("Apple Iphone 14 Pro Max", pageable);
        assertEquals(5, products.size());
        assertEquals("Apple Iphone 14 Pro Max", products.get(0).getName());
        assertEquals("Apple Iphone 14 Pro Max", products.get(1).getName());
        assertEquals("Apple Iphone 14 Pro Max", products.get(2).getName());
        assertEquals("Apple Iphone 14 Pro Max", products.get(3).getName());
        assertEquals("Apple Iphone 14 Pro Max", products.get(4).getName());
    }

    @Test
    void testSearchProductLike() {
        Pageable pageable = PageRequest.of(0, 8, Sort.by(Sort.Order.desc("id")));
        Page<Product> products = productRepository.searchProduct("%Iphone%", pageable);
        assertEquals(8, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalPages());
        assertEquals(10, products.getTotalElements());
        assertEquals("Apple Iphone 13 Pro Max", products.getContent().get(0).getName());

        products = productRepository.searchProduct("%%GADGET%%", pageable);
        assertEquals(8, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalPages());
        assertEquals(10, products.getTotalElements());
        assertEquals("Apple Iphone 13 Pro Max", products.getContent().get(0).getName());
    }

    @Test
    void testModifying() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Integer total = productRepository.deleteProductUsingName("Wrong");
            assertEquals(0, total);

            total = productRepository.updateProductPriceToZero(13L);
            assertEquals(1, total);

            Product product = productRepository.findById(13L).orElse(null);
            assertNotNull(product);
            assertEquals(0, product.getPrice());
        });
    }

    @Test
    void testStream() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Stream<Product> products = productRepository.streamAllByCategory(category);
            products.forEach(product -> {
                System.out.println(product.getId() + " : " + product.getName());
            });
        });
    }

}