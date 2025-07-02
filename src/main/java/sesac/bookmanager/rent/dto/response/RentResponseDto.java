package sesac.bookmanager.rent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.admin.Admin;
import sesac.bookmanager.rent.domain.Rent;
import sesac.bookmanager.rent.enums.RentStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentResponseDto {
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

    public static RentResponseDto from(Rent rent) {
        return new RentResponseDto(
                rent.getStatus(),
                rent.getBookItem().getBook().getCover(),
                rent.getBookItem().getBook().getTitle(),
                rent.getBookItem().getBook().getStock(),
                Optional.ofNullable(rent.getAdmin())
                        .map(Admin::getName)
                        .orElse("미배정"),
                rent.getRentalDate(),
                rent.getUser().getName(),
                rent.getUser().getPhone(),
                rent.getUser().getEmail(),
                rent.getDescription()
        );
    }
}
