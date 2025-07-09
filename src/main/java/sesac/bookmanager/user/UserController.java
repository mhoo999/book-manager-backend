package sesac.bookmanager.user;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.ApiResponse;
import sesac.bookmanager.auth.AuthService;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.user.data.ChangePasswordRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    /** 내 정보 가져오기 **/
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Object>> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserInfo(customUserDetails.getUser().getId())));
    }

    /*** 비밀번호 변경 **/
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                              @RequestBody ChangePasswordRequestDto requestBody) {
        userService.changePassword(customUserDetails.getUser().getId(),requestBody);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    /** 회원 탈퇴 **/
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                          HttpServletResponse response) {
        userService.softDeleteUser(customUserDetails.getUser().getId());
        authService.logout(customUserDetails);
        ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true) //.secure(true)
                .sameSite("Strict").path("/").maxAge(0)
                .build();
        response.addHeader("Set-Cookie", expiredCookie.toString());
        return ResponseEntity.ok(ApiResponse.success(null));
    }


}
