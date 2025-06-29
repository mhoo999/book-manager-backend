package sesac.bookmanager.book.service;

import org.springframework.stereotype.Service;
import sesac.bookmanager.book.dto.request.CreateBookRequestDto;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.request.UpdateBookRequestDto;
import sesac.bookmanager.book.dto.response.BookCodeResponseDto;
import sesac.bookmanager.book.dto.response.BookResponseDto;
import sesac.bookmanager.book.dto.response.PageBookResponseDto;

@Service
public class BookService {
    public BookCodeResponseDto createBook(CreateBookRequestDto request) {

    }

    public PageBookResponseDto getAllBooks() {
    }

    public PageBookResponseDto searchBooks(SearchBookRequestDto request) {
    }

    public BookResponseDto getBook(long bookId) {
    }

    public BookCodeResponseDto updateBook(Long bookId, UpdateBookRequestDto request) {
    }
}
