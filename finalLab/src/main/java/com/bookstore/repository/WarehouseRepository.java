package com.bookstore.repository;

import com.bookstore.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByQuantityLessThanEqual(Integer quantity);
    Warehouse findByBookId(Long bookId);
}


