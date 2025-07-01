package sesac.bookmanager.rent.repository;

import org.springframework.data.domain.Page;
import sesac.bookmanager.rent.dto.request.SearchRentRequestDto;
import sesac.bookmanager.rent.dto.response.RentResponseDto;

public interface RentQueryRepository {
    Page<RentResponseDto> searchRents(SearchRentRequestDto request);
}
