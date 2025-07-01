package sesac.bookmanager.wish;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.wish.data.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish")
public class WishController {

    private final WishService wishService;

    /*
     * 희망도서 작성(유저용) : `POST("/create")`
     * 희망도서 목록 : `GET`
     * 희망도서 열람 : `GET("/{wishId}")`
     * 희망도서 처리상태 전환(관리자용) : `PUT("/{wishId}/progress")`
    `*/

    @PostMapping("/create")
    public ResponseEntity<WishResponse> createWish(@RequestBody WishCreateRequest request) {
        return ResponseEntity.ok(wishService.createWish(request));
    }

    @GetMapping
    public ResponseEntity<WishPageResponse> getWishlist(@ModelAttribute WishSearchRequest search) {
        return ResponseEntity.ok(wishService.getWishlist(search));
    }

    @GetMapping("/{wishId}")
    public ResponseEntity<WishResponse> getWishById(@PathVariable Integer wishId) {
        return ResponseEntity.ok(wishService.getWishById(wishId));
    }

    @PutMapping("/{wishId}/progress")
    public ResponseEntity<WishResponse> updateProgress(@PathVariable Integer wishId, @ModelAttribute WishStatusUpdateRequest request) {
        return ResponseEntity.ok(wishService.updateProgress(wishId, request));
    }
}
