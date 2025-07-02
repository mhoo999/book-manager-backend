package sesac.bookmanager.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.ApiResponse;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.user.data.ChangePasswordRequestDto;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    /**
     * 내 정보 가져오기
     */
    @ResponseBody
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Object>> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserInfo(customUserDetails.getUser().getId())));
    }

    /**
     * 비밀번호 변경
     */
    @ResponseBody
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                              @RequestBody ChangePasswordRequestDto requestBody) {
        userService.changePassword(customUserDetails.getUser().getId(),requestBody);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 회원 탈퇴
     */
    @ResponseBody
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.softDeleteUser(customUserDetails.getUser().getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
