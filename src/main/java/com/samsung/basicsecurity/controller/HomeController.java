package com.samsung.basicsecurity.controller;

import com.samsung.basicsecurity.repositories.CatalogRepository;
import com.samsung.basicsecurity.repositories.ProductRepository;
import com.samsung.basicsecurity.repositories.models.Catalogs;
import com.samsung.basicsecurity.repositories.models.Product;
import com.samsung.basicsecurity.repositories.models.User;
import com.samsung.basicsecurity.services.ProductService;
import com.samsung.basicsecurity.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    @Autowired
    CatalogRepository catalogRepository;

    //Demo nen dung truc tiep product repository
    @Autowired
    ProductService productService;

    @GetMapping("/")
    public String welcome(final Model model)
    {
        if(catalogRepository.count() == 0) {
            initData();
        }
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String Home(@RequestParam(value = "catalogId", required = false) Long catalogId,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       Model model)
    {
        if(catalogRepository.count() == 0) {
            initData();
        }

        List<Product> products;
        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProductsByName(keyword);
        } else if (catalogId != null) {
            products = productService.getProductsByCatalogId(catalogId);
        } else {
            products = productService.getAllProduct();
        }

        model.addAttribute("products", products);
        return "welcome";
    }

    private void initData(){
        catalogRepository.save(Catalogs.builder().name("Catalog 1").build());
        catalogRepository.save(Catalogs.builder().name("Catalog 2").build());

        Catalogs catalog1 = catalogRepository.findById(1L).get();
        Catalogs catalog2 = catalogRepository.findById(2L).get();

        List<Product> lstProduct= new ArrayList<>();
        lstProduct.add(
                Product.builder()
                        .id(1l)
                        .name("Sneakers for Women Running Shoes")
                        .picture("https://images-na.ssl-images-amazon.com/images/I/81kvvmH31SL._SR200,200_.jpg")
                        .price(821000)
                        .catalog(catalog1)
                        .build());

        lstProduct.add(
                Product.builder()
                        .id(2l)
                        .name("Women's Navida Sneaker")
                        .picture("https://images-na.ssl-images-amazon.com/images/I/71dr3ma507L._SR200,200_.jpg")
                        .catalog(catalog1)
                        .price(1423770).build());

        lstProduct.add(
                Product.builder()
                        .id(3l)
                        .name("Men's Charged Assert 10")
                        .picture("https://images-na.ssl-images-amazon.com/images/I/61O30g0cZmL._SR200,200_.jpg")
                        .catalog(catalog2)
                        .price(2352000).build());

        lstProduct.add(
                Product.builder()
                        .id(4l)
                        .name("Women's Uno- Stand On Air Sneaker")
                        .picture("https://images-na.ssl-images-amazon.com/images/I/71ArFp7g5bL._SR200,200_.jpg")
                        .catalog(catalog2)
                        .price(2171000).build());

        productService.saveAll(lstProduct);
    }
}
