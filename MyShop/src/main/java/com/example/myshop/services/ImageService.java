package com.example.myshop.services;

import com.example.myshop.models.Image;
import com.example.myshop.repositories.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> getAllImage() {
        return imageRepository.findAll();
    }

    public Image getImageId(int id) {
        Optional<Image> image_bd = imageRepository.findById(id);
        return image_bd.orElse(null);
    }

    @Transactional
    public void deleteImage(int id_image) {
        imageRepository.deleteImage(id_image);
    }

}
