package sesac.bookmanager.wish;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.wish.data.*;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {

    private final WishRepository wishRepository;

    public WishResponse createWish(WishCreateRequest request, CustomUserDetails customUserDetails) {
        Wish newWish = request.toDomain();

        newWish.setStatus(WishStatus.EXAMINING);
        newWish.setUser(customUserDetails.getUser());
        newWish.setDueDate(LocalDateTime.now());

        Wish saved = wishRepository.save(newWish);

        return WishResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public WishPageResponse getWishlist(WishSearchRequest search) {
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(Sort.Direction.DESC, "wishId"));

        Page<WishResponse> pagedWish = wishRepository.findAll(pageable)
                .map(WishResponse::from);

        return WishPageResponse.from(pagedWish.getContent(), search, pagedWish.getTotalElements());
    }

    @Transactional(readOnly = true)
    public WishResponse getWishById(Integer wishId) {
        return WishResponse.from(wishRepository.findById(wishId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 희망도서가 없습니다 : " + wishId)));
    }

    public WishResponse updateProgress(Integer wishId, WishStatusUpdateRequest request) {
        Wish targetWish = wishRepository.findById(wishId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 희망도서가 없습니다 : " + wishId));

        targetWish.setStatus(request.getStatus());

        return WishResponse.from(targetWish);
    }

    public WishStatisticsResponse getStatistics() {
        WishStatisticsResponse response = new WishStatisticsResponse();

        response.setTotalWish(wishRepository.findAll().size());
        response.setTotalUnsolvedWish(wishRepository.findByStatusBetween(WishStatus.EXAMINING, WishStatus.APPROVED).size());

        return response;
    }

    public int  getMyWishes(int id) {
        return wishRepository.countByUserId(id);
    }

    public WishPageResponse getMyWishlist(WishSearchRequest search, CustomUserDetails customUserDetails) {
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(Sort.Direction.DESC, "wishId"));

        Page<WishResponse> pagedWish = wishRepository.findByUser_Id(customUserDetails.getUser().getId(), pageable)
                .map(WishResponse::from);

        return WishPageResponse.from(pagedWish.getContent(), search, pagedWish.getTotalElements());
    }
}
