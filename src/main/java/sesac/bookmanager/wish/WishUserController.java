package sesac.bookmanager.wish;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.wish.data.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish")
public class WishUserController {

    private final WishService wishService;


    @GetMapping
    public ResponseEntity<WishPageResponse> getWishlist(WishSearchRequest search) {
        return ResponseEntity.ok(wishService.getWishlist(search));
    }

    @GetMapping("/{wishId}")
    public ResponseEntity<WishResponse> getWishById(@PathVariable Integer wishId) {
        return ResponseEntity.ok(wishService.getWishById(wishId));
    }
}
