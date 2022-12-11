package com.example.myshop.controllers;

import com.example.myshop.models.Product;
import com.example.myshop.services.CategoryService;
import com.example.myshop.services.ProductService;
import com.example.myshop.util.CheckSelected;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public MainController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    public int valuesMin(List<Product> productList) {
        if(productList.size() > 0) {
            int minPrice = (int) productList.get(0).getPrice();
            for(Product product : productList) {
                if(product.getPrice() < minPrice) {
                    minPrice = (int) product.getPrice();
                }
            }
            return minPrice;
        } else {
            return 0;
        }
    }

    public int valuesMax(List<Product> productList) {
        if(productList.size() > 0) {
            int maxPrice = (int) productList.get(0).getPrice();
            for(Product product : productList) {
                if(product.getPrice() > maxPrice) {
                    maxPrice = (int) product.getPrice();
                }
            }
            return maxPrice;
        } else {
            return 0;
        }
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String getAllProduct(Model model) {
        List<Product> productList = productService.getAllProductOrderByDesk();
        CheckSelected checkSelected = new CheckSelected();
        model.addAttribute("checkSelected", checkSelected);
        model.addAttribute("selecting_category", 0);
        model.addAttribute("selecting_sorting", 0);
        model.addAttribute("minPrice", valuesMin(productList));
        model.addAttribute("maxPrice", valuesMax(productList));
        model.addAttribute("list_category", categoryService.getAllCategory());
        model.addAttribute("products", productList);
        return "visitor/products";
    }

    @GetMapping("/product/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.getProductId(id));
        return "visitor/infoProduct";
    }

    @PostMapping("/products/search")
    public String productSearch(@RequestParam("search") String search, Model model) {
        List<Product> productAllList = productService.getAllProductOrderByDesk();
        List<Product> productList = productService.getProductsContainingTitle(search);
        CheckSelected checkSelected = new CheckSelected();
        model.addAttribute("checkSelected", checkSelected);
        model.addAttribute("selecting_category", 0);
        model.addAttribute("selecting_sorting", 0);
        model.addAttribute("minPrice", valuesMin(productAllList));
        model.addAttribute("maxPrice", valuesMax(productAllList));
        model.addAttribute("value_search", search);
        model.addAttribute("list_category", categoryService.getAllCategory());
        model.addAttribute("products", productList);
        return "visitor/products";
    }

    @GetMapping("/products/category/{id}")
    public String getCategory(@PathVariable("id") int id, Model model) {
        List<Product> productAllList = productService.getAllProductOrderByDesk();
        List<Product> productList = productService.getProductsByCategory(id);
        CheckSelected checkSelected = new CheckSelected();
        model.addAttribute("checkSelected", checkSelected);
        model.addAttribute("selecting_category", id);
        model.addAttribute("selecting_sorting", 0);
        model.addAttribute("minPrice", valuesMin(productAllList));
        model.addAttribute("maxPrice", valuesMax(productAllList));
        model.addAttribute("list_category", categoryService.getAllCategory());
        model.addAttribute("products", productList);
        return "visitor/products";
    }

    @PostMapping("/products/sorting")
    public String sorting(@RequestParam("id_category") int id_category, @RequestParam("id_sort") int id_sort, @RequestParam(value = "min", required = false, defaultValue = "0") int min, @RequestParam(value = "max", required = false, defaultValue = "9999999") int max, Model model) {
        List<Product> productList = new ArrayList<>();

        if(id_category == 0 && id_sort == 0) {
            productList = productService.getProductsALLCategoryAndPrice(min, max);
        } else if(id_category > 0 && id_sort == 0) {
            productList = productService.getProductsByCategoryAndPrice(id_category, min, max);
        } else if(id_category > 0 && id_sort == 1) {
            productList = productService.getProductsByCategoryAndPriceAndSortPriceAsc(id_category, min, max);
        } else if(id_category > 0 && id_sort == 2) {
            productList = productService.getProductsByCategoryAndPriceAndSortPriceDesc(id_category, min, max);
        } else if(id_category == 0 && id_sort == 1) {
            productList = productService.getProductsAllCategoryAndPriceAndSortPriceAsc(min, max);
        } else if(id_category == 0 && id_sort == 2) {
            productList = productService.getProductsAllCategoryAndPriceAndSortPriceDesc(min, max);
        }

        model.addAttribute("minPrice", min);
        model.addAttribute("maxPrice", max);


        CheckSelected checkSelected = new CheckSelected();
        model.addAttribute("checkSelected", checkSelected);
        model.addAttribute("selecting_category", id_category);
        model.addAttribute("selecting_sorting", id_sort);
        model.addAttribute("list_category", categoryService.getAllCategory());
        model.addAttribute("products", productList);
        return "visitor/products";
    }

}
