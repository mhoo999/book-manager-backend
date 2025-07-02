package sesac.bookmanager.rent.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sesac.bookmanager.admin.Admin;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.book.domain.BookItem;
import sesac.bookmanager.book.repository.BookItemRepository;
import sesac.bookmanager.rent.domain.Rent;
import sesac.bookmanager.rent.dto.request.CreateRentRequestDto;
import sesac.bookmanager.rent.dto.response.RentIdResponseDto;
import sesac.bookmanager.rent.dto.response.RentResponseDto;
import sesac.bookmanager.rent.enums.RentStatus;
import sesac.bookmanager.rent.repository.RentRepository;
import sesac.bookmanager.security.CustomAdminDetails;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.user.UserRepository;
import sesac.bookmanager.user.data.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
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

    private User testUser;
    private BookItem testBookItem;
    private Admin testAdmin;
    private Rent testRent;
    private CustomUserDetails customUserDetails;
    private CustomAdminDetails customAdminDetails;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setName("테스트유저");

        testBookItem = new BookItem();
        testBookItem.setId(1L);

        testAdmin = new Admin();
        testAdmin.setId(1);
        testAdmin.setName("테스트관리자");

        testRent = new Rent();
        testRent.setId(1);
        testRent.setUser(testUser);
        testRent.setBookItem(testBookItem);
        testRent.setStatus(RentStatus.REQUESTED);

        customUserDetails = mock(CustomUserDetails.class);
        given(customUserDetails.getUser()).willReturn(testUser);

//        customAdminDetails = mock(CustomAdminDetails.class);
//        given(customAdminDetails.getAdmin()).willReturn(testAdmin);
    }

    @Nested
    @DisplayName("대여 등록 테스트")
    class RegisterTest {

        @Test
        @DisplayName("정상적인 대여 등록")
        void registerRent_Success() {
            // given
            CreateRentRequestDto request = new CreateRentRequestDto();
            request.setBookId(1L);

            given(userRepository.findById(1)).willReturn(Optional.of(testUser));
            given(bookItemRepository.findById(1L)).willReturn(Optional.of(testBookItem));
            given(rentRepository.save(any(Rent.class))).willReturn(testRent);

            // when
            RentIdResponseDto result = rentService.register(request, customUserDetails);

            // then
            assertThat(result.getRentId()).isEqualTo(1L);
            then(rentRepository).should().save(any(Rent.class));
        }

        @Test
        @DisplayName("존재하지 않는 유저로 대여 등록 시 예외 발생")
        void registerRent_UserNotFound_ThrowException() {
            // given
            CreateRentRequestDto request = new CreateRentRequestDto();
            request.setBookId(1L);

            given(userRepository.findById(1)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> rentService.register(request,customUserDetails))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("해당 아이디를 가진 유저가 존재하지 않습니다");
        }
    }

}