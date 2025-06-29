package sesac.bookmanager.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.book.dto.request.CreateBookRequestDto;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.request.UpdateBookRequestDto;
import sesac.bookmanager.book.dto.response.BookCodeResponseDto;
import sesac.bookmanager.book.dto.response.BookResponseDto;
import sesac.bookmanager.book.dto.response.PageBookResponseDto;
import sesac.bookmanager.book.service.BookService;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/register")
    public ResponseEntity<BookCodeResponseDto> createBook(@RequestBody CreateBookRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request));
    }

    @GetMapping
    public ResponseEntity<PageBookResponseDto> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
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
    public ResponseEntity<BookCodeResponseDto> updateBook(@PathVariable Long bookId, @RequestBody UpdateBookRequestDto request) {
        return ResponseEntity.ok(bookService.updateBook(bookId, request));
    }

}
