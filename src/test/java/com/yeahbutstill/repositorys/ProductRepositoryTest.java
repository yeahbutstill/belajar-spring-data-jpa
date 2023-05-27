package com.yeahbutstill.repositorys;

import com.yeahbutstill.entitys.Category;
import com.yeahbutstill.entitys.Product;
import com.yeahbutstill.model.ProductPrice;
import com.yeahbutstill.model.SimpleProduct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
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
    @Disabled
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

        assertEquals(12, products.size());
        assertEquals("Apple Iphone 13 Pro Max", products.get(0).getName());
        assertEquals("Apple Iphone 14 Pro Max", products.get(1).getName());
    }

    @Test
    void testFindProductByNameSort() {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        List<Product> products = productRepository.findAllByCategory_Name("GADGET MURAH", sort);

        assertEquals(12, products.size());
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
        assertEquals(12, products.getTotalElements());
        assertEquals(12, products.getTotalPages());
        assertEquals("Apple Iphone 13 Pro Max", products.getContent().get(0).getName());

        // page 1
        pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")));
        products = productRepository.findAllByCategory_Name("GADGET MURAH", pageable);
        assertEquals(1, products.getContent().size());
        assertEquals(1, products.getNumber());
        assertEquals(12, products.getTotalElements());
        assertEquals(12, products.getTotalPages());
        assertEquals("Apple Iphone 14 Pro Max", products.getContent().get(0).getName());

    }

    @Test
    void testCount() {
        Long count = productRepository.count();
        assertEquals(12L, count);

        count = productRepository.countByCategory_Name("GADGET MURAH");
        assertEquals(12L, count);

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
        assertEquals(6, products.size());
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
        assertEquals(12, products.getTotalElements());
        assertEquals("Apple Iphone 13 Pro Max", products.getContent().get(0).getName());

        products = productRepository.searchProduct("%%GADGET%%", pageable);
        assertEquals(8, products.getContent().size());
        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalPages());
        assertEquals(12, products.getTotalElements());
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

    @Test
    void testSlice() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Pageable firstPage = PageRequest.of(0, 8);
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Slice<Product> slice = productRepository.findAllByCategory(category, firstPage);
            // do with content
            while (slice.hasNext()) {
                slice = productRepository.findAllByCategory(category, slice.nextPageable());
            }
        });
    }

    @Test
    void testLock1() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            try {
                Product product = productRepository.findFirstByIdEquals(13L).orElse(null);
                assertNotNull(product);
                product.setPrice(30_000_000L);

                Thread.sleep(20_000L);
                productRepository.save(product);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testLock2() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Product product = productRepository.findFirstByIdEquals(13L).orElse(null);
            assertNotNull(product);
            product.setPrice(10_000_000L);
            productRepository.save(product);
        });
    }

    @Test
    void testLock3() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            try {
                Product product = productRepository.findFirstByIdEquals(13L).orElse(null);
                assertNotNull(product);
                product.setPrice(15_000_000L);

                Thread.sleep(10_000L);
                productRepository.save(product);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testSpecification() {
        Specification<Product> productSpecification = (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaQuery.where(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("name"), "Apple Iphone 13 Pro Max"),
                            criteriaBuilder.equal(root.get("name"), "Apple Iphone 14 Pro Max")
                    )
            ).getRestriction();
        };

        List<Product> products = productRepository.findAll(productSpecification);
        Assertions.assertEquals(12, products.size());
    }

    @Test
    void testProjection() {
        List<SimpleProduct> products = productRepository.findAllByNameLike("%Apple%", SimpleProduct.class);
        Assertions.assertEquals(12, products.size());

        List<ProductPrice> productPrices = productRepository.findAllByNameLike("%Apple%", ProductPrice.class);
        Assertions.assertEquals(12, productPrices.size());
    }



}