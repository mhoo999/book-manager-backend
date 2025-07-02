package sesac.bookmanager.rent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRentRequestDto {
    private long userId;
    private long bookId;
}
