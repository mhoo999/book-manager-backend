package sesac.bookmanager.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sesac.bookmanager.category.domain.Category;
import sesac.bookmanager.book.dto.request.CreateBookRequestDto;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.request.UpdateBookRequestDto;
import sesac.bookmanager.book.dto.response.BookIdResponseDto;
import sesac.bookmanager.book.dto.response.BookResponseDto;
import sesac.bookmanager.book.dto.response.PageBookResponseDto;
import sesac.bookmanager.book.service.BookService;
import sesac.bookmanager.category.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/v1/books")
@RequiredArgsConstructor
@Slf4j
public class AdminBookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping("/register")
    public String showCreateForm(Model model) {
        // 대분류 카테고리만 조회
        List<Category> largeCategories = categoryService.findLargeCategories();
        model.addAttribute("largeCategories", largeCategories);

        return "admin/books/register";
    }

    @PostMapping("/register")
    public String createBook(
            @Valid @ModelAttribute CreateBookRequestDto request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // 카테고리 코드 형식 검증
        if (request.getCategory() == null || request.getCategory().length() != 6) {
            bindingResult.rejectValue("category", "invalid.format", "올바른 카테고리를 선택해주세요.");
        } else if (!categoryService.isValidCategoryCode(request.getCategory())) {
            bindingResult.rejectValue("category", "invalid.category", "존재하지 않는 카테고리입니다.");
        }

        if (bindingResult.hasErrors()) {
            // 에러 발생 시 대분류 카테고리 다시 로드
            try {
                List<Category> largeCategories = categoryService.findLargeCategories();
                model.addAttribute("largeCategories", largeCategories);
            } catch (Exception e) {
                log.error("대분류 카테고리 로드 실패", e);
                model.addAttribute("largeCategories", new ArrayList<>());
            }
            return "admin/books/register";
        }

        try {
            BookIdResponseDto result = bookService.createBook(request);

            // 성공 메시지 설정
            redirectAttributes.addFlashAttribute("message", "정상적으로 등록되었습니다!");
            log.info("도서 등록 성공: bookId={}, title={}", result.getBookId(), request.getTitle());

            // 등록 페이지로 리다이렉트 (성공 메시지 표시 후 자동으로 리스트로 이동)
            return "redirect:/admin/v1/books/register";

        } catch (Exception e) {
            log.error("도서 등록 실패", e);
            model.addAttribute("errorMessage", "도서 등록 중 오류가 발생했습니다: " + e.getMessage());

            // 에러 발생 시에도 대분류 카테고리 로드
            try {
                List<Category> largeCategories = categoryService.findLargeCategories();
                model.addAttribute("largeCategories", largeCategories);
            } catch (Exception ex) {
                log.error("대분류 카테고리 로드 실패", ex);
                model.addAttribute("largeCategories", new ArrayList<>());
            }
            return "admin/books/register";
        }
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
