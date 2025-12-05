package com.example.repository;

import com.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Репозиторий для работы с товарами (часть Model в MVC)
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Найти все не удалённые товары
    List<Product> findByDeletedFalse();

    // Фильтрация по имени
    List<Product> findByDeletedFalseAndNameContainingIgnoreCase(String name);

    // Фильтрация по категории
    List<Product> findByDeletedFalseAndCategoryId(Long categoryId);

    // Фильтрация по диапазону цен
    List<Product> findByDeletedFalseAndPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Комбинированная фильтрация
    @Query("SELECT p FROM Product p WHERE p.deleted = false " +
           "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> findByFilters(@Param("name") String name,
                                @Param("categoryId") Long categoryId,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice);

    // Сортировка
    List<Product> findByDeletedFalseOrderByNameAsc();
    List<Product> findByDeletedFalseOrderByNameDesc();
    List<Product> findByDeletedFalseOrderByPriceAsc();
    List<Product> findByDeletedFalseOrderByPriceDesc();
    List<Product> findByDeletedFalseOrderByQuantityAsc();
    List<Product> findByDeletedFalseOrderByQuantityDesc();

    // Логическое удаление
    @Modifying
    @Query("UPDATE Product p SET p.deleted = true WHERE p.id IN :ids")
    void softDeleteByIds(@Param("ids") List<Long> ids);

    // Физическое удаление нескольких записей
    @Modifying
    @Query("DELETE FROM Product p WHERE p.id IN :ids")
    void hardDeleteByIds(@Param("ids") List<Long> ids);

    // Восстановление удалённых
    @Modifying
    @Query("UPDATE Product p SET p.deleted = false WHERE p.id IN :ids")
    void restoreByIds(@Param("ids") List<Long> ids);

    // Найти удалённые товары
    List<Product> findByDeletedTrue();
}


