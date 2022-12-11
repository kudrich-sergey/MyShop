package com.example.myshop.controllers;

import com.example.myshop.enums.Status;
import com.example.myshop.models.Cart;
import com.example.myshop.models.Order;
import com.example.myshop.models.Product;
import com.example.myshop.repositories.CartRepository;
import com.example.myshop.repositories.OrderRepository;
import com.example.myshop.security.PersonDetails;
import com.example.myshop.services.CartService;
import com.example.myshop.services.CategoryService;
import com.example.myshop.services.OrderService;
import com.example.myshop.services.ProductService;
import com.example.myshop.util.CheckSelected;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;

    private final CategoryService categoryService;

    @Autowired
    public UserController(ProductService productService, CartRepository cartRepository, CartService cartService, OrderRepository orderRepository, OrderService orderService, CategoryService categoryService) {
        this.productService = productService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.categoryService = categoryService;
    }

    private PersonDetails personDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails;
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

    @GetMapping("products")
    public String index(Model model) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/users";
        } else {
            String fio = personDetails().getUser().getLastname() + " " +
                    personDetails().getUser().getFirstname() + " " + personDetails().getUser().getPatronymic();

            model.addAttribute("fio", fio);
            model.addAttribute("role", personDetails().getUser().getTextRole());

            List<Product> productList = productService.getAllProductOrderByDesk();
            CheckSelected checkSelected = new CheckSelected();
            model.addAttribute("checkSelected", checkSelected);
            model.addAttribute("selecting_category", 0);
            model.addAttribute("selecting_sorting", 0);
            model.addAttribute("minPrice", valuesMin(productList));
            model.addAttribute("maxPrice", valuesMax(productList));
            model.addAttribute("list_category", categoryService.getAllCategory());
            model.addAttribute("products", productList);
            return "user/products";
        }
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/users";
        } else {
            int id_user = personDetails().getUser().getId();
            List<Cart> cartList = cartService.getUserById(id_user);
            List<Product> productList = new ArrayList<>();
            for (Cart cart: cartList) {
                productList.add(productService.getProductId(cart.getProductId()));
            }

            float price = 0;
            for (Product product: productList) {
                price += product.getPrice();
            }

            int sum_total = (int) price;
            if(price <= 3000) {
                sum_total += 149;
            }

            String fio = personDetails().getUser().getLastname() + " " +
                    personDetails().getUser().getFirstname() + " " + personDetails().getUser().getPatronymic();

            model.addAttribute("fio", fio);
            model.addAttribute("role", personDetails().getUser().getTextRole());
            model.addAttribute("price", price);
            model.addAttribute("sum_total", sum_total);
            model.addAttribute("cart_product", productList);
            return "user/cart";
        }
    }

    @GetMapping("/cart/add/{id}")
    public String addProductInCart(@PathVariable("id") int id) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/users";
        } else {
            Product product = productService.getProductId(id);
            int id_user = personDetails().getUser().getId();
            Cart cart = new Cart(id_user, product.getId());
            cartService.saveCart(cart);
            return "redirect:/user/cart";
        }
    }

    @GetMapping("/cart/delete/{id}")
    public String deleteProductFromCart(@PathVariable("id") int id) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/users";
        } else {
            int id_user = personDetails().getUser().getId();
            cartService.deleteProductFromCartById(id, id_user);
            return "redirect:/user/cart";
        }
    }

    @GetMapping("/product/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/users";
        } else {
            String fio = personDetails().getUser().getLastname() + " " +
                    personDetails().getUser().getFirstname() + " " + personDetails().getUser().getPatronymic();

            model.addAttribute("fio", fio);
            model.addAttribute("role", personDetails().getUser().getTextRole());
            model.addAttribute("product", productService.getProductId(id));
            return "user/infoProduct";
        }
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
        return "user/products";
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
        return "user/products";
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
        return "user/products";
    }

    @GetMapping("/orders")
    public String ordersUser(Model model) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/users";
        } else {
            String fio = personDetails().getUser().getLastname() + " " +
                    personDetails().getUser().getFirstname() + " " + personDetails().getUser().getPatronymic();

            model.addAttribute("fio", fio);
            model.addAttribute("role", personDetails().getUser().getTextRole());
            List<Order> orderList = orderService.getAllOrdersUserOrderByDesk(personDetails().getUser().getId());
            model.addAttribute("orders", orderList);
            return "/user/orders";
        }
    }

    @PostMapping("/order/create")
    public String createOrder(@RequestParam("count") int count, @RequestParam("sum_total") int sum_total, @RequestParam("address_text") String address) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/users";
        } else {
            int id_user = personDetails().getUser().getId();
            List<Cart> cartList = cartService.getUserById(id_user);
            List<Product> productList = new ArrayList<>();
            for (Cart cart: cartList) {
                productList.add(productService.getProductId(cart.getProductId()));
            }

            for (Product product: productList) {
                String uuid = UUID.randomUUID().toString();
                Order newOrder = new Order(uuid, count, sum_total, Status.Создан, product, personDetails().getUser(), address);
                orderService.saveOrder(newOrder);
                cartService.deleteProductFromCartById(product.getId(), id_user);
            }
            return "redirect:/user/orders";
        }
    }



}
