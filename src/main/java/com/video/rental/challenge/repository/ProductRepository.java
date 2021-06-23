package com.video.rental.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.video.rental.challenge.entity.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

}
