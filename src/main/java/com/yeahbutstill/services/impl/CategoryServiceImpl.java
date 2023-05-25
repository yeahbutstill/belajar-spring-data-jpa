package com.yeahbutstill.services.impl;

import com.yeahbutstill.entitys.Category;
import com.yeahbutstill.repositorys.CategoryRepository;
import com.yeahbutstill.services.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionOperations transactionOperations;
    private final PlatformTransactionManager transactionManager;

    public CategoryServiceImpl(CategoryRepository categoryRepository, TransactionOperations transactionOperations, PlatformTransactionManager transactionManager) {
        this.categoryRepository = categoryRepository;
        this.transactionOperations = transactionOperations;
        this.transactionManager = transactionManager;
    }

    public void error() {
        throw new RuntimeException("Ups rollback please");
    }

    // manual transaction
    @Override
    public void createCategoryProgrammaticTransaction() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            for (int i = 0; i < 100; i++) {
                Category category = new Category();
                category.setName("CATEGORY " + i);
                categoryRepository.save(category);
            }
            error();
        });
    }

    @Override
    public void createCategoryManualPlatfromTransactionManager() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setTimeout(300);
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus transaction = transactionManager.getTransaction(definition);
        try {
            for (int i = 0; i < 100; i++) {
                Category category = new Category();
                category.setName("CATEGORY MANUAL " + i);
                categoryRepository.save(category);
            }

            error();
            transactionManager.commit(transaction);
        } catch (Throwable throwable) {
            transactionManager.rollback(transaction);
            throw throwable;
        }
    }

    @Transactional // gampang emang,
    // tapi karena dia AOP kalau dipanggil di 1 object yang sama udeh dah wasalam
    @Override
    public void create() {
        for (int i = 0; i < 100; i++) {
            Category category = new Category();
            category.setName("CATEGORY " + i);
            categoryRepository.save(category);
        }
        error();
    }

    @Override
    public void test() {
        create(); // engga jalan transactionnya karena menggunakan AOP
    }

}
