package sesac.bookmanager.rent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.admin.Admin;
import sesac.bookmanager.rent.domain.Rent;
import sesac.bookmanager.rent.enums.RentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentResponseDto {
    // 기존 필드들
    private RentStatus status;
    private String bookCover;
    private String bookName;
    private int stock;
    private String adminName;
    private LocalDateTime rentalDate;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String description;

    // 리스트용 추가 필드들
    private Integer rentCode;        // 대여번호
    private String bookCode;         // 도서번호
    private LocalDate dueDate;       // 반납예정일
    private String rentStatus;       // 상태 (String)
    private String bookThumbnail;    // 썸네일
    private LocalDate rentDate;      // 대여일

    public static RentResponseDto from(Rent rent) {
        RentResponseDto dto = new RentResponseDto();

        // 기존 필드들
        dto.setStatus(rent.getStatus());
        dto.setBookCover(rent.getBookItem().getBook().getCover());
        dto.setBookName(rent.getBookItem().getBook().getTitle());
        dto.setStock(rent.getBookItem().getBook().getStock());
        dto.setAdminName(Optional.ofNullable(rent.getAdmin())
                .map(Admin::getName)
                .orElse("미배정"));
        dto.setRentalDate(rent.getRentalDate());
        dto.setUserName(rent.getUser().getName());
        dto.setUserPhone(rent.getUser().getPhone());
        dto.setUserEmail(rent.getUser().getEmail());
        dto.setDescription(rent.getDescription());

        // 리스트용 추가 필드들
        dto.setRentCode(rent.getId());
        dto.setBookCode(rent.getBookItem().getBookCode());
        dto.setDueDate(rent.getExpectedReturnDate());
        dto.setRentStatus(rent.getStatus().name());
        dto.setBookThumbnail(rent.getBookItem().getBook().getCover()); // 썸네일은 커버와 동일
        dto.setRentDate(rent.getRentalDate() != null ? rent.getRentalDate().toLocalDate() : null);

        return dto;
    }

//    // 상태 표시용 메서드들
//    public String getStatusDisplayName() {
//        switch (rentStatus) {
//            case "REQUESTED": return "대여요청";
//            case "RENTED": return "대여중";
//            case "RETURNED": return "반납";
//            case "OVERDUE": return "미납중";
//            default: return rentStatus;
//        }
//    }
}