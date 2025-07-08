package sesac.bookmanager.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeBookResponseDto {
    private List<BookResponseDto> recommendedBooks;
    private List<BookResponseDto> newBooks;
}
