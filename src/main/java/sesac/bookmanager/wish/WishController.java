package sesac.bookmanager.wish;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.wish.data.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/wish")
public class WishController {

    private final WishService wishService;


    @PostMapping("/create")
    public ResponseEntity<WishResponse> createWish(@RequestBody WishCreateRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(wishService.createWish(request, customUserDetails));
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<WishPageResponse> getWishlist(WishSearchRequest search) {
        return ResponseEntity.ok(wishService.getWishlist(search));
    }

    @ResponseBody
    @GetMapping("/{wishId}")
    public ResponseEntity<WishResponse> getWishById(@PathVariable Integer wishId) {
        return ResponseEntity.ok(wishService.getWishById(wishId));
    }

    @PutMapping("/{wishId}/progress")
    public String updateProgress(@PathVariable Integer wishId, @ModelAttribute WishStatusUpdateRequest request) {
        wishService.updateProgress(wishId, request);
        return "redirect:/api/wish/" + wishId;
    }
}
