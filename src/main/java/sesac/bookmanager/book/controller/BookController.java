package sesac.bookmanager.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.response.BookResponseDto;
import sesac.bookmanager.book.dto.response.HomeBookResponseDto;
import sesac.bookmanager.book.dto.response.PageBookResponseDto;
import sesac.bookmanager.book.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<PageBookResponseDto> searchBooks(SearchBookRequestDto request) {
        return ResponseEntity.ok(bookService.searchBooks(request));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> getBook(@PathVariable long bookId) {
        return ResponseEntity.ok(bookService.getBook(bookId));
    }

    @GetMapping("/home")
    @ResponseBody
    public ResponseEntity<HomeBookResponseDto> getHomeBooks() {
        List<BookResponseDto> recommendedBooks = bookService.getRecommendedBooks(5); // 상위 5개
        List<BookResponseDto> newBooks = bookService.getNewBooks(5); // 최신 5개

        HomeBookResponseDto response = new HomeBookResponseDto(recommendedBooks, newBooks);
        return ResponseEntity.ok(response);
    }
}
