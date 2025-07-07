package sesac.bookmanager.wish;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.user.UserRepository;
import sesac.bookmanager.user.data.User;
import sesac.bookmanager.wish.data.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WishService wishService;

    private User user;
    private Wish wish;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1)
                .build();

        wish = Wish.builder()
                .wishId(1)
                .user(user)
                .author("가상작가")
                .bookName("가상제목")
                .publisher("가상출판사")
                .status(WishStatus.EXAMINING)
                .build();
    }

    @Test
    void 희망도서_신청_성공() {
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUser()).thenReturn(user);

        WishCreateRequest request = new WishCreateRequest();

        request.setAuthor("가상작가");
        request.setBookName("가상제목");
        request.setPublisher("가상출판사");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        when(wishRepository.save(any(Wish.class))).thenReturn(wish);

        WishResponse response = wishService.createWish(request, userDetails);

        assertEquals("가상작가", response.getAuthor());
        assertEquals("가상제목", response.getBookName());
        assertEquals("가상출판사", response.getPublisher());
        assertEquals(WishStatus.EXAMINING, response.getStatus());
        verify(wishRepository).save(any(Wish.class));
    }

    @Test
    void 희망도서_목록_조회_성공() {
        Wish wish2 = Wish.builder()
                .wishId(2)
                .user(user)
                .author("두번째 작가")
                .bookName("두번째 제목")
                .publisher("두번째 출판사")
                .status(WishStatus.EXAMINING)
                .build();

        WishSearchRequest search = new WishSearchRequest();
        search.setPage(0);
        search.setSize(10);

        Page<Wish> page = new PageImpl<>(List.of(wish, wish2));
        when(wishRepository.findAll(any(Pageable.class))).thenReturn(page);

        WishPageResponse response = wishService.getWishlist(search);

        assertEquals(2, response.getTotalCount());
        assertEquals("가상작가", response.getWishes().get(0).getAuthor());
        assertEquals("두번째 작가", response.getWishes().get(1).getAuthor());
    }

    @Test
    void 희망도서_단건_조회_성공() {
        when(wishRepository.findById(1)).thenReturn(Optional.of(wish));

        WishResponse response = wishService.getWishById(1);

        assertEquals("가상제목", response.getBookName());
        assertEquals("가상작가", response.getAuthor());
    }

    @Test
    void 희망도서_단건_조회_실패() {
        when(wishRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> wishService.getWishById(999));
    }

    @Test
    void 희망도서_상태_업데이트_성공() {
        when(wishRepository.findById(1)).thenReturn(Optional.of(wish));

        WishStatusUpdateRequest updateRequest = new WishStatusUpdateRequest();
        updateRequest.setStatus(WishStatus.APPROVED);

        WishResponse response = wishService.updateProgress(1, updateRequest);

        assertEquals(WishStatus.APPROVED, wish.getStatus());
        assertEquals(WishStatus.APPROVED, response.getStatus());
    }

    @Test
    void 희망도서_상태_업데이트_실패() {
        when(wishRepository.findById(999)).thenReturn(Optional.empty());

        WishStatusUpdateRequest updateRequest = new WishStatusUpdateRequest();
        updateRequest.setStatus(WishStatus.REJECTED);

        assertThrows(EntityNotFoundException.class, () -> wishService.updateProgress(999, updateRequest));
    }

}
