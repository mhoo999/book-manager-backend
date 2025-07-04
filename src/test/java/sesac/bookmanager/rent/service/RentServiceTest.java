package sesac.bookmanager.rent.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import sesac.bookmanager.admin.data.Admin;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.book.domain.BookItem;
import sesac.bookmanager.book.repository.BookItemRepository;
import sesac.bookmanager.rent.domain.Rent;
import sesac.bookmanager.rent.dto.request.CreateRentRequestDto;
import sesac.bookmanager.rent.dto.response.RentIdResponseDto;
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

}