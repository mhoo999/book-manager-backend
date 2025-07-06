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
        int pageValue = search.getPageValue();
        int sizeValue = search.getSizeValue();
        int totalPages = (int) Math.ceil((double) count / sizeValue);
        return new PageRentResponseDto(
                pageValue,
                sizeValue,
                count,
                totalPages,
                rents
        );
    }

//    // 페이지네이션 관련 유틸리티 메서드들
//    public boolean isFirstPage() {
//        return page == 0;
//    }
//
//    public boolean isLastPage() {
//        return page >= totalPages - 1;
//    }
//
//    public boolean hasPrevious() {
//        return page > 0;
//    }
//
//    public boolean hasNext() {
//        return page < totalPages - 1;
//    }
}