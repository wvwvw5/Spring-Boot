package com.example.repository;

import com.example.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с категориями (часть Model в MVC)
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Найти все не удалённые категории
    List<Category> findByDeletedFalse();

    // Найти по имени (для фильтрации)
    List<Category> findByDeletedFalseAndNameContainingIgnoreCase(String name);

    // Сортировка по имени
    List<Category> findByDeletedFalseOrderByNameAsc();
    List<Category> findByDeletedFalseOrderByNameDesc();

    // Логическое удаление
    @Modifying
    @Query("UPDATE Category c SET c.deleted = true WHERE c.id IN :ids")
    void softDeleteByIds(@Param("ids") List<Long> ids);

    // Физическое удаление нескольких записей
    @Modifying
    @Query("DELETE FROM Category c WHERE c.id IN :ids")
    void hardDeleteByIds(@Param("ids") List<Long> ids);

    // Восстановление удалённых
    @Modifying
    @Query("UPDATE Category c SET c.deleted = false WHERE c.id IN :ids")
    void restoreByIds(@Param("ids") List<Long> ids);

    // Найти удалённые категории
    List<Category> findByDeletedTrue();
}


