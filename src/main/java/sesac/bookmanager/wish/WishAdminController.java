package sesac.bookmanager.wish;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.ApiResponse;
import sesac.bookmanager.wish.data.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/wish")
public class WishAdminController {

    private final WishService wishService;


    @GetMapping
    public String getWishlist(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {

        WishPageResponse pageResponse = null;

        if(page == null || page < 1) {
            pageResponse = wishService.getWishlist(new WishSearchRequest());
        } else {
            pageResponse = wishService.getWishlist(new WishSearchRequest(page - 1, 10));
        }

        model.addAttribute("page", pageResponse.getPage() + 1);
        model.addAttribute("totalPages", (pageResponse.getTotalPages() > 0) ? pageResponse.getTotalPages() : 1);
        model.addAttribute("totalCount", pageResponse.getTotalCount());
        model.addAttribute("wishes", pageResponse.getWishes());

        model.addAttribute("status", WishStatus.values());

        return "/admin/wish/list";
    }

    @GetMapping("/{wishId}")
    public String getWishById(Model model, @PathVariable Integer wishId) {
        WishResponse response = wishService.getWishById(wishId);

        WishStatusUpdateRequest updateRequest = new WishStatusUpdateRequest();
        updateRequest.setStatus(response.getStatus());

        model.addAttribute("wish", response);
        model.addAttribute("wishId", wishId);
        model.addAttribute("WishStatusUpdateRequest", updateRequest);
        return "/admin/wish/one";
    }

    @PostMapping("/{wishId}/progress")
    public String updateProgress(@PathVariable Integer wishId, @ModelAttribute WishStatusUpdateRequest request) {
        wishService.updateProgress(wishId, request);
        return "redirect:/admin/wish/" + wishId;
    }


    @ResponseBody
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<WishStatisticsResponse>> getStatistics() {

        return ResponseEntity.ok(ApiResponse.success(wishService.getStatistics()));
    }
}
