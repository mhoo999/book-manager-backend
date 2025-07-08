package sesac.bookmanager.rent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRentRequestDto {
    private String searchType;      // 검색 유형: username, userPhone, bookName
    private String searchKeyword;   // 검색 키워드
    private String rentStatus;      // 대여 상태 필터

    // 기존 필드들
    private Integer rentCode;
    private Integer userId;
    private String username;
    private String bookCode;
    private String bookName;
    private String userPhone;

    private String sort = "rentCode,DESC";
    private Integer page = 0;       // Integer로 변경하여 null 허용
    private Integer size = 10;      // Integer로 변경하여 null 허용

    // null-safe getter 메서드들
    public int getPageValue() {
        return page != null ? page : 0;
    }

    public int getSizeValue() {
        return size != null ? size : 10;
    }

//    // 검색 조건이 있는지 확인하는 메서드
//    public boolean hasSearchCondition() {
//        return searchKeyword != null && !searchKeyword.trim().isEmpty();
//    }
//
//    // 상태 필터가 있는지 확인하는 메서드
//    public boolean hasStatusFilter() {
//        return rentStatus != null && !rentStatus.trim().isEmpty();
//    }
}