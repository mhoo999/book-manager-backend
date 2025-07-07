package sesac.bookmanager.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import sesac.bookmanager.book.domain.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookRequestDto {
    private String title;
    private String author;
    private String publisher;
    private String location;
    private String cover;
    private MultipartFile coverFile;
}
