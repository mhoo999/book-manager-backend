package sesac.bookmanager.rent.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.rent.domain.Rent;
import sesac.bookmanager.rent.dto.request.CreateRentRequestDto;
import sesac.bookmanager.rent.dto.request.SearchRentRequestDto;
import sesac.bookmanager.rent.dto.request.UpdateRentRequestDto;
import sesac.bookmanager.rent.dto.response.PageRentResponseDto;
import sesac.bookmanager.rent.dto.response.RentIdResponseDto;
import sesac.bookmanager.rent.dto.response.RentResponseDto;
import sesac.bookmanager.rent.enums.RentStatus;
import sesac.bookmanager.rent.repository.RentRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RentService {

    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final BookItemRepository bookRepository;

    public RentIdResponseDto register(CreateRentRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 유저가 존재하지 않습니다: " + request.getUserId()));

        // todo: 개별 책의 id 또는 book-code 사용하여 조회해야 함
        BookItem bookItem = bookItemRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 도서가 존재하지 않습니다: " + request.getBookId()));

        Rent rent = new Rent();
        rent.setUser(user);
        rent.setBookItem(bookItem);
        rent.setStatus(RentStatus.REQUESTED);

        Rent savedRent = rentRepository.save(rent);
        return new RentIdResponseDto(savedRent.getId());
    }

    @Transactional(readOnly = true)
    public PageRentResponseDto searchRents(SearchRentRequestDto request) {
        return null;
    }

    @Transactional(readOnly = true)
    public RentResponseDto getRent(Long rentId) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new IllegalArgumentException("대여 기록을 찾을 수 없습니다: " + rentId));
        return RentResponseDto.from(rent);
    }

    public RentIdResponseDto updateRent(Long rentId, UpdateRentRequestDto request) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new IllegalArgumentException("대여 기록을 찾을 수 없습니다: " + rentId));

        RentStatus newStatus = rent.getStatus();

        switch (newStatus) {
            case RENTED:
                if (rent.getStatus() != RentStatus.REQUESTED) {
                    throw new IllegalStateException("대여요청 상태에서만 대여중으로 변경할 수 있습니다.");
                }
                rent.setStatus(RentStatus.RENTED);
                rent.setRentalDate(LocalDateTime.now());
                rent.setExpectedReturnDate(LocalDateTime.now().plusWeeks(2));
                // todo: 승인 관리자 정보 포함해야함
                break;
            case RETURNED:
                if (rent.getStatus() != RentStatus.RENTED && rent.getStatus() != RentStatus.OVERDUE) {
                    throw new IllegalStateException("대여중 또는 미납 상태에서만 반납할 수 있습니다.");
                }
                rent.setStatus(RentStatus.RETURNED);
                break;
            case OVERDUE:
                if (rent.getStatus() != RentStatus.RENTED) {
                    throw new IllegalStateException("대여중 상태에서만 미납으로 변경할 수 있습니다.");
                }
                rent.setStatus(RentStatus.OVERDUE);
                break;
            case REJECTED:
                if (rent.getStatus() != RentStatus.REQUESTED) {
                    throw new IllegalStateException("대여요청 상태에서만 거절로 변경할 수 있습니다.");
                }
                rent.setStatus(RentStatus.REJECTED);
            default:
                throw new IllegalArgumentException("유효하지 않은 상태입니다: " + newStatus);
        }

        return new RentIdResponseDto(rent.getId());
    }
}
