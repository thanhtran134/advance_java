package com.samsung.basicsecurity.controller;

import com.google.gson.Gson;
import com.samsung.basicsecurity.configuration.CustomUserDetails;
import com.samsung.basicsecurity.repositories.OrderDetailsRepository;
import com.samsung.basicsecurity.repositories.OrderRepository;
import com.samsung.basicsecurity.repositories.ProductRepository;
import com.samsung.basicsecurity.repositories.UserRepository;
import com.samsung.basicsecurity.repositories.models.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ShoppingController {
    private final String SHOPPING_CART_SESSION = "shoping_cart";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/shopping/addtocart")
    public String AddToCart(@RequestParam("productId") Long productId, HttpSession session)
    {
        Gson gson = new Gson();

        String cartInJson = (String) session.getAttribute(SHOPPING_CART_SESSION);
        ShoppingCart cart=null;
        //khoi tao neu chua co
        if(cartInJson==null)
            cart = new ShoppingCart();
        else
        {
            cart =gson.fromJson(cartInJson, ShoppingCart.class);
        }
        Product p = productRepository.findById(productId).get();
        Product product = new Product(p.getId(), p.getName(), p.getPicture(), p.getPrice(), null);


        for(ShoppingCartItem item: cart.items)
        {
            if(item.product.getId().equals(productId))
            {
                item.Qty++;
                session.setAttribute(SHOPPING_CART_SESSION, gson.toJson(cart));
                return "redirect:/shopping/view";
            }
        }

        ShoppingCartItem cartItem = new ShoppingCartItem();
        cartItem.product = product;
        cartItem.Qty =1;
        cart.items.add(cartItem);

        session.setAttribute(SHOPPING_CART_SESSION, gson.toJson(cart));
        return "redirect:/shopping/view";
    }

    @PostMapping("/shopping/increase")
    public String increaseQuantity(@RequestParam("productId") Long productId, HttpSession session) {
        String cartInJson = (String) session.getAttribute(SHOPPING_CART_SESSION);
        ShoppingCart cart=null;
        Gson gson = new Gson();
        if(cartInJson==null)
            cart = new ShoppingCart();
        else
        {
            cart =gson.fromJson(cartInJson, ShoppingCart.class);
        }

        if (cart != null) {
            for (ShoppingCartItem item : cart.items) {
                if (item.getProduct().getId().equals(productId)) {
                    item.setQty(item.getQty() + 1);
                    break;
                }
            }
            session.setAttribute(SHOPPING_CART_SESSION, gson.toJson(cart));
        }
        return "redirect:/shopping/view";
    }

    @PostMapping("/shopping/decrease")
    public String decreaseQuantity(@RequestParam("productId") Long productId, HttpSession session) {
        String cartInJson = (String) session.getAttribute(SHOPPING_CART_SESSION);
        ShoppingCart cart=null;
        Gson gson = new Gson();
        if(cartInJson==null)
            cart = new ShoppingCart();
        else
        {
            cart =gson.fromJson(cartInJson, ShoppingCart.class);
        }

        if (cart != null) {
            for (int i = 0; i < cart.items.size(); i++) {
                ShoppingCartItem item = cart.items.get(i);
                if (item.getProduct().getId().equals(productId)) {
                    item.setQty(item.getQty() - 1);
                    if (item.getQty() <= 0) {
                        cart.items.remove(i);
                    }
                    break;
                }
            }
            session.setAttribute(SHOPPING_CART_SESSION, gson.toJson(cart));
        }
        return "redirect:/shopping/view";
    }

    @PostMapping("/shopping/remove")
    public String removeItem(@RequestParam("productId") Long productId, HttpSession session) {
        String cartInJson = (String) session.getAttribute(SHOPPING_CART_SESSION);
        ShoppingCart cart=null;
        Gson gson = new Gson();
        if(cartInJson==null)
            cart = new ShoppingCart();
        else
        {
            cart =gson.fromJson(cartInJson, ShoppingCart.class);
        }

        if (cart != null) {
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            session.setAttribute(SHOPPING_CART_SESSION, gson.toJson(cart));
        }
        return "redirect:/shopping/view";
    }

    @PostMapping("/checkout")
    @Transactional
    public String checkout(HttpSession session){
        String cartInJson = (String) session.getAttribute(SHOPPING_CART_SESSION);
        ShoppingCart cart=null;
        Gson gson = new Gson();
        if(cartInJson==null)
           return "redirect:/home";
        else
        {
            cart =gson.fromJson(cartInJson, ShoppingCart.class);
        }

        Order order = Order.builder()
                .user(userRepository.findById(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId()).get())
                .totalQty(cart.getTotalQty())
                .totalAmount(cart.getTotalPrice())
                .build();
        orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for(ShoppingCartItem item: cart.items)
        {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(item.product);
            orderDetail.setUnitPrice(item.product.getPrice());
            orderDetail.setQuantity(item.Qty);
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);

        session.removeAttribute(SHOPPING_CART_SESSION);

        return "redirect:/home";
    }

    @GetMapping("/shopping/view")
    public String ViewShopping(final Model model, HttpSession session)
    {
        Gson gson = new Gson();
        String cartInJson = (String) session.getAttribute(SHOPPING_CART_SESSION);
        ShoppingCart cart = gson.fromJson(cartInJson, ShoppingCart.class);

        model.addAttribute("cart", cart);
        return "shoppingcart";
    }
}
