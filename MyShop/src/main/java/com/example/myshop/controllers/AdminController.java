package com.example.myshop.controllers;

import com.example.myshop.enums.Status;
import com.example.myshop.models.Image;
import com.example.myshop.models.Order;
import com.example.myshop.models.Product;
import com.example.myshop.repositories.ImageRepository;
import com.example.myshop.repositories.OrderRepository;
import com.example.myshop.security.PersonDetails;
import com.example.myshop.services.CategoryService;
import com.example.myshop.services.OrderService;
import com.example.myshop.services.ProductService;
import com.example.myshop.services.UserService;
import com.example.myshop.util.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${upload.path}")
    private String uploadPath;
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final ProductValidator productValidator;
    private final ImageRepository imageRepository;

    @Autowired
    public AdminController(ProductService productService, UserService userService, CategoryService categoryService, ProductValidator productValidator, OrderRepository orderRepository, OrderService orderService,
                           ImageRepository imageRepository) {
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.productValidator = productValidator;
        this.orderService = orderService;
        this.imageRepository = imageRepository;
    }

    private PersonDetails personDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails;
    }

    @GetMapping("")
    public String admin(Model model) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            return "redirect:/admin/users";
        }
    }

    @GetMapping("users")
    public String listUsers(Model model) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            String fio = personDetails().getUser().getLastname() + " " +
                    personDetails().getUser().getFirstname() + " " + personDetails().getUser().getPatronymic();
    
            model.addAttribute("fio", fio);
            model.addAttribute("role", personDetails().getUser().getTextRole());
            model.addAttribute("users", userService.getAllUsersOrderByDesk());

            return "admin/users";
        }
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            userService.deleteUser(id);
            return "redirect:/admin";
        }
    }

    @GetMapping("/user/role/up/{id}")
    public String userRoleUp(@PathVariable("id") int id) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            userService.updateRoleUser("ROLE_ADMIN", id);
            return "redirect:/admin";
        }
    }

    @GetMapping("/user/role/down/{id}")
    public String userRoleDown(@PathVariable("id") int id) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            userService.updateRoleUser("ROLE_USER", id);
            return "redirect:/admin";
        }
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        String role = personDetails().getUser().getRole();

        if (role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            String fio = personDetails().getUser().getLastname() + " " +
                    personDetails().getUser().getFirstname() + " " + personDetails().getUser().getPatronymic();

            model.addAttribute("fio", fio);
            model.addAttribute("role", personDetails().getUser().getTextRole());
            model.addAttribute("products", productService.getAllProductOrderByDesk());
            return "admin/products";
        }
    }

    @GetMapping("/product/add")
    public String addProduct(Model model) {
        String role = personDetails().getUser().getRole();

        if (role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            String fio = personDetails().getUser().getLastname() + " " +
                    personDetails().getUser().getFirstname() + " " + personDetails().getUser().getPatronymic();

            model.addAttribute("fio", fio);
            model.addAttribute("role", personDetails().getUser().getTextRole());
            model.addAttribute("product", new Product());
            model.addAttribute("category", categoryService.getAllCategory());
            return "admin/addProduct";
        }
    }

    private void uploadFile(Product product, MultipartFile multipartFile) throws IOException {
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName = uuidFile + "." + multipartFile.getOriginalFilename();
        multipartFile.transferTo(new File(uploadPath + "/" + resultFileName));
        Image image = new Image();
        image.setProduct(product);
        image.setFileName(resultFileName);
        product.addImageProduct(image);
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one") MultipartFile file_one, @RequestParam("file_two") MultipartFile file_two, @RequestParam("file_three") MultipartFile file_three, @RequestParam("file_four") MultipartFile file_four, @RequestParam("file_five") MultipartFile file_five, Model model) throws IOException {

        String role = personDetails().getUser().getRole();

        if (role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            productValidator.validate(product, bindingResult);
            if (bindingResult.hasErrors()) {
                String fio = personDetails().getUser().getLastname() + " " +
                        personDetails().getUser().getFirstname()+ " " + personDetails().getUser().getPatronymic();

                model.addAttribute("fio", fio);
                model.addAttribute("role", personDetails().getUser().getTextRole());
                model.addAttribute("category", categoryService.getAllCategory());
                return "admin/addProduct";
            }
            if (!file_one.isEmpty()) {
                uploadFile(product, file_one);
            }
            if (!file_two.isEmpty()) {
                uploadFile(product, file_two);
            }
            if (!file_three.isEmpty()) {
                uploadFile(product, file_three);
            }
            if (!file_four.isEmpty()) {
                uploadFile(product, file_four);
            }
            if (!file_five.isEmpty()) {
                uploadFile(product, file_five);
            }

            productService.saveProduct(product);
            return "redirect:/admin/products";
        }
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable("id") int id, Model model) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            String fio = personDetails().getUser().getLastname() + " " +
                    personDetails().getUser().getFirstname()  + " " + personDetails().getUser().getPatronymic();

            model.addAttribute("fio", fio);
            model.addAttribute("role", personDetails().getUser().getTextRole());
            model.addAttribute("editProduct", productService.getProductId(id));
            model.addAttribute("category", categoryService.getAllCategory());
            return "admin/editProduct";
        }
    }

    @PostMapping("/product/edit/{id}")
    public String editProduct(@ModelAttribute("editProduct") Product product, @PathVariable("id") int id) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            productService.updateProduct(id, product);
            return "redirect:/admin/products";
        }
    }

    @GetMapping("/product/{id_product}/image/delete/{id_img}")
    public String deleteImage(@PathVariable("id_product") int id_product, @PathVariable("id_img") int id_img) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            Product product = productService.getProductId(id_product);
            if(product.getImageList().size() > 1) {
                imageRepository.deleteImage(id_img);
            }
            return "redirect:/admin/product/edit/" + id_product;
        }
    }

    @PostMapping("/product/{id_product}/image/add")
    public String addProduct(@RequestParam("id_product") int id_product, @RequestParam("file") MultipartFile file) throws IOException {

        String role = personDetails().getUser().getRole();

        if (role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            if (!file.isEmpty()) {
                Product product = productService.getProductId(id_product);
                uploadFile(product, file);
                productService.saveProduct(product);
            }

            return "redirect:/admin/product/edit/" + id_product;
        }
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            productService.deleteProduct(id);
            return "redirect:/admin/products";
        }
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        String role = personDetails().getUser().getRole();

        if (role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            String fio = personDetails().getUser().getLastname() + " " +
                    personDetails().getUser().getFirstname() + " " + personDetails().getUser().getPatronymic();

            model.addAttribute("fio", fio);
            model.addAttribute("role", personDetails().getUser().getTextRole());
            List<Order> orderList = orderService.getAllOrdersOrderByDesk();
            model.addAttribute("orders", orderList);
            return "admin/orders";
        }
    }

    @GetMapping("/order/info/{id}")
    public String infoOrder(@PathVariable("id") int id, Model model) {
        String role = personDetails().getUser().getRole();

        if (role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            String fio = personDetails().getUser().getLastname() + " " +
                    personDetails().getUser().getFirstname() + " " + personDetails().getUser().getPatronymic();

            model.addAttribute("fio", fio);
            model.addAttribute("role", personDetails().getUser().getTextRole());
            model.addAttribute("order", orderService.getOrderId(id));

            HashMap<Integer, Status> statuses = new HashMap<>();
            int index = 0;
            for (Status el: Status.values()) {
                statuses.put(index, el);
                index++;
            }
            model.addAttribute("statuses", statuses);
            return "admin/infoOrder";
        }
    }

    @PostMapping("/order/change/status/{id}")
    public String changeStatus(@RequestParam("id_status") int id_status, @PathVariable("id") int id_order) {
        String role = personDetails().getUser().getRole();

        if(role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            orderService.updateStatusOrder(id_status, id_order);
            return "redirect:/admin/orders";
        }
    }


    @PostMapping("/search/orders")
    public String searchOrder(@RequestParam("search") String search, Model model) {
        String role = personDetails().getUser().getRole();

        if (role.equals("ROLE_USER")) {
            return "redirect:/user/products";
        } else {
            String fio = personDetails().getUser().getLastname() + " " +
                    personDetails().getUser().getFirstname() + " " + personDetails().getUser().getPatronymic();

            model.addAttribute("fio", fio);
            model.addAttribute("role", personDetails().getUser().getTextRole());
            model.addAttribute("value_search", search);
            List<Order> orderList = orderService.getOrdersByNumber(search.toLowerCase());
            model.addAttribute("orders", orderList);
            return "/admin/orders";
        }
    }

}
