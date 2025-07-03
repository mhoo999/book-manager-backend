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
@RequestMapping("/admin/wish")
public class WishAdminController {

    private final WishService wishService;


    @PostMapping("/create")
    public String createWish(@RequestBody WishCreateRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        WishResponse response = wishService.createWish(request, customUserDetails);
        return "redirect:/admin/wish/" + response.getWishId();
    }

    @GetMapping
    public String getWishlist(WishSearchRequest search) {
        WishPageResponse pageResponse = wishService.getWishlist(search);
        return "/admin/wish";
    }

    @GetMapping("/{wishId}")
    public String getWishById(@PathVariable Integer wishId) {
        WishResponse response = wishService.getWishById(wishId);
        return "/admin/wish/" + response.getWishId();
    }

    @PutMapping("/{wishId}/progress")
    public String updateProgress(@PathVariable Integer wishId, @ModelAttribute WishStatusUpdateRequest request) {
        wishService.updateProgress(wishId, request);
        return "redirect:/admin/wish/" + wishId;
    }
}
