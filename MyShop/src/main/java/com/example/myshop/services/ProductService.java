package com.example.myshop.services;

import com.example.myshop.models.Product;
import com.example.myshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public List<Product> getAllProductOrderByDesk() {
        return productRepository.findByAllProductOrderByDesk();
    }

    public Product getProductId(int id) {
        Optional<Product> product_bd = productRepository.findById(id);
        return product_bd.orElse(null);
    }

    @Transactional
    public void saveProduct(Product product){
        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(int id, Product product) {
        product.setId(id);
        product.setDateTimeOfCreated(getProductId(id).getDateTimeOfCreated());
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    public Product getProductFindByTitle(Product product) {
        Optional<Product> product_bd = productRepository.findByTitle(product.getTitle());
        return product_bd.orElse(null);
    }

    public List<Product> getProductsContainingTitle(String title) {
        return productRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Product> getProductsByCategory(int id_category) {
        List<Product> products = new ArrayList<>();
        List<Product> products_bd = productRepository.getProductsByCategory(id_category);
        if(products_bd.isEmpty()) {
            return products;
        } else {
            return products_bd;
        }
    }

    public List<Product> getProductsALLCategoryAndPrice(int min, int max) {
        List<Product> products = new ArrayList<>();
        List<Product> products_bd = productRepository.getProductsALLCategoryAndPrice(min, max);
        if(products_bd.isEmpty()) {
            return products;
        } else {
            return products_bd;
        }
    }

    public List<Product> getProductsByCategoryAndPrice(int id_category, int min, int max) {
        List<Product> products = new ArrayList<>();
        List<Product> products_bd = productRepository.getProductsByCategoryAndPrice(id_category, min, max);
        if(products_bd.isEmpty()) {
            return products;
        } else {
            return products_bd;
        }
    }

    public List<Product> getProductsByCategoryAndPriceAndSortPriceDesc(int id_category, int min, int max) {
        List<Product> products = new ArrayList<>();
        List<Product> products_bd = productRepository.getProductsByCategoryAndPriceAndSortPriceDesc(id_category, min, max);
        if(products_bd.isEmpty()) {
            return products;
        } else {
            return products_bd;
        }
    }

    public List<Product> getProductsByCategoryAndPriceAndSortPriceAsc(int id_category, int min, int max) {
        List<Product> products = new ArrayList<>();
        List<Product> products_bd = productRepository.getProductsByCategoryAndPriceAndSortPriceAsc(id_category, min, max);
        if(products_bd.isEmpty()) {
            return products;
        } else {
            return products_bd;
        }
    }

    public List<Product> getProductsAllCategoryAndPriceAndSortPriceDesc(int min, int max) {
        List<Product> products = new ArrayList<>();
        List<Product> products_bd = productRepository.getProductsAllCategoryAndPriceAndSortPriceDesc(min, max);
        if(products_bd.isEmpty()) {
            return products;
        } else {
            return products_bd;
        }
    }

    public List<Product> getProductsAllCategoryAndPriceAndSortPriceAsc(int min, int max) {
        List<Product> products = new ArrayList<>();
        List<Product> products_bd = productRepository.getProductsAllCategoryAndPriceAndSortPriceAsc(min, max);
        if(products_bd.isEmpty()) {
            return products;
        } else {
            return products_bd;
        }
    }

}
