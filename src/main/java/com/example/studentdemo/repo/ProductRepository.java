package com.example.studentdemo.repo;

import com.example.studentdemo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<Product, Long> {
}
