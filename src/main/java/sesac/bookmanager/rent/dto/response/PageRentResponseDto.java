package sesac.bookmanager.rent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.rent.dto.request.SearchRentRequestDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRentResponseDto {
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private List<RentResponseDto> rents;

    public static PageRentResponseDto from(List<RentResponseDto> rents, SearchRentRequestDto search, Long count) {
        int totalPages = (int) Math.ceil((double) count / search.getSize());
        return new PageRentResponseDto(
                search.getPage(),
                search.getSize(),
                count,
                totalPages,
                rents
        );
    }

}
