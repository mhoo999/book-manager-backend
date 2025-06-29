package sesac.bookmanager.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;

import java.util.List;

@Data
@AllArgsConstructor
public class PageBookResponseDto {
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private List<BookResponseDto> books;

    public static PageBookResponseDto from(List<BookResponseDto> books, SearchBookRequestDto search, Long count) {
        int totalPages = (int) Math.ceil((double) count / search.getSize());
        return new PageBookResponseDto(
                search.getPage(),
                search.getSize(),
                count,
                totalPages,
                books
        );
    }
}
