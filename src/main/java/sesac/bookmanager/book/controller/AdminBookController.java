package sesac.bookmanager.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.book.dto.request.CreateBookRequestDto;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.request.UpdateBookRequestDto;
import sesac.bookmanager.book.dto.response.BookIdResponseDto;
import sesac.bookmanager.book.dto.response.BookResponseDto;
import sesac.bookmanager.book.dto.response.PageBookResponseDto;
import sesac.bookmanager.book.service.BookService;

@Controller
@RequestMapping("/admin/v1/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;

    @GetMapping("/register")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new CreateBookRequestDto());
        return "admin/books/register";
    }

    @PostMapping("/register")
    public String createBook(
            @Valid @ModelAttribute CreateBookRequestDto request,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/books/register";
        }

        BookIdResponseDto result = bookService.createBook(request);
        return "redirect:/admin/v1/books/" + result.getBookId();
    }

    @GetMapping("/search")
    public String searchBooks(SearchBookRequestDto request, Model model) {
        PageBookResponseDto books = bookService.searchBooks(request);
        model.addAttribute("books", books);
        model.addAttribute("searchCondition", request);
        return "admin/books/list";
    }

    @GetMapping("/{bookId}")
    public String getBook(@PathVariable long bookId, Model model) {
        BookResponseDto book = bookService.getBook(bookId);
        model.addAttribute("book", book);
        return "admin/books/detail";
    }

    @GetMapping("/{bookId}/edit")
    public String showUpdateForm(@PathVariable long bookId, Model model) {
        BookResponseDto book = bookService.getBook(bookId);
        model.addAttribute("book", book);
        return "admin/books/edit";
    }

    @PostMapping("/{bookId}/edit")
    public String updateBook(
            @PathVariable Long bookId,
            @Valid @ModelAttribute UpdateBookRequestDto request,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bookId", bookId);
            return "admin/books/edit";
        }

        bookService.updateBook(bookId, request);
        return "redirect:/admin/v1/books/" + bookId;
    }

    @GetMapping
    public String bookList(Model model) {
        SearchBookRequestDto defaultRequest = new SearchBookRequestDto();

        PageBookResponseDto books = bookService.searchBooks(defaultRequest);
        model.addAttribute("books", books);
        model.addAttribute("searchCondition", defaultRequest);
        return "admin/books/list";
    }
}
