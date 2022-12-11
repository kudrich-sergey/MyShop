package com.example.myshop.services;

import com.example.myshop.models.Category;
import com.example.myshop.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getNameCategory(String name) {
        return categoryRepository.findByName(name);
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

}
