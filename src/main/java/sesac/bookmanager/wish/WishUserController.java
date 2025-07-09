package sesac.bookmanager.wish;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.ApiResponse;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.wish.data.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish")
public class WishUserController {

    private final WishService wishService;


    @PostMapping("/create")
    public ResponseEntity<WishResponse> createWish(@RequestBody WishCreateRequest request,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(wishService.createWish(request, customUserDetails));
    }

    @GetMapping
    public ResponseEntity<WishPageResponse> getWishlist(WishSearchRequest search) {
        return ResponseEntity.ok(wishService.getWishlist(search));
    }

    @GetMapping("/{wishId}")
    public ResponseEntity<WishResponse> getWishById(@PathVariable Integer wishId) {
        return ResponseEntity.ok(wishService.getWishById(wishId));
    }

    @GetMapping("/myWishes")
    public ResponseEntity<ApiResponse<?>> getMyWishes(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(ApiResponse.success(Map.of("myWishesCount", wishService.getMyWishes(customUserDetails.getUser().getId()))));
    }
}
