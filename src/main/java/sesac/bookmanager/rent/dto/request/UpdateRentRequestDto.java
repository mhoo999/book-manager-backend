package sesac.bookmanager.rent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.rent.enums.RentStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRentRequestDto {
    private RentStatus status;
}
