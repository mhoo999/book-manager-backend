package sesac.bookmanager.rent.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.admin.data.Admin;
import sesac.bookmanager.book.domain.Book;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RentService 테스트")
class RentServiceTest {

    @Mock
    private RentRepository rentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookItemRepository bookItemRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private RentService rentService;

    private User user;
    private BookItem bookItem;
    private Book book;
    private Admin admin;
    private Rent rent;

    @BeforeEach
    void setUp() {
        // User 설정
        user = User.builder()
                .id(1)
                .name("테스트 유저")
                .email("test@example.com")
                .phone("010-1234-5678")
                .pwd("password123")
                .isDeleted(false)
                .build();

        // Book 설정 (필수 필드들 포함)
        book = new Book();
        book.setId(1L);
        book.setTitle("테스트 도서");
        book.setAuthor("테스트 저자");
        book.setPublisher("테스트 출판사");
        book.setIsbn("9781234567890");
        book.setCategoryCode("100001");
        book.setStock(5);
        book.setCover("/images/cover.jpg");
        book.setLocation("A1-001");
        book.setDescription("테스트 도서 설명");
        book.setPublishedAt(LocalDateTime.now().minusDays(30));

        // BookItem 설정
        bookItem = new BookItem();
        bookItem.setId(1L);
        bookItem.setBookCode("BK1000010001");
        bookItem.setStatus(BookStatus.RENTABLE);
        bookItem.setBook(book);

        // Admin 설정
        admin = Admin.builder()
                .id(1)
                .accountId("admin01")
                .name("관리자")
                .pwd("adminpwd")
                .phone("010-9999-8888")
                .dept("IT")
                .position("팀장")
                .build();

        // Rent 설정
        rent = new Rent();
        rent.setId(1);
        rent.setUser(user);
        rent.setBookItem(bookItem);
        rent.setStatus(RentStatus.REQUESTED);
        rent.setRentalDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("대여 등록 성공")
    void register_Success() {
        // given
        CreateRentRequestDto request = new CreateRentRequestDto("BK1000010001");

        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        given(customUserDetails.getUser()).willReturn(user);

        given(userRepository.findById(1)).willReturn(Optional.of(user));
        given(bookItemRepository.findByBookCodeWithLock("BK1000010001")).willReturn(Optional.of(bookItem));
        given(rentRepository.save(any(Rent.class))).willReturn(rent);

        // when
        RentIdResponseDto result = rentService.register(request, customUserDetails);

        // then
        assertThat(result.getRentId()).isEqualTo(1L);
        assertThat(bookItem.getStatus()).isEqualTo(BookStatus.RENTED);

        ArgumentCaptor<Rent> rentCaptor = ArgumentCaptor.forClass(Rent.class);
        verify(rentRepository).save(rentCaptor.capture());
        Rent savedRent = rentCaptor.getValue();

        assertThat(savedRent.getUser()).isEqualTo(user);
        assertThat(savedRent.getBookItem()).isEqualTo(bookItem);
        assertThat(savedRent.getStatus()).isEqualTo(RentStatus.REQUESTED);
        assertThat(savedRent.getRentalDate()).isNotNull();
    }

    @Test
    @DisplayName("대여 등록 실패 - 존재하지 않는 유저")
    void register_Fail_UserNotFound() {
        // given
        CreateRentRequestDto request = new CreateRentRequestDto("BK1000010001");

        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        given(customUserDetails.getUser()).willReturn(user);

        given(userRepository.findById(1)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> rentService.register(request, customUserDetails))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 아이디를 가진 유저가 존재하지 않습니다: 1");
    }

    @Test
    @DisplayName("대여 등록 실패 - 존재하지 않는 도서")
    void register_Fail_BookItemNotFound() {
        // given
        CreateRentRequestDto request = new CreateRentRequestDto("INVALID_CODE");

        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        given(customUserDetails.getUser()).willReturn(user);

        given(userRepository.findById(1)).willReturn(Optional.of(user));
        given(bookItemRepository.findByBookCodeWithLock("INVALID_CODE")).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> rentService.register(request, customUserDetails))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 도서 코드를 가진 도서가 존재하지 않습니다: INVALID_CODE");
    }

    @Test
    @DisplayName("대여 등록 실패 - 대여 불가능한 도서")
    void register_Fail_BookItemNotRentable() {
        // given
        CreateRentRequestDto request = new CreateRentRequestDto("BK1000010001");
        bookItem.setStatus(BookStatus.RENTED);

        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        given(customUserDetails.getUser()).willReturn(user);

        given(userRepository.findById(1)).willReturn(Optional.of(user));
        given(bookItemRepository.findByBookCodeWithLock("BK1000010001")).willReturn(Optional.of(bookItem));

        // when & then
        assertThatThrownBy(() -> rentService.register(request, customUserDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("현재 대여 불가능한 도서입니다. 상태: RENTED");
    }

    @Test
    @DisplayName("대여 목록 검색 성공")
    void searchRents_Success() {
        // given
        SearchRentRequestDto request = new SearchRentRequestDto();
        request.setUsername("테스트");
        request.setPage(0);
        request.setSize(10);
        request.setSort("rentCode,DESC");

        List<RentResponseDto> rentList = Arrays.asList(
                createRentResponseDto(1, "테스트 유저1"),
                createRentResponseDto(2, "테스트 유저2")
        );
        Page<RentResponseDto> page = new PageImpl<>(rentList, PageRequest.of(0, 10), 2);

        given(rentRepository.searchRents(request)).willReturn(page);

        // when
        PageRentResponseDto result = rentService.searchRents(request);

        // then
        assertThat(result.getRents()).hasSize(2);
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("대여 단건 조회 성공")
    void getRent_Success() {
        // given
        Long rentId = 1L;
        given(rentRepository.findById(rentId)).willReturn(Optional.of(rent));

        // when
        RentResponseDto result = rentService.getRent(rentId);

        // then
        assertThat(result.getRentCode()).isEqualTo(1);
        assertThat(result.getUserName()).isEqualTo("테스트 유저");
        assertThat(result.getBookCode()).isEqualTo("BK1000010001");
        assertThat(result.getStatus()).isEqualTo(RentStatus.REQUESTED);
    }

    @Test
    @DisplayName("대여 단건 조회 실패 - 존재하지 않는 대여")
    void getRent_Fail_RentNotFound() {
        // given
        Long rentId = 999L;
        given(rentRepository.findById(rentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> rentService.getRent(rentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("대여 기록을 찾을 수 없습니다: 999");
    }

    @Test
    @DisplayName("대여 상태 수정 성공 - REQUESTED to RENTED")
    void updateRent_Success_RequestedToRented() {
        // given
        Long rentId = 1L;
        UpdateRentRequestDto request = new UpdateRentRequestDto(RentStatus.RENTED);

        CustomAdminDetails customAdminDetails = mock(CustomAdminDetails.class);
        given(customAdminDetails.getAdmin()).willReturn(admin);

        given(rentRepository.findById(rentId)).willReturn(Optional.of(rent));
        given(adminRepository.findById(1)).willReturn(Optional.of(admin));
        given(rentRepository.save(any(Rent.class))).willReturn(rent);

        // when
        RentIdResponseDto result = rentService.updateRent(rentId, request, customAdminDetails);

        // then
        assertThat(result.getRentId()).isEqualTo(1L);
        assertThat(rent.getStatus()).isEqualTo(RentStatus.RENTED);
        assertThat(rent.getExpectedReturnDate()).isEqualTo(LocalDate.now().plusWeeks(2));
        assertThat(rent.getAdmin()).isEqualTo(admin);
    }

    @Test
    @DisplayName("대여 상태 수정 성공 - REQUESTED to REJECTED")
    void updateRent_Success_RequestedToRejected() {
        // given
        Long rentId = 1L;
        UpdateRentRequestDto request = new UpdateRentRequestDto(RentStatus.REJECTED);

        CustomAdminDetails customAdminDetails = mock(CustomAdminDetails.class);
        given(customAdminDetails.getAdmin()).willReturn(admin);

        given(rentRepository.findById(rentId)).willReturn(Optional.of(rent));
        given(adminRepository.findById(1)).willReturn(Optional.of(admin));
        given(rentRepository.save(any(Rent.class))).willReturn(rent);

        // when
        RentIdResponseDto result = rentService.updateRent(rentId, request, customAdminDetails);

        // then
        assertThat(result.getRentId()).isEqualTo(1L);
        assertThat(rent.getStatus()).isEqualTo(RentStatus.REJECTED);
        assertThat(rent.getAdmin()).isEqualTo(admin);
    }

    @Test
    @DisplayName("대여 상태 수정 성공 - RENTED to RETURNED")
    void updateRent_Success_RentedToReturned() {
        // given
        Long rentId = 1L;
        rent.setStatus(RentStatus.RENTED);
        UpdateRentRequestDto request = new UpdateRentRequestDto(RentStatus.RETURNED);

        given(rentRepository.findById(rentId)).willReturn(Optional.of(rent));
        given(rentRepository.save(any(Rent.class))).willReturn(rent);

        // when - null을 전달하여 admin 없이 테스트
        RentIdResponseDto result = rentService.updateRent(rentId, request, null);

        // then
        assertThat(result.getRentId()).isEqualTo(1L);
        assertThat(rent.getStatus()).isEqualTo(RentStatus.RETURNED);
    }

    @Test
    @DisplayName("대여 상태 수정 성공 - RENTED to OVERDUE")
    void updateRent_Success_RentedToOverdue() {
        // given
        Long rentId = 1L;
        rent.setStatus(RentStatus.RENTED);
        UpdateRentRequestDto request = new UpdateRentRequestDto(RentStatus.OVERDUE);

        given(rentRepository.findById(rentId)).willReturn(Optional.of(rent));
        given(rentRepository.save(any(Rent.class))).willReturn(rent);

        // when - null을 전달하여 admin 없이 테스트
        RentIdResponseDto result = rentService.updateRent(rentId, request, null);

        // then
        assertThat(result.getRentId()).isEqualTo(1L);
        assertThat(rent.getStatus()).isEqualTo(RentStatus.OVERDUE);
    }

    @Test
    @DisplayName("대여 상태 수정 실패 - 잘못된 상태 전환 (RENTED to RENTED)")
    void updateRent_Fail_InvalidStatusTransition() {
        // given
        Long rentId = 1L;
        rent.setStatus(RentStatus.RENTED);
        UpdateRentRequestDto request = new UpdateRentRequestDto(RentStatus.RENTED);

        given(rentRepository.findById(rentId)).willReturn(Optional.of(rent));

        // when & then - null을 전달하여 admin 없이 테스트
        assertThatThrownBy(() -> rentService.updateRent(rentId, request, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("대여요청 상태에서만 대여중으로 변경할 수 있습니다.");
    }

    @Test
    @DisplayName("대여 상태 수정 실패 - 존재하지 않는 대여")
    void updateRent_Fail_RentNotFound() {
        // given
        Long rentId = 999L;
        UpdateRentRequestDto request = new UpdateRentRequestDto(RentStatus.RENTED);

        given(rentRepository.findById(rentId)).willReturn(Optional.empty());

        // when & then - null을 전달하여 admin 없이 테스트
        assertThatThrownBy(() -> rentService.updateRent(rentId, request, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("대여 기록을 찾을 수 없습니다: 999");
    }

    @Test
    @DisplayName("대여 상태 수정 실패 - 존재하지 않는 관리자")
    void updateRent_Fail_AdminNotFound() {
        // given
        Long rentId = 1L;
        UpdateRentRequestDto request = new UpdateRentRequestDto(RentStatus.RENTED);

        CustomAdminDetails customAdminDetails = mock(CustomAdminDetails.class);
        given(customAdminDetails.getAdmin()).willReturn(admin);

        given(rentRepository.findById(rentId)).willReturn(Optional.of(rent));
        given(adminRepository.findById(1)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> rentService.updateRent(rentId, request, customAdminDetails))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 아이디를 가진 관리자가 존재하지 않습니다: 1");
    }

    @Test
    @DisplayName("대여 메모 수정 성공")
    void updateRentMemo_Success() {
        // given
        Long rentId = 1L;
        String description = "대여 시 주의사항: 파손 주의";

        given(rentRepository.findById(rentId)).willReturn(Optional.of(rent));
        given(rentRepository.save(any(Rent.class))).willReturn(rent);

        // when
        RentIdResponseDto result = rentService.updateRentMemo(rentId, description);

        // then
        assertThat(result.getRentId()).isEqualTo(1L);
        assertThat(rent.getDescription()).isEqualTo(description);
    }

    @Test
    @DisplayName("대여 메모 수정 실패 - 존재하지 않는 대여")
    void updateRentMemo_Fail_RentNotFound() {
        // given
        Long rentId = 999L;
        String description = "테스트 메모";
        given(rentRepository.findById(rentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> rentService.updateRentMemo(rentId, description))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("대여 기록을 찾을 수 없습니다: 999");
    }

    @Test
    @DisplayName("상태 전환 검증 - OVERDUE to RETURNED 허용")
    void updateRent_Success_OverdueToReturned() {
        // given
        Long rentId = 1L;
        rent.setStatus(RentStatus.OVERDUE);
        UpdateRentRequestDto request = new UpdateRentRequestDto(RentStatus.RETURNED);

        given(rentRepository.findById(rentId)).willReturn(Optional.of(rent));
        given(rentRepository.save(any(Rent.class))).willReturn(rent);

        // when - null을 전달하여 admin 없이 테스트
        RentIdResponseDto result = rentService.updateRent(rentId, request, null);

        // then
        assertThat(result.getRentId()).isEqualTo(1L);
        assertThat(rent.getStatus()).isEqualTo(RentStatus.RETURNED);
    }

    @Test
    @DisplayName("상태 전환 검증 - RETURNED to RENTED 불허용")
    void updateRent_Fail_ReturnedToRented() {
        // given
        Long rentId = 1L;
        rent.setStatus(RentStatus.RETURNED);
        UpdateRentRequestDto request = new UpdateRentRequestDto(RentStatus.RENTED);

        given(rentRepository.findById(rentId)).willReturn(Optional.of(rent));

        // when & then - null을 전달하여 admin 없이 테스트
        assertThatThrownBy(() -> rentService.updateRent(rentId, request, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("대여요청 상태에서만 대여중으로 변경할 수 있습니다.");
    }

    // 헬퍼 메서드
    private RentResponseDto createRentResponseDto(int rentCode, String userName) {
        RentResponseDto dto = new RentResponseDto();
        dto.setRentCode(rentCode);
        dto.setUserName(userName);
        dto.setBookCode("BK1000010001");
        dto.setBookName("테스트 도서");
        dto.setStatus(RentStatus.REQUESTED);
        dto.setRentalDate(LocalDateTime.now());
        return dto;
    }
}