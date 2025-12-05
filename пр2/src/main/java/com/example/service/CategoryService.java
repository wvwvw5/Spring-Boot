package com.example.service;

import com.example.model.Category;
import com.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с категориями (бизнес-логика, часть Model в MVC)
 */
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // CREATE
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // READ - все активные
    public List<Category> findAll() {
        return categoryRepository.findByDeletedFalse();
    }

    // READ - по ID
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    // UPDATE
    public Category update(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        
        return categoryRepository.save(category);
    }

    // DELETE - логическое (одиночное)
    public void softDelete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        category.setDeleted(true);
        categoryRepository.save(category);
    }

    // DELETE - физическое (одиночное)
    public void hardDelete(Long id) {
        categoryRepository.deleteById(id);
    }

    // DELETE - логическое (множественное)
    public void softDeleteMultiple(List<Long> ids) {
        categoryRepository.softDeleteByIds(ids);
    }

    // DELETE - физическое (множественное)
    public void hardDeleteMultiple(List<Long> ids) {
        categoryRepository.hardDeleteByIds(ids);
    }

    // Восстановление удалённых
    public void restoreMultiple(List<Long> ids) {
        categoryRepository.restoreByIds(ids);
    }

    // Получить удалённые категории
    public List<Category> findDeleted() {
        return categoryRepository.findByDeletedTrue();
    }

    // Фильтрация по имени
    public List<Category> findByName(String name) {
        if (name == null || name.isEmpty()) {
            return findAll();
        }
        return categoryRepository.findByDeletedFalseAndNameContainingIgnoreCase(name);
    }

    // Сортировка
    public List<Category> findAllSorted(String sortBy, String sortDir) {
        if ("name".equals(sortBy)) {
            return "desc".equals(sortDir) 
                ? categoryRepository.findByDeletedFalseOrderByNameDesc()
                : categoryRepository.findByDeletedFalseOrderByNameAsc();
        }
        return findAll();
    }
}


