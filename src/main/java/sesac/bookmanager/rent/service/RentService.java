package sesac.bookmanager.rent.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.admin.data.Admin;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.book.domain.BookItem;
import sesac.bookmanager.book.enums.BookStatus;
import sesac.bookmanager.book.repository.BookItemRepository;
import sesac.bookmanager.rent.domain.Rent;
import sesac.bookmanager.rent.dto.request.CreateRentRequestDto;
import sesac.bookmanager.rent.dto.request.SearchRentRequestDto;
import sesac.bookmanager.rent.dto.request.UpdateRentRequestDto;
import sesac.bookmanager.rent.dto.response.PageRentResponseDto;
import sesac.bookmanager.rent.dto.response.RentIdResponseDto;
import sesac.bookmanager.rent.dto.response.RentResponseDto;
import sesac.bookmanager.rent.enums.RentStatus;
import sesac.bookmanager.rent.repository.RentRepository;
import sesac.bookmanager.security.CustomAdminDetails;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.user.UserRepository;
import sesac.bookmanager.user.data.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RentService {

    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final BookItemRepository bookItemRepository;
    private final AdminRepository adminRepository;

    public RentIdResponseDto register(CreateRentRequestDto request, CustomUserDetails customUserDetails) {
        User user = userRepository.findById(customUserDetails.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 유저가 존재하지 않습니다: " + customUserDetails.getUser().getId()));

        BookItem bookItem = bookItemRepository.findByBookCodeWithLock(request.getBookCode())
                .orElseThrow(() -> new EntityNotFoundException("해당 도서 코드를 가진 도서가 존재하지 않습니다: " + request.getBookCode()));

        if (bookItem.getStatus() != BookStatus.RENTABLE) {
            throw new IllegalStateException("현재 대여 불가능한 도서입니다. 상태: " + bookItem.getStatus());
        }

        bookItem.setStatus(BookStatus.RENTED);

        Rent rent = new Rent();
        rent.setUser(user);
        rent.setBookItem(bookItem);
        rent.setStatus(RentStatus.REQUESTED);
        rent.setRentalDate(LocalDateTime.now());

        Rent savedRent = rentRepository.save(rent);
        return new RentIdResponseDto(savedRent.getId());
    }

    @Transactional(readOnly = true)
    public PageRentResponseDto searchRents(SearchRentRequestDto request) {
        Page<RentResponseDto> page = rentRepository.searchRents(request);
        return PageRentResponseDto.from(page.getContent(), request, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public RentResponseDto getRent(Long rentId) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new IllegalArgumentException("대여 기록을 찾을 수 없습니다: " + rentId));
        return RentResponseDto.from(rent);
    }

    public RentIdResponseDto updateRent(Long rentId, UpdateRentRequestDto request, CustomAdminDetails customAdminDetails) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new IllegalArgumentException("대여 기록을 찾을 수 없습니다: " + rentId));

        RentStatus newStatus = request.getStatus();

        switch (newStatus) {
            case RENTED:
                if (rent.getStatus() != RentStatus.REQUESTED) {
                    throw new IllegalStateException("대여요청 상태에서만 대여중으로 변경할 수 있습니다.");
                }
                rent.setStatus(RentStatus.RENTED);
                rent.setRentalDate(LocalDateTime.now());
                rent.setExpectedReturnDate(LocalDate.now().plusWeeks(2));
                int adminId = customAdminDetails.getAdmin().getId();
                Admin admin = adminRepository.findById(adminId)
                        .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 관리자가 존재하지 않습니다: " + adminId));
                rent.setAdmin(admin);
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
