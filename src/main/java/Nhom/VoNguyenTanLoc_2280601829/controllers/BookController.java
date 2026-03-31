package Nhom.VoNguyenTanLoc_2280601829.controllers;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Nhom.VoNguyenTanLoc_2280601829.daos.Item;
import Nhom.VoNguyenTanLoc_2280601829.entities.Book;
import Nhom.VoNguyenTanLoc_2280601829.services.BookService;
import Nhom.VoNguyenTanLoc_2280601829.services.CartService;
import Nhom.VoNguyenTanLoc_2280601829.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final CartService cartService;

    @GetMapping
    public String showAllBooks(@NotNull Model model, @RequestParam(defaultValue = "0") Integer pageNo,
                @RequestParam(defaultValue = "20") Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("books", bookService.getAllBooks(pageNo, pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("totalPages", bookService.getAllBooks(pageNo, pageSize, sortBy).size() / pageSize);
        return "book/list";
    }

    @GetMapping("/add")
    public String addBookForm(@NotNull Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/add";
    }

    @PostMapping("/add")
    public String addBook(
        @Valid @ModelAttribute("book") Book book,
        @NotNull BindingResult bindingResult,
        Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("categories",
            categoryService.getAllCategories());
            return "book/add";
        }
        bookService.addBook(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@NotNull Model model, @PathVariable long id)
    {
        var book = bookService.getBookById(id);
        model.addAttribute("book", book.orElseThrow(() -> new IllegalArgumentException("Book not found")));
        model.addAttribute("categories",
        categoryService.getAllCategories());
        return "book/edit";
    }
    @PostMapping("/edit")
    public String editBook(@Valid @ModelAttribute("book") Book book,
    @NotNull BindingResult bindingResult,
    Model model) {
    if (bindingResult.hasErrors()) {
    var errors = bindingResult.getAllErrors()
    .stream()
    .map(DefaultMessageSourceResolvable::getDefaultMessage)
    .toArray(String[]::new);
    model.addAttribute("errors", errors);
    model.addAttribute("categories",
    categoryService.getAllCategories());
    return "book/edit";
    }
    bookService.updateBook(book);
    return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.getBookById(id)
        .ifPresentOrElse(
        book -> bookService.deleteBookById(id),
        () -> { throw new IllegalArgumentException("Book not found"); });
        return "redirect:/books";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session,
        @RequestParam long id,
        @RequestParam String name,
        @RequestParam double price,
        @RequestParam(defaultValue = "1") int quantity)
        {
        var cart = cartService.getCart(session);
        cart.addItems(new Item(id, name, price, quantity));
        cartService.updateCart(session, cart);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBooks(@NotNull Model model,
                                @RequestParam String keyword,
                                @RequestParam(defaultValue = "0") Integer pageNo,
                                @RequestParam(defaultValue = "20") Integer pageSize,
                                @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("books", bookService.searchBook(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages",
        bookService
        .getAllBooks(pageNo, pageSize, sortBy)
        .size() / pageSize);
        model.addAttribute("categories",
        categoryService.getAllCategories());
        return "book/list";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable Long id, Model model) {

        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));

        model.addAttribute("book", book);

        return "book/detail";
    }
}

