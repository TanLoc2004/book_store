package Nhom.VoNguyenTanLoc_2280601829.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Nhom.VoNguyenTanLoc_2280601829.daos.Cart;
import Nhom.VoNguyenTanLoc_2280601829.services.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @GetMapping
    public String showCart(HttpSession session, @NotNull Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("totalPrice",
        cartService.getSumPrice(session));
        model.addAttribute("totalQuantity",
        cartService.getSumQuantity(session));
        return "book/cart";
    }
    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(HttpSession session,
        @PathVariable Long id) {
        var cart = cartService.getCart(session);
        cart.removeItems(id);
        return "redirect:/cart";
    }
    // @GetMapping("/updateCart/{id}/{quantity}")
    // public String updateCart(HttpSession session,
    //     @PathVariable int  id,
    //     @PathVariable int quantity) {
    //     var cart = cartService.getCart(session);
    //     cart.updateItems(id, quantity);
    //     return "book/cart";
    // }
    @GetMapping("/updateCart/{id}/{quantity}")
    @ResponseBody
    public void updateCart(
            @PathVariable int id,
            @PathVariable int quantity,
            HttpSession session) {

        Cart cart = (Cart) session.getAttribute("cart");

        cart.updateItems(id, quantity);

        session.setAttribute("cart", cart);
    }
    @GetMapping("/clearCart")
    public String clearCart(HttpSession session) {
        cartService.removeCart(session);
        return "redirect:/cart ";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session) {
        cartService.saveCart(session);
        return "redirect:/cart";
    }
    
}
