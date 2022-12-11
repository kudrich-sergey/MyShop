package com.example.myshop.repositories;

import com.example.myshop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByTitle(String title);

    List<Product> findByTitleContainingIgnoreCase(String name);

    @Query(value = "select * from products order by id desc", nativeQuery = true)
    List<Product> findByAllProductOrderByDesk();

    @Query(value = "select * from products where category_id=?1", nativeQuery = true)
    List<Product> getProductsByCategory(int id_category);

    @Query(value = "select * from products where (price >= ?1 and price <= ?2) order by id desc", nativeQuery = true)
    List<Product> getProductsALLCategoryAndPrice(int min, int max);

    @Query(value = "select * from products where category_id=?1 and (price >= ?2 and price <= ?3) order by id desc", nativeQuery = true)
    List<Product> getProductsByCategoryAndPrice(int id_category, int min, int max);

    @Query(value = "select * from products where category_id=?1 and (price >= ?2 and price <= ?3) order by price desc", nativeQuery = true)
    List<Product> getProductsByCategoryAndPriceAndSortPriceDesc(int id_category, int min, int max);

    @Query(value = "select * from products where category_id=?1 and (price >= ?2 and price <= ?3) order by price asc", nativeQuery = true)
    List<Product> getProductsByCategoryAndPriceAndSortPriceAsc(int id_category, int min, int max);

    @Query(value = "select * from products where (price >= ?1 and price <= ?2) order by price desc", nativeQuery = true)
    List<Product> getProductsAllCategoryAndPriceAndSortPriceDesc(int min, int max);

    @Query(value = "select * from products where (price >= ?1 and price <= ?2) order by price asc", nativeQuery = true)
    List<Product> getProductsAllCategoryAndPriceAndSortPriceAsc(int min, int max);

}
