package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с товарами (бизнес-логика, часть Model в MVC)
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // CREATE
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // READ - все активные
    public List<Product> findAll() {
        return productRepository.findByDeletedFalse();
    }

    // READ - по ID
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    // UPDATE
    public Product update(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setCategory(productDetails.getCategory());
        
        return productRepository.save(product);
    }

    // DELETE - логическое (одиночное)
    public void softDelete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        product.setDeleted(true);
        productRepository.save(product);
    }

    // DELETE - физическое (одиночное)
    public void hardDelete(Long id) {
        productRepository.deleteById(id);
    }

    // DELETE - логическое (множественное)
    public void softDeleteMultiple(List<Long> ids) {
        productRepository.softDeleteByIds(ids);
    }

    // DELETE - физическое (множественное)
    public void hardDeleteMultiple(List<Long> ids) {
        productRepository.hardDeleteByIds(ids);
    }

    // Восстановление удалённых
    public void restoreMultiple(List<Long> ids) {
        productRepository.restoreByIds(ids);
    }

    // Получить удалённые товары
    public List<Product> findDeleted() {
        return productRepository.findByDeletedTrue();
    }

    // Фильтрация с несколькими параметрами
    public List<Product> findByFilters(String name, Long categoryId, 
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByFilters(name, categoryId, minPrice, maxPrice);
    }

    // Сортировка
    public List<Product> findAllSorted(String sortBy, String sortDir) {
        boolean desc = "desc".equals(sortDir);
        
        switch (sortBy != null ? sortBy : "name") {
            case "price":
                return desc ? productRepository.findByDeletedFalseOrderByPriceDesc()
                           : productRepository.findByDeletedFalseOrderByPriceAsc();
            case "quantity":
                return desc ? productRepository.findByDeletedFalseOrderByQuantityDesc()
                           : productRepository.findByDeletedFalseOrderByQuantityAsc();
            case "name":
            default:
                return desc ? productRepository.findByDeletedFalseOrderByNameDesc()
                           : productRepository.findByDeletedFalseOrderByNameAsc();
        }
    }
}


