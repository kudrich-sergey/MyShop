package com.example.myshop.repositories;

import com.example.myshop.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Transactional
    @Modifying
    @Query(value = "delete from images where id=?1", nativeQuery = true)
    void deleteImage(int id_image);
}

