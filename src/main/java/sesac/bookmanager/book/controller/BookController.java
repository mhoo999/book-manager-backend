package sesac.bookmanager.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.book.dto.request.CreateBookRequestDto;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.request.UpdateBookRequestDto;
import sesac.bookmanager.book.dto.response.BookIdResponseDto;
import sesac.bookmanager.book.dto.response.BookResponseDto;
import sesac.bookmanager.book.dto.response.PageBookResponseDto;
import sesac.bookmanager.book.service.BookService;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/register")
    public ResponseEntity<BookIdResponseDto> createBook(@Valid @ModelAttribute CreateBookRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request));
    }

    @GetMapping("/search")
    public ResponseEntity<PageBookResponseDto> searchBooks(SearchBookRequestDto request) {
        return ResponseEntity.ok(bookService.searchBooks(request));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> getBook(@PathVariable long bookId) {
        return ResponseEntity.ok(bookService.getBook(bookId));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookIdResponseDto> updateBook(@PathVariable Long bookId, @ModelAttribute UpdateBookRequestDto request) {
        return ResponseEntity.ok(bookService.updateBook(bookId, request));
    }

}
